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

package com.starsoft.plib.executors.workers

import com.starsoft.plib.runables.ProcessingRunnable
import java.lang.ref.WeakReference
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

//This File Created at 05.04.2020 14:37.
/**@suppress
 * @since 1.0
 * */
class SingleThreadPoolExecutor<V> :
    ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>()),
        (V) -> Unit {

    var listener: TaskWithTrueFlagEndListener? = null
    var previusTaskResult: WeakReference<Any?>? = null

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        super.afterExecute(r, t)
        if (r is ProcessingRunnable<*, *> && r.booleanFlag) {
            listener?.onTaskWithTrueFlagEndCallback()
        }
    }

    override fun beforeExecute(t: Thread?, r: Runnable?) {
        super.beforeExecute(t, r)
        if (r is ProcessingRunnable<*, *> && previusTaskResult != null) {
            r.setData(previusTaskResult?.get())
            previusTaskResult = null
        }
    }

    override fun terminated() {
        super.terminated()
    }

    interface TaskWithTrueFlagEndListener {
        fun onTaskWithTrueFlagEndCallback()
    }

    override fun invoke(p1: V) {

        previusTaskResult = WeakReference(p1)
    }

}