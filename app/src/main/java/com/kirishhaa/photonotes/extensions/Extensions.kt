package com.kirishhaa.photonotes.extensions

import kotlinx.coroutines.CancellationException

suspend fun <T> coroutineTryCatcher(
    tryBlock: suspend () -> T,
    catchBlock: suspend (Exception) -> T
): T {
    return try {
        tryBlock()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        catchBlock(e)
    }
}