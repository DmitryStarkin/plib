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

package com.starsoft.plib.interfaces

import com.starsoft.plib.interfaces.util.CALLBACK_IN

//This File Created at 05.04.2020 10:32.

/**
 * Run an instances [Processor]
 * @since 1.0
 */

interface ProcessorExecutor {
    /**
     * Processing [dataForProcessing] with the help of a implementation [Processor]
     * the result is returned by calling [onProcessedCallback]
     * the error is returned by calling [onErrorCallback]
     * @param processor instance off class that implements [Processor] that processes data
     * @param dataForProcessing data for processing  data interpretation depends
     * on the implementation [Processor]
     * @param onProcessedCallback the code that return the result,
     * if this code is missing will be run [stubCallback][ProcessorExecutor.stubCallback]
     * the result can be passed to the next task for processing see
     * [DELIVER_.TO_NEXT][com.starsoft.plib.interfaces.util.DELIVER_.TO_NEXT]
     * @param onErrorCallback the code that  handle the Exception,
     * if this code is missing will be run [stubErrorCallback][ProcessorExecutor.stubErrorCallback]
     * @param callbackIn specifies where the callback is performed,
     * see [CALLBACK_IN][com.starsoft.plib.interfaces.util.CALLBACK_IN]
     */
    fun <T, V> processing(
            processor: Processor<T, V>,
            dataForProcessing: T? = null,
            onProcessedCallback: (V) -> Unit = ::stubCallback,
            onErrorCallback: (Exception) -> Unit = ::stubErrorCallback,
            callbackIn: CALLBACK_IN = CALLBACK_IN._MAIN_THREAD
    )

    /**
     * This is stub
     * in case you don't need to send anything
     */
    private fun <V> stubCallback(par: V) {

    }

    /**
     * This is stub
     * in case you don't need to to handle Exception
     * the exception will simply be called
     */
    private fun stubErrorCallback(par: Exception) {
        throw par
    }
}


