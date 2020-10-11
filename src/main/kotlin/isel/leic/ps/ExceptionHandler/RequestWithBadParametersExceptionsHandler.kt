package isel.leic.ps.ExceptionHandler

import com.google.code.siren4j.component.Entity
import org.postgresql.util.PSQLException
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
 * Handler of exceptions thrown by the data layer operations.
 */
@ControllerAdvice
class RequestWithBadParametersExceptionsHandler: ExceptionHandlerExceptionResolver() {

    @ExceptionHandler(PSQLException::class)
    fun handleNotFoundEntity(): ResponseEntity<Entity> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
    }
}