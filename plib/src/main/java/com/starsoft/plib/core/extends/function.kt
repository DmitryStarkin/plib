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
import com.starsoft.plib.executors.SequentiallyProcessorExecutor



//This File Created at 15.05.2020 15:51.

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

fun <T, R> T.runOnThread(
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    lambda: T.() -> R
) {

    SequentiallyExecutor.processing(object : Processor<Unit, R> {
        override fun processing(dataForProcessing: Unit): R {
            return this@runOnThread.lambda()
        }
    }, Unit, onResult, onError)
}

fun <T, V, R> T.runOnThreadWithData(
    data: V?,
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    lambda: T.(V) -> R
) {

    SequentiallyExecutor.processing(object : Processor<V, R> {
        override fun processing(dataForProcessing: V): R {
            return this@runOnThreadWithData.lambda(dataForProcessing)
        }
    }, data, onResult, onError)
}


private object SequentiallyExecutor : ProcessorExecutor by SequentiallyProcessorExecutor() {}