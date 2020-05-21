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

package com.starsoft.plib.core.func

import com.starsoft.plib.core.auxiliary.stub
import com.starsoft.plib.core.auxiliary.stubErrorCallback
import com.starsoft.plib.handlers.MainHandler


//This File Created at 17.05.2020 10:34.

/**
 * Calls the specified function [lambda] with `this` value
 * as its receiver and returns its result as callback
 * each call is made on a new thread
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 * @param _isDaemon determines whether the thread is a daemon, true by default
 * @return a reference to the thread in which the work
 * is performed can be used for example for interrupting
 */
fun <T, R> T.runOnThread(
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    _isDaemon: Boolean = true,
    lambda: T.() -> R
): Thread {
    return Thread {
        try {
            val result = this.lambda()
            MainHandler.instance.post { onResult(result) }
        } catch (e: Exception) {
            MainHandler.instance.post { onError(e) }
        } catch (e: Error) {
            MainHandler.instance.post { onError(Exception("Error - $e")) }
        }
    }.apply { isDaemon =_isDaemon
        start() }
}

/**
 * Calls the specified function [lambda] with the given [receiver] as its receiver
 * and returns its result as callback
 * each call is made on a new thread
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 *  @param _isDaemon determines whether the thread is a daemon, true by default
 * @return a reference to the thread in which the work
 * is performed can be used for example for interrupting
 */
fun <T, R> runOnThreadWitch(
    receiver: T,
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    _isDaemon: Boolean = true,
    lambda: T.() -> R
): Thread {
    return Thread {
        try {
            val result = receiver.lambda()
            MainHandler.instance.post { onResult(result) }
        } catch (e: Exception) {
            MainHandler.instance.post { onError(e) }
        } catch (e: Error) {
            MainHandler.instance.post { onError(Exception("Error - $e")) }
        }
    }.apply { isDaemon =_isDaemon
        start() }
}

/**
 * Calls the specified function [lambda]
 * with `this` value as its argument and returns its result as callback
 * each call is made on a new thread
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 * @param _isDaemon determines whether the thread is a daemon, true by default
 * @return a reference to the thread in which the work
 * is performed can be used for example for interrupting
 */

fun <T, R> T.processingOnThread(
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    _isDaemon: Boolean = true,
    lambda: (T) -> R
): Thread {
    return Thread {
        try {
            val result = lambda(this)
            MainHandler.instance.post { onResult(result) }
        } catch (e: Exception) {
            MainHandler.instance.post { onError(e) }
        } catch (e: Error) {
            MainHandler.instance.post { onError(Exception("Error - $e")) }
        }
    }.apply { isDaemon =_isDaemon
        start() }
}

/**
 * Calls the specified function [lambda] with the given [data] as as its argument
 * and returns its result as callback
 * each call is made on a new thread
 * @param data data for handling
 * @param onResult the code that return the result,
 * if this code is missing will be run [stub][stub]
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 *
 * [onError] and [onResult] are called in the main thread
 * @param _isDaemon determines whether the thread is a daemon, true by default
 * @return a reference to the thread in which the work
 * is performed can be used for example for interrupting
 */
fun <T, R> handleOnThread(
    data: T,
    onResult: (R) -> Unit = ::stub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    _isDaemon: Boolean = true,
    lambda: (T) -> R
): Thread {
    return Thread {
        try {
            val result = lambda(data)
            MainHandler.instance.post { onResult(result) }
        } catch (e: Exception) {
            MainHandler.instance.post { onError(e) }
        } catch (e: Error) {
            MainHandler.instance.post { onError(Exception("Error - $e")) }
        }
    }.apply { isDaemon =_isDaemon
        start() }
}

/**
 * Calls the specified function [lambda]
 * with `this` value as its argument on a new thread
 * @param onResult Calls  on main thread with `this` value as its argument after
 * function [lambda] finished
 * if this code is missing will be run stub
 * @param onError the code that  handle the Exception,
 * if this code is missing will be run [stubErrorCallback][stubErrorCallback]
 * [onError] and [onResult] are called in the main thread
 * @param _isDaemon determines whether the thread is a daemon, true by default
 * @return a reference to the thread in which the work
 * is performed can be used for example for interrupting
 */
fun <T> T.prepareOnThreadAndRun(
    onResult: T.() -> Unit = ::rStub,
    onError: (Exception) -> Unit = ::stubErrorCallback,
    _isDaemon: Boolean = true,
    lambda: T.() -> Unit
): Thread {
    return Thread {
        try {
            this.lambda()
            MainHandler.instance.post { this.onResult() }
        } catch (e: Exception) {
            MainHandler.instance.post { onError(e) }
        } catch (e: Error) {
            MainHandler.instance.post { onError(Exception("Error - $e")) }
        }
    }.apply { isDaemon =_isDaemon
        start() }
}

/**@suppress*/
fun <T> T.rStub(t: T) {

}


