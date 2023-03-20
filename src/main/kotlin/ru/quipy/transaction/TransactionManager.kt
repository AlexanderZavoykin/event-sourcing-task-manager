package ru.quipy.transaction

interface TransactionManager {

    fun <T> transaction(block: () -> T): T

}