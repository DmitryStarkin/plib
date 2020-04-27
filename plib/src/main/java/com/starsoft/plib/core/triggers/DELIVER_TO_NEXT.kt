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

package com.starsoft.plib.core.triggers

//This File Created at 08.04.2020 12:24.
/**
 * When passed as a "onProcessedCallback" in [processing][com.starsoft.plib.executors.SequentiallyProcessorExecutor.processing]
 * initiates the transfer of the task result to the next task
 * @since 1.0
 */
enum class DELIVER_ : (Any?)->Unit{
 /**
  * When passed as a "onProcessedCallback" in [processing][com.starsoft.plib.executors.SequentiallyProcessorExecutor.processing]
  * initiates the transfer of the task result to the next task
  */
 TO_NEXT {
  /**@suppress*/
  override fun invoke(p1: Any?) {
   throw Exception("DELIVER_TO_NEXT only for use witch SequentiallyProcessorExecutorFromMain")
  }
 }
}