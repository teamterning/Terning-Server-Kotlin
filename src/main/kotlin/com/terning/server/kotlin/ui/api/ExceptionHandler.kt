package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.terning.server.kotlin.domain.auth.exception.AuthException
import com.terning.server.kotlin.domain.common.BaseException
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val message =
            when (val exception = ex.cause) {
                is MismatchedInputException -> "${exception.path.lastOrNull()?.fieldName ?: "UnknownField"}: 널이어서는 안됩니다"
                is InvalidFormatException -> "${exception.path.lastOrNull()?.fieldName ?: "UnknownField"}: 올바른 형식이어야 합니다"
                else -> exception?.message.orEmpty()
            }

        logger.error(
            "Handling ${ex::class.simpleName} with status ${HttpStatus.BAD_REQUEST}: $message",
            ex,
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(HttpStatus.BAD_REQUEST, message))
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val message = ex.messages()

        logger.error(
            "Handling ${ex::class.simpleName} with status ${HttpStatus.BAD_REQUEST}: $message",
            ex,
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(HttpStatus.BAD_REQUEST, message))
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        logger.error(
            "Handling ${exception::class.simpleName} with status ${HttpStatus.BAD_REQUEST}: ${exception.message}",
            exception,
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(HttpStatus.BAD_REQUEST, exception.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFoundException(exception: EntityNotFoundException): ResponseEntity<ApiResponse<Unit>> {
        logger.error(
            "Handling ${exception::class.simpleName} with status ${HttpStatus.NOT_FOUND}: ${exception.message}",
            exception,
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(HttpStatus.NOT_FOUND, exception.message ?: "데이터를 찾을 수 없습니다."))
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error(
            "Handling ${exception::class.simpleName} with status ${HttpStatus.INTERNAL_SERVER_ERROR}: ${exception.message}",
            exception,
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."))
    }

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(exception: BaseException): ResponseEntity<ApiResponse<Unit>> {
        logger.error(
            "Handling ${exception::class.simpleName} with status ${exception.errorCode.status}: ${exception.errorCode.message}",
            exception,
        )
        return ResponseEntity
            .status(exception.errorCode.status)
            .body(ApiResponse.error(exception.errorCode.status, exception.errorCode.message))
    }

    @ExceptionHandler(AuthException::class)
    fun handleAuthException(exception: AuthException): ResponseEntity<ApiResponse<Unit>> {
        logger.error(
            "Handling ${exception::class.simpleName} with status ${exception.errorCode.status}: ${exception.errorCode.message}",
            exception,
        )
        return ResponseEntity
            .status(exception.errorCode.status)
            .body(ApiResponse.error(exception.errorCode.status, exception.errorCode.message))
    }

    private fun MethodArgumentNotValidException.messages(): String =
        bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage.orEmpty()}"
        }
}
