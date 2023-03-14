package ru.quipy.config

import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class ProjectionConfig {

    @Autowired
    private lateinit var properties: ProjectionDatabaseProperties

    @PostConstruct
    fun setup() {
        with (properties) {
            Database.connect(url = url, driver = driver, user = username, password = password)
        }
    }

}