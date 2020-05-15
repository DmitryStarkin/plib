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


//This File Created at 15.05.2020 15:51.

fun<T> ProcessorExecutor.executeAsProcessor(onResult: (T) -> Unit= ::stub, onError: (Exception) -> Unit = ::stubErrorCallback, lambda: () -> T){

    this.processing(object : Processor<Unit, T> {
        override fun processing(dataForProcessing: Unit): T {
            return lambda.invoke()
        }
    }, Unit,onResult,onError)

}

fun<V, T> ProcessorExecutor.executeAsProcessor(data: V?, onResult: (T) -> Unit = ::stub, onError: (Exception) -> Unit = ::stubErrorCallback, lambda: (V) -> T){

    this.processing(object : Processor<V, T> {
        override fun processing(dataForProcessing: V): T {
            return lambda.invoke(dataForProcessing)
        }
    }, data, onResult, onError)

}

fun<T, R> T.executeOnExecutor(executor: ProcessorExecutor, onResult: (R) -> Unit = ::stub, onError: (Exception) -> Unit = ::stubErrorCallback, lambda: T.() -> R){

    executor.processing(object : Processor<Unit, R> {
        override fun processing(dataForProcessing: Unit): R {
            return lambda.invoke(this@executeOnExecutor)
        }}, Unit,onResult,onError)


}