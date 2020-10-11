package isel.leic.ps.ExceptionHandler

import com.google.code.siren4j.component.Entity
import isel.leic.ps.Exception.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver


@ControllerAdvice
class DefaultExceptionHandler : ExceptionHandlerExceptionResolver() {

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFoundEntity(): ResponseEntity<Entity> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}