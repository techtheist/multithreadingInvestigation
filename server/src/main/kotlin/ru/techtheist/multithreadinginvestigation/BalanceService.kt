package ru.techtheist.multithreadinginvestigation

import java.util.*

interface BalanceService {
    /**
     *  Get current balance of bank account
     *
     *  @param id of bank account
     *  @return amount on bank account
     */
    fun getBalance(id: Long): Optional<Long>

    /**
     *  change balance on bank account
     *
     *  @param id of bank account
     *  @param value amount to add in bank account
     */
    fun changeBalance(id: Long, amount: Long)
}