package isel.leic.ps.Controllers

import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The only purpose of this controller is to have
 * a method only for testing some functionality
 */
@RestController
@RequestMapping(value = "/")
class TestController {

    @RequestMapping(value= "")
    fun test(): HttpEntity<String>{
        //println(System.getenv("POSTGRES_USR"))
        return ResponseEntity.ok().body(System.getenv("POSTGRES_USR"))
    }
}
