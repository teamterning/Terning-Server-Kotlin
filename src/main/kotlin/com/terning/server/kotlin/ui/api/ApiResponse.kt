package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val result: T? = null
) {
    companion object {
        fun <T> success(status: HttpStatus, message: String, result: T): ApiResponse<T> =
            ApiResponse(status.value(), message, result)

        fun error(status: HttpStatus, message: String): ApiResponse<Unit> =
            ApiResponse(status.value(), message, null)
    }
}
