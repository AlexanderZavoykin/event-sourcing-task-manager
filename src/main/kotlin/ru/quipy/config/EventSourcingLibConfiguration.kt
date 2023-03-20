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
import ru.quipy.projection.subscriber.ProjectAggregateEventSubscriber
import ru.quipy.projection.subscriber.TaskAggregateEventSubscriber
import ru.quipy.projection.subscriber.UserAggregateEventSubscriber
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
    private lateinit var projectAggregateEventSubscriber: ProjectAggregateEventSubscriber

    @Autowired
    private lateinit var userAggregateEventSubscriber: UserAggregateEventSubscriber

    @Autowired
    private lateinit var taskAggregateEventSubscriber: TaskAggregateEventSubscriber

    @PostConstruct
    fun setup() {
        subscriptionsManager.subscribe<ProjectAggregate>(projectAggregateEventSubscriber)
        subscriptionsManager.subscribe<UserAggregate>(userAggregateEventSubscriber)
        subscriptionsManager.subscribe<TaskAggregate>(taskAggregateEventSubscriber)
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