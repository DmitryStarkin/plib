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

//This File Created at 05.04.2020 10:31.

/**
 * Processing the input data
 * @since 1.0
 */
interface Processor<T,V>{
    /**
     * Processing the input data
     * @param dataForProcessing data for processing  data interpretation depends on the implementation
     * @return processing result data interpretation depends on the implementation
     */
    fun processing(dataForProcessing: T): V

}