package ru.quipy.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.TaskAggregate
import ru.quipy.api.UserAggregate
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.TaskAggregateState
import ru.quipy.logic.UserAggregateState
import ru.quipy.projection.ProjectEventSubscriber
import ru.quipy.projection.UserEventSubscriber
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Configuration
class EventSourcingLibConfiguration {

    private val logger = LoggerFactory.getLogger(EventSourcingLibConfiguration::class.java)

    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Autowired
    private lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @Autowired
    private lateinit var projectEventSubscriber: ProjectEventSubscriber

    @Autowired
    private lateinit var userEventSubscriber: UserEventSubscriber

    @PostConstruct
    fun setup() {
        subscriptionsManager.subscribe<ProjectAggregate>(projectEventSubscriber)
        subscriptionsManager.subscribe<UserAggregate>(userEventSubscriber)
    }

    @Bean
    fun projectEsService(): EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState> =
        eventSourcingServiceFactory.create<UUID, ProjectAggregate, ProjectAggregateState>()

    @Bean
    fun taskEsService(): EventSourcingService<UUID, TaskAggregate, TaskAggregateState> =
        eventSourcingServiceFactory.create<UUID, TaskAggregate, TaskAggregateState>()

    @Bean
    fun userEsService(): EventSourcingService<UUID, UserAggregate, UserAggregateState> =
        eventSourcingServiceFactory.create<UUID, UserAggregate, UserAggregateState>()

}