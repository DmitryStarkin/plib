package com.starsoft.plib.core.auxiliary


//This File Created at 26.04.2020 11:15.

/**
 * This is stub for Callbacks
 * in case you don't need to do anything
 *  @since 1.0
 */
fun <T> stub(par: T) {

}

/**
 * This is stub for ErrorCallback
 * in case you don't need to to handle Exception
 * the exception will simply be throw
 * @since 1.0
 */
fun stubErrorCallback(par: Exception) {
    throw par
}