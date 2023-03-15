package ru.quipy.config

import at.favre.lib.crypto.bcrypt.BCrypt
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
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
        val flywayConfig = with(properties) {
            val config = ClassicConfiguration()
            config.setDataSource(url, username, password)
            return@with config
        }

        Flyway(flywayConfig).migrate()

        with(properties) {
            Database.connect(url = url, driver = driver, user = username, password = password)
        }
    }

    @Bean
    fun bcryptHasher(): BCrypt.Hasher = BCrypt.withDefaults()

}