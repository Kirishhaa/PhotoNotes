package com.kirishhaa.photonotes.extensions

import kotlinx.coroutines.CancellationException

suspend fun coroutineTryCatcher(tryBlock: suspend () -> Unit, catchBlock: suspend (Exception) -> Unit) {
    try {
        tryBlock()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        catchBlock(e)
    }
}