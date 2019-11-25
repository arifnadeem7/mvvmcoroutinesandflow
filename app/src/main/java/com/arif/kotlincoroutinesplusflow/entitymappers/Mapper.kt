package com.arif.kotlincoroutinesplusflow.entitymappers

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Mapper<T> {

    @WorkerThread
    suspend fun map(): T {
        return withContext(Dispatchers.Default) {
            getMapping()
        }
    }

    @WorkerThread
    fun getMapping(): T
}