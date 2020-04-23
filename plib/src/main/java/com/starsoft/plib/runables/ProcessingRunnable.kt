/*
 * Copyright © 2020. Dmitry Starkin Contacts: t0506803080@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the «License»);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * //www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an «AS IS» BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.starsoft.plib.runables

import android.os.Looper
import android.util.Log
import com.starsoft.plib.BuildConfig
import com.starsoft.plib.handlers.DELIVER_ERROR
import com.starsoft.plib.handlers.DELIVER_RESULT
import com.starsoft.plib.handlers.MainHandler
import com.starsoft.plib.interfaces.Processor
import com.starsoft.plib.interfaces.util.CALLBACK_IN
import com.starsoft.plib.interfaces.util.NOT_NULL_DATA_STRATEGY


const val DFAULT_TAG = "no tag"

//This File Created at 05.04.2020 10:37.
/**
 * @since 1.0
 */
class ProcessingRunnable<T, V>(
        private val processor: Processor<T, V>,
        private var data: T?,
        private val onProcessed: ((V) -> Unit)?,
        private val onError: ((Exception) -> Unit)?,
        private val callbackIn: CALLBACK_IN,
        private val strategy: NOT_NULL_DATA_STRATEGY,
        private val tag: String = DFAULT_TAG) : Runnable {


    private var result: V? = null
    var error: Exception? = null
        private set
    var booleanFlag: Boolean = false
    var externalError: Throwable? = null
    var listener: EndListener? = null

    @Suppress("UNCHECKED_CAST")
    fun setData(d: Any?) {
        fun setD() {
            if (d is Exception) {
                error = d
            } else {
                data = d as T?
            }
        }
        if (data != null) {
            when (strategy) {
                NOT_NULL_DATA_STRATEGY.CHANGE -> setD()
                NOT_NULL_DATA_STRATEGY.ALLOW_IF_ERROR -> {
                    if (d !is java.lang.Exception) {
                        data = d as T?
                    }
                }
                NOT_NULL_DATA_STRATEGY.REJECT -> error =
                    Exception("impossible set previous task result because next task has not null data")
                NOT_NULL_DATA_STRATEGY.ALLOW -> {
                    if (d is Exception) {
                        error = d
                    }
                }
            }
        } else {
            setD()
        }
    }

    override fun run() {
        if (BuildConfig.DEBUG) {
            Log.d("AudioRecorderService", onProcessed.toString())
        }
        if (error == null) {
            try {
                result = data?.let { processor.processing(it) }
                    ?: throw Exception("no data for processing")
                command(DELIVER_RESULT)
            } catch (e: Exception) {
                e.printStackTrace()
                error = e
                command(DELIVER_ERROR)
            }
        } else {
            command(DELIVER_ERROR)
        }
    }

    fun deliverResult() {
        if (BuildConfig.DEBUG) {
            Log.d("AudioRecorderService", onProcessed.toString())
        }
        result?.let { onProcessed?.invoke(it) }

        callEndedCallback()
        finalize()
    }

    fun deliverError() {
        error?.let { onError?.invoke(it) }
        callEndedCallback()
        finalize()
    }

    private fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }

    private fun sendCommandToHandler(command: Int) {
        val message = MainHandler.instance.obtainMessage(command, this)
        message.sendToTarget()
    }

    private fun command(command: Int) {
        if (BuildConfig.DEBUG) {
            Log.d("AudioRecorderService", onProcessed.toString())
        }
        if (!Thread.currentThread().isInterrupted) {
            if (!isMainThread() && callbackIn == CALLBACK_IN._MAIN_THREAD) {
                sendCommandToHandler(command)
            } else {
                when (command) {
                    DELIVER_RESULT -> deliverResult()
                    DELIVER_ERROR -> deliverError()
                }
            }
        }
    }

    fun callEndedCallback() {
        listener?.onRunnableEnd(this)
    }

    //TODO probably don't need
    private fun finalize() {
        result = null
        listener = null
    }

    interface EndListener {
        fun onRunnableEnd(runnable: ProcessingRunnable<*, *>)
    }
}