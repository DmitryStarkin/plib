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

package com.starsoft.plib.executors.global

import com.starsoft.plib.core.auxiliary.stub
import com.starsoft.plib.core.auxiliary.stubErrorCallback
import com.starsoft.plib.core.extends.execute
import com.starsoft.plib.core.extends.executeWithData
import com.starsoft.plib.core.interfaces.ProcessorExecutor
import com.starsoft.plib.core.triggers.CALLBACK_IN
import com.starsoft.plib.executors.SequentiallyProcessorExecutor


//This File Created at 17.05.2020 12:07.

/**
 * Singleton on the basis of [SequentiallyProcessorExecutor][com.starsoft.plib.executors.SequentiallyProcessorExecutor]
 */
object SequentiallyGlobalExecutor : ProcessorExecutor by SequentiallyProcessorExecutor(){

    /**
     * Calls the specified function [lambda] with `this` value
     * as its receiver and returns its result as callback
     * the call is made on executor witch single thread
     * this executor is global within the process
     * @param onResult the code that return the result,
     * if this code is missing will be run [stub][stub]
     * the result can be passed to the next task for processing see
     * [DELIVER_.TO_NEXT][com.starsoft.plib.core.triggers.DELIVER_.TO_NEXT]
     * @param onError the code that  handle the Exception,
     * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
     * [onError] and [onResult] are called in the main thread
     * @return  global  executor on which the execution was performed
     */
    fun <T, R> T.runOnExecutor(
        onResult: (R) -> Unit = ::stub,
        onError: (Exception) -> Unit = ::stubErrorCallback,
        lambda: T.() -> R
    ): ProcessorExecutor {
        this.execute(
            this@SequentiallyGlobalExecutor, onResult, onError,
            CALLBACK_IN._MAIN_THREAD, lambda
        )
        return this@SequentiallyGlobalExecutor
    }

    /**
     * Calls the specified function [lambda] with `this` value
     * as its receiver and returns its result as callback
     * the call is made on executor witch single thread
     * this executor is global within the process
     * @param data data that will be passed to lambda as an input parameter
     * @param onResult the code that return the result,
     * if this code is missing will be run [stub][stub]
     * the result can be passed to the next task for processing see
     * [DELIVER_.TO_NEXT][com.starsoft.plib.core.triggers.DELIVER_.TO_NEXT]
     * @param onError the code that  handle the Exception,
     * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
     * [onError] and [onResult] are called in the main thread
     * @return  global  executor on which the execution was performed
     */
    fun <T, V, R> T.runOnExecutorWithData(
        data: V?,
        onResult: (R) -> Unit = ::stub,
        onError: (Exception) -> Unit = ::stubErrorCallback,
        lambda: T.(V) -> R
    ): ProcessorExecutor {
        this.executeWithData(
            this@SequentiallyGlobalExecutor, data, onResult, onError,
            CALLBACK_IN._MAIN_THREAD, lambda
        )
        return this@SequentiallyGlobalExecutor
    }

    /**
     * Reset Global sequentially executor
     * @see com.starsoft.plib.core.interfaces.ProcessorExecutor.reset
     */
    fun resetGlobalSequentiallyExecutor(){
        this@SequentiallyGlobalExecutor.reset()
    }
}