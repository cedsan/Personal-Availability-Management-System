package isel.leic.ps.Controllers

import com.fasterxml.jackson.databind.ObjectMapper
import isel.leic.ps.Controllers.ResponseDto.GetAllObservablesDto
import isel.leic.ps.DAL.Interfaces.IObservableDAL
import isel.leic.ps.DTO.ObservableDTO
import isel.leic.ps.SgdlApplication
import org.apache.http.HttpStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ImportResource
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(SgdlApplication::class))
@SpringBootTest
@Transactional
@ImportResource("applicationContext.xml")
@JsonTest
class ObservableControllerTest {

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Autowired
    private val observableDAL: IObservableDAL? = null

    @Test
    public fun getAllObservablesShouldReturn() {
        // Prepare
        // Insert two Observables in to the database
        observableDAL!!.createObservable(ObservableDTO("Name1", 900, "email1", "avatar_url1"))
        observableDAL.createObservable(ObservableDTO("Name2", 901, "email2", "avatar_url2"))

        // Act
        // MAke the request
        // Parse the response in order to access the array containing the Observables
        var response = restTemplate!!.getForEntity("/observable", String::class.java)
        var getObservablesDto = ObjectMapper().readValue<GetAllObservablesDto>(response.body, GetAllObservablesDto::class.java)

        // Test
        // Assert we got OK as the response status Code &
        // Assert that number is two and eventually
        assertEquals(2, getObservablesDto.properties.observableList.size)
        assertEquals(org.springframework.http.HttpStatus.OK, response.statusCode)
    }
}