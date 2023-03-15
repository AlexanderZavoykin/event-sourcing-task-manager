package ru.quipy.transaction

import org.springframework.stereotype.Service

@Service
class ExposedTransactionManager : TransactionManager {

    override fun <T> transaction(block: () -> T): T =
        org.jetbrains.exposed.sql.transactions.transaction { block.invoke() }

}