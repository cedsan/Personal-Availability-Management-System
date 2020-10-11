package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IUserDAL
import org.junit.Before
import org.junit.Test
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.util.*
import kotlin.test.assertFails


class UserDALTest {

    private lateinit var userDal: IUserDAL

    @Before
    fun instantiateUserDAL() {
        userDal = UserDBDAL(DataSourceTransactionManager(DriverManagerDataSource("jdbc:postgresql://localhost:5432/SGDL", System.getenv("POSTGRES_USR"), System.getenv("POSTGRES_PSW"))))
    }

    @Test
    fun getUserTypeWithObserverUserMailShouldReturnObserver() {
        var res = userDal.getUserType("pedrofelix@cc.isel.ipl.pt")
        assert(false)

    }


}