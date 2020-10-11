package isel.leic.ps.Register.DAL

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the operations to perform to the data source.
 * of our Identity Provider in order to allow creation of users
 * through an externial service.
 */
class MitreIdUserSqlQueries {

    companion object {
        val INSERT_USER = "INSERT INTO users (username, password, enabled) " +
                "VALUES (?, ?, true);"

        val INSERT_USER_AUTHORITY = "INSERT INTO authorities (username, authority) " +
        "VALUES (?,'ROLE_USER');"

        val INSERT_USER_INFO = "INSERT INTO user_info (sub, preferred_username, name, email, email_verified) " +
        "VALUES ('12345.ISEL',?,?,?, true);"
    }
}