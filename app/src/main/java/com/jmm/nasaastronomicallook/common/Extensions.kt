package com.jmm.nasaastronomicallook.common

import kotlinx.coroutines.*

fun <T> async(function: () -> T): Deferred<T> {
    return CoroutineScope(Dispatchers.IO).async { function() }
}

fun launch(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Main).launch { block() }
}

fun launchIO(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.IO).launch { block() }
}

suspend fun <T> asyncSeq(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO) { block() }
}