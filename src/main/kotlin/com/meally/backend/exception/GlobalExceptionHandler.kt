package com.meally.backend.exception

import com.meally.backend.exception.model.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(): ResponseEntity<String> =
        ResponseEntity(
            "Resource not found",
            HttpStatus.NOT_FOUND
        )

//    @ExceptionHandler(Throwable::class)
//    fun handleException(): ResponseEntity<String> =
//        ResponseEntity(
//            "Something wrong happened",
//            HttpStatus.INTERNAL_SERVER_ERROR
//        )

}