package ru.quipy.config

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
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

    @Bean
    fun bcryptHasher(): BCrypt.Hasher = BCrypt.withDefaults()

}