package com.arif.kotlincoroutinesplusflow.network.response

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ErrorResponse(val cod: Int, val message: String)