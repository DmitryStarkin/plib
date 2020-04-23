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

//This File Created at 08.04.2020 12:03.
/**
 * Constants that determine which thread the result will be returned in
 * @since 1.0
 */
 enum class CALLBACK_IN {
    /**
     * The code processing the result will be
     * called in the main thread
     */
    _MAIN_THREAD ,

    /**
     * The code processing the result will
     * be called in the thread from which the request was made
     */
    _CALLING_THREAD
}