package ru.techtheist.multithreadinginvestigation

import jakarta.transaction.Transactional
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) {
    fun getAccount(id: Long): Account = accountRepository.findById(id).orElseThrow()
    @Transactional
    fun changeBalance(id: Long, amount: Long) {
        var retries = 3
        while (true) {
            try {
                val account = accountRepository.findByIdForUpdate(id).orElseThrow()
                account.balance += amount
                accountRepository.save(account)
                break
            } catch (ex: OptimisticLockingFailureException) {
                if (--retries == 0) throw ex
            }
        }
    }
}