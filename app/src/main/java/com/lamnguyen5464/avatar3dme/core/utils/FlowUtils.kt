package com.lamnguyen5464.avatar3dme.core.utils

import com.lamnguyen5464.avatar3dme.core.providers.Providers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun <T> Flow<T>.debounce(timeoutMillis: Long): Flow<T> {
    val sharedFlow = MutableSharedFlow<T>()

    // Launch a coroutine to collect the original flow and emit values to the shared flow
    Providers.commonIOScope.launch {
        var job: Job? = null

        collect { value ->
            job?.cancel() // Cancel the previous debounce job if it exists

            job = launch {
                delay(timeoutMillis)
                sharedFlow.emit(value) // Emit the value after the specified timeout
            }
        }
    }

    // Use the debounce operator on the shared flow
    return sharedFlow.debounce(timeoutMillis)
}