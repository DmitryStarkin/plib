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

package com.starsoft.plib.handlers

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.starsoft.plib.runables.ProcessingRunnable

/**
 * @since 1.0
 */
const val DELIVER_ERROR = 0
const val  DELIVER_RESULT = 1

//This File Created at 05.04.2020 12:09.


class MainHandler private constructor(): Handler(Looper.getMainLooper()) {
    companion object {
        @JvmStatic
        val instance: MainHandler by lazy { (::MainHandler)() }
    }
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what){
            DELIVER_RESULT -> (msg.obj as ProcessingRunnable<*,*>).deliverResult()
            DELIVER_ERROR -> (msg.obj as ProcessingRunnable<*,*>).deliverError()
        }
    }

}