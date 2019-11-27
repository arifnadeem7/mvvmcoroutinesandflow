package com.arif.kotlincoroutinesplusflow.extensions

import com.arif.kotlincoroutinesplusflow.base.Progress
import com.arif.kotlincoroutinesplusflow.base.Result
import com.arif.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.io.IOException

/**
 * A String class extension function which will captitalize
 * all first characters of all words in a sentence.
 */
fun String.capitalizeWords(): String = this.split(' ').joinToString(" ") { it.capitalize() }

@PublishedApi
internal inline fun Retrofit.Builder.callFactory(crossinline body: (Request) -> Call) =
    callFactory(object : Call.Factory {
        override fun newCall(request: Request): Call = body(request)
    })

@Suppress("NOTHING_TO_INLINE")
inline fun Retrofit.Builder.delegatingCallFactory(delegate: dagger.Lazy<OkHttpClient>): Retrofit.Builder =
    callFactory {
        delegate.get().newCall(it)
    }

/**
 * You may want to apply some common side-effects to your flow to avoid repeating commonly used
 * logic across your app.
 *
 * For e.g. If you want to show/hide progress then use side-effect methods like
 * onStart & onCompletion
 *
 * You can also write common business logic which is applicable to all flows in your application,
 * in this case we are retrying requests 3 times with an exponential delay; if the exception thrown
 * is of type IOException.
 *
 */
fun <T : Any> Flow<Result<T>>.applyCommonSideEffects() =
        retryWhen { cause, attempt ->
            when {
                (cause is IOException && attempt < Utils.MAX_RETRIES) -> {
                    delay(Utils.getBackoffDelay(attempt))
                    true
                }
                else -> {
                    false
                }
            }
        }
        .onStart { emit(Progress(isLoading = true)) }
        .onCompletion { emit(Progress(isLoading = false)) }

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}
