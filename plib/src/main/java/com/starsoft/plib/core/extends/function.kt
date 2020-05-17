/*
 * Copyright (c) 2020. Dmitry Starkin Contacts: t0506803080@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the «License»);
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  //www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an «AS IS» BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.starsoft.plib.core.extends

import com.starsoft.plib.core.auxiliary.stub
import com.starsoft.plib.core.auxiliary.stubErrorCallback
import com.starsoft.plib.core.interfaces.Processor
import com.starsoft.plib.core.interfaces.ProcessorExecutor
import com.starsoft.plib.core.triggers.CALLBACK_IN

//This File Created at 15.05.2020 15:51.

/**
 * Calls the specified function [lambda]
 * as its receiver and returns its result as callback
 * call is made on [ProcessorExecutor]
 * @receiver a [ProcessorExecutor][com.starsoft.plib.core.interfaces.ProcessorExecutor]
 * where the task will be performed
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * the result can be passed to the next task for processing see
 * [DELIVER_.TO_NEXT][com.starsoft.plib.core.triggers.DELIVER_.TO_NEXT]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 */
fun <T> ProcessorExecutor.executeAsProcessor(
    onResult: (T) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    callbackIn: CALLBACK_IN = CALLBACK_IN._MAIN_THREAD,
    lambda: () -> T
) {

    this.processing(object : Processor<Unit, T> {
        override fun processing(dataForProcessing: Unit): T {
            return lambda.invoke()
        }
    }, Unit, onResult, onError, callbackIn)

}
/**
 * Calls the specified function [lambda]
 * as its receiver and returns its result as callback
 * call is made on [ProcessorExecutor]
 * @receiver a [ProcessorExecutor][com.starsoft.plib.core.interfaces.ProcessorExecutor]
 * where the task will be performed
 * @param data data that will be passed to lambda as an input parameter
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * the result can be passed to the next task for processing see
 * [DELIVER_.TO_NEXT][com.starsoft.plib.core.triggers.DELIVER_.TO_NEXT]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 */
fun <V, T> ProcessorExecutor.executeAsProcessorWithData(
    data: V?,
    onResult: (T) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    callbackIn: CALLBACK_IN = CALLBACK_IN._MAIN_THREAD,
    lambda: (V) -> T
) {

    this.processing(object : Processor<V, T> {
        override fun processing(dataForProcessing: V): T {
            return lambda.invoke(dataForProcessing)
        }
    }, data, onResult, onError, callbackIn)

}

/**
 * Calls the specified function [lambda] with `this` value
 * as its receiver and returns its result as callback
 * @param executor an [executor][com.starsoft.plib.core.interfaces.ProcessorExecutor]
 * where the task will be performed
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * the result can be passed to the next task for processing see
 * [DELIVER_.TO_NEXT][com.starsoft.plib.core.triggers.DELIVER_.TO_NEXT]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 */
fun <T, R> T.execute(
    executor: ProcessorExecutor,
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    callbackIn: CALLBACK_IN = CALLBACK_IN._MAIN_THREAD,
    lambda: T.() -> R
) {

    executor.processing(object : Processor<Unit, R> {
        override fun processing(dataForProcessing: Unit): R {
            return this@execute.lambda()
        }
    }, Unit, onResult, onError, callbackIn)
}
/**
 * Calls the specified function [lambda] with `this` value
 * as its receiver and returns its result as callback
 * @param executor an [executor][com.starsoft.plib.core.interfaces.ProcessorExecutor]
 * where the task will be performed
 * @param data data that will be passed to lambda as an input parameter
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * the result can be passed to the next task for processing see
 * [DELIVER_.TO_NEXT][com.starsoft.plib.core.triggers.DELIVER_.TO_NEXT]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 */

fun <T, V, R> T.executeWithData(
    executor: ProcessorExecutor,
    data: V?,
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    callbackIn: CALLBACK_IN = CALLBACK_IN._MAIN_THREAD,
    lambda: T.(V) -> R
) {

    executor.processing(object : Processor<V, R> {
        override fun processing(dataForProcessing: V): R {
            return this@executeWithData.lambda(dataForProcessing)
        }
    }, data, onResult, onError, callbackIn)
}
