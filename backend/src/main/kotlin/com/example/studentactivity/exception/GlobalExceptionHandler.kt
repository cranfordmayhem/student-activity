package com.example.studentactivity.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    object ErrorUtil {

        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

        fun errorMapper(status: Int, error: Any, request: HttpServletRequest): Map<String, Any> {
            return mapOf(
                "timestamp" to LocalDateTime.now(),
                "status" to status,
                "error" to error,
                "path" to request.requestURI
            ).also { logger.error("ERROR: $error") }
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(handler: DataIntegrityViolationException, request: HttpServletRequest):
            Map<String, Any>{

        val error = handler.message.toString()

        return ErrorUtil.errorMapper(HttpStatus.BAD_REQUEST.value(), error, request)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        handler: MethodArgumentNotValidException,
        request: HttpServletRequest)
    : Map<String, Any> {
        val errors = handler.bindingResult.fieldErrors
            .associate { it.field to (it.defaultMessage ?: "Invalid" ) }

        return ErrorUtil.errorMapper(HttpStatus.BAD_REQUEST.value(), errors, request)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidToken(handler: InvalidTokenException, request: HttpServletRequest):
            Map<String, Any> {
        val error = handler.message.toString()

        return ErrorUtil.errorMapper(HttpStatus.UNAUTHORIZED.value(), error, request)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(handler: UserNotFoundException, request: HttpServletRequest):
            Map<String, Any> {
        val error = handler.message.toString()

        return ErrorUtil.errorMapper(HttpStatus.NOT_FOUND.value(), error, request)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthUserException::class)
    fun handleAuthUser(handler: AuthUserException, request: HttpServletRequest):
            Map<String, Any> {
        val error = handler.message.toString()

        return ErrorUtil.errorMapper(HttpStatus.UNAUTHORIZED.value(), error, request)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IdNotFoundException::class)
    fun handleIdNotFound(handler: IdNotFoundException, request: HttpServletRequest):
            Map<String, Any> {
        val error = handler.message.toString()

        return ErrorUtil.errorMapper(HttpStatus.NOT_FOUND.value(), error, request)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(handler: UnauthorizedException, request: HttpServletRequest):
            Map<String, Any> {
        val error = handler.message.toString()

        return ErrorUtil.errorMapper(HttpStatus.UNAUTHORIZED.value(), error, request)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException::class)
    fun handleUnauthenticated(handler: UnauthenticatedException, request: HttpServletRequest):
            Map<String, Any> {
        val error = handler.message.toString()

        return ErrorUtil.errorMapper(HttpStatus.UNAUTHORIZED.value(), error, request)
    }

}

class InvalidTokenException(message: String): RuntimeException(message)
class UserNotFoundException(email: String): RuntimeException("User $email not found")
class AuthUserException(): RuntimeException("Username or Password is invalid")
class IdNotFoundException(id: Any, entity: String): RuntimeException("$entity with $id not found")
class UnauthorizedException(entity: String, action: String): RuntimeException("Not authorized to $action $entity")
class UnauthenticatedException(): RuntimeException("User not authenticated")