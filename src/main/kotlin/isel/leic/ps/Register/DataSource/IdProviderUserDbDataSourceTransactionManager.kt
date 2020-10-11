package isel.leic.ps.Register.DataSource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource


class IdProviderUserDbDataSourceTransactionManager {
    var dataSourceTransManager : DataSourceTransactionManager

    constructor(dataSource: DataSource) {
        dataSourceTransManager = DataSourceTransactionManager(dataSource)
    }
}
