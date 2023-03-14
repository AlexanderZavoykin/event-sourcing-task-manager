package ru.quipy.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("projection.database")
data class ProjectionDatabaseProperties(
    val url: String,
    val driver: String,
    val username: String,
    val password: String,
)