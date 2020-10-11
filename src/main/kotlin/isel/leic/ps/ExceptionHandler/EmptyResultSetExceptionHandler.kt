package isel.leic.ps.ExceptionHandler

import isel.leic.ps.Exception.UserAlreadyExistsException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver


/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the handler for emptyResultSet
 */
@ControllerAdvice
class EmptyResultSetExceptionHandler : ExceptionHandlerExceptionResolver() {

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleNotFoundEntity(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}