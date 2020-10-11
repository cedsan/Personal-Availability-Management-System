package isel.leic.ps.ExceptionHandler

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.google.code.siren4j.component.Entity
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
 * Specifies the handler for requestes that are malformed
 */
@ControllerAdvice
class MalformedRequestExceptionHandler : ExceptionHandlerExceptionResolver(){
    @ExceptionHandler(UnrecognizedPropertyException::class)
    fun handleNotFoundEntity(): ResponseEntity<Entity> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
    }
}