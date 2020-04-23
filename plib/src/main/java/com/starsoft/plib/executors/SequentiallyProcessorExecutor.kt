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

package com.starsoft.plib.executors

import android.os.Looper
import android.util.Log
import com.starsoft.plib.executors.commands.ExecutorCommands
import com.starsoft.plib.executors.workers.SingleThreadPoolExecutor
import com.starsoft.plib.handlers.MainHandler
import com.starsoft.plib.interfaces.Processor
import com.starsoft.plib.interfaces.ProcessorExecutor
import com.starsoft.plib.interfaces.util.CALLBACK_IN
import com.starsoft.plib.interfaces.util.DELIVER_
import com.starsoft.plib.interfaces.util.NOT_NULL_DATA_STRATEGY
import com.starsoft.plib.runables.ProcessingRunnable

//This File Created at 06.04.2020 10:56.

/**
 * starts [Processor][Processor] to run sequentially using a pool of threads with a single thread
 * tasks that do not come from the main thread are rejected
 * can be controlled by passing special commands in the [processing] parameter onProcessedCallback
 * see [com.starsoft.plib.executors.commands.ExecutorCommands]
 * after received command [ExecutorCommands.ShutdownNow] all new tasks are rejected
 * if the [processing] onProcessedCallback parameter was passed when calling the command
 * [ExecutorCommands.ShutdownNow] the list of outstanding tasks will be returned in it
 * for correct processing of the [ExecutorCommands.ShutdownNow]
 * all of the processor implementation must include verification interrupt by
 * @see java.lang.Thread.isInterrupted()
 * and ensure that work is completed correctly
 * @property strategy
 * @property worker a pool of threads with a single thread for performing tasks
 * can also be used for performing external tasks by directly adding
 * @constructor Creates an [SequentiallyProcessorExecutor]
 * @since 1.0
 */
class SequentiallyProcessorExecutor : ProcessorExecutor {

    private val TAG = this::class.java.simpleName

    var strategy: NOT_NULL_DATA_STRATEGY = NOT_NULL_DATA_STRATEGY.REJECT
    val worker = SingleThreadPoolExecutor<Any?>()
    private var stopped = false

    /**
     * processing data, see [com.starsoft.plib.interfaces.ProcessorExecutor.processing]
     * @see com.starsoft.plib.interfaces.ProcessorExecutor
     */
    override fun <T, V> processing(
        processor: Processor<T, V>,
        dataForProcessing: T?,
        onProcessedCallback: (V) -> Unit,
        onErrorCallback: (Exception) -> Unit,
        callbackIn: CALLBACK_IN
    ) {

        fun deliverError(errorDescription: String) {
            if (!isMainThread() && callbackIn == CALLBACK_IN._MAIN_THREAD) {
                MainHandler.instance.post { onErrorCallback(Exception(errorDescription)) }
            } else {
                onErrorCallback(Exception(errorDescription))
            }
        }

        fun deliverResult(result: V) {
            if (!isMainThread() && callbackIn == CALLBACK_IN._MAIN_THREAD) {
                MainHandler.instance.post { onProcessedCallback(result) }
            } else {
                onProcessedCallback(result)
            }
        }

        if (!stopped) {
            when (processor) {
                is ExecutorCommands.DeleteAllNotProcessingTasks -> {
                    worker.queue.clear()
                }
                is ExecutorCommands.ShutdownNow -> {
                    stopped = true
                    @Suppress("UNCHECKED_CAST")
                    deliverResult(processor.processing(worker.shutdownNow()) as V)
                }
                else -> {
                    if (isMainThread()) {
                        if (onProcessedCallback == DELIVER_.TO_NEXT) {
                            Log.d(TAG, "true")
                            val task = ProcessingRunnable(
                                processor,
                                dataForProcessing,
                                worker,
                                worker,
                                CALLBACK_IN._CALLING_THREAD,
                                strategy
                            )
                            worker.execute(task)
                        } else {
                            val task = ProcessingRunnable(
                                processor,
                                dataForProcessing,
                                onProcessedCallback,
                                onErrorCallback,
                                callbackIn,
                                strategy
                            )
                            worker.execute(task)
                        }
                    } else {
                        deliverError("Not possible execute a task from not main thread")
                    }
                }
            }
        } else {
            deliverError("impossible run task executor is stopped")
        }
    }

    private fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }

    fun executeRunnableList(list: MutableList<Runnable>) {
        if (!stopped) {
            for (task in list) {
                worker.execute(task)
            }
        } else {
            throw Exception("impossible run task executor is stopped")
        }
    }
}