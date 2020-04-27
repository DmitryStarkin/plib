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

package com.starsoft.plib.core.commands

import android.util.Log
import com.starsoft.plib.core.interfaces.Processor

//This File Created at 06.04.2020 10:46.

/**
 * Contains classes used as commands for the
 * [ProcessorExecutor][com.starsoft.plib.core.ProcessorExecutor]
 * @constructor usually not required
 * @since 1.0
 */

class ExecutorCommands {

    /**
     * When passing an instance of this class as parameter "processor" to
     * [processing][com.starsoft.plib.core.ProcessorExecutor.processing] in
     * implementation of [ProcessorExecutor][com.starsoft.plib.core.ProcessorExecutor]
     * all tasks are removed from the task queue
     * @constructor usually not required
     * @since 1.0
     */
    class DeleteAllNotProcessingTasks :
        Processor<Unit, Unit> {
        private val TAG = this::class.java.simpleName
        /**@suppress*/
        override fun processing(dataForProcessing: Unit) {
            Log.d(TAG, "DeleteAllNotProcessingTasks command received")
        }
    }

    /**
     * When passing an instance of this class as parameter "processor" to
     * [processing][com.starsoft.plib.core.ProcessorExecutor.processing] in
     * implementation of [ProcessorExecutor][com.starsoft.plib.core.ProcessorExecutor]
     * the executor stops working
     * Attempts to stop all actively executing tasks, halts the processing of waiting tasks,
     * and returns a list of the tasks that were awaiting execution in the onProcessedCallback call if it is presented.
     * These tasks are drained (removed) from the task queue.
     * the instance of [ProcessorExecutor][com.starsoft.plib.core.ProcessorExecutor]
     * will be stopped and cannot be used in the future
     * @constructor usually not required
     * @since 1.0
     */
    class ShutdownNow :
        Processor<MutableList<Runnable>?, MutableList<Runnable>?> {
        private val TAG = this::class.java.simpleName

        /**@suppress*/
        override fun processing(dataForProcessing: MutableList<Runnable>?): MutableList<Runnable>? {
            Log.d(TAG, "ShutdownNow command received")
            return dataForProcessing
        }
    }
}