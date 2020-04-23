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

package com.starsoft.plib.interfaces.util

//This File Created at 08.04.2020 19:03.

/**
 * Defines the strategy of behavior in case the task which
 * takes the data from the previous task has a nonnull data
 * @since 1.0
 */

 enum  class NOT_NULL_DATA_STRATEGY {
    /**
     * Accepts current data
     */
    ALLOW,

    /**
     * Rejects data and throws an exception (default behavior)
     */
    REJECT,

    /**
     * Replaces data with data received from the previous task
     */
    CHANGE,

    /**
     * Accepts the current data if the previous task failed
     * with an error otherwise replaces the data
     */
    ALLOW_IF_ERROR
}