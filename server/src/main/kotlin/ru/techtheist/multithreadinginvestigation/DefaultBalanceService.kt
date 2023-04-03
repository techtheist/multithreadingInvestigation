package ru.techtheist.multithreadinginvestigation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock

@Service
class DefaultBalanceService(private val accountService: AccountService) : BalanceService {
    private val readWriteLock = ReentrantReadWriteLock()

    @Cacheable(cacheNames = ["balance"], key = "#id")
    override fun getBalance(id: Long): Optional<Long> {
        readWriteLock.readLock().lock()
        try {
            val account = accountService.getAccount(id)
            return Optional.ofNullable(account.balance)
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["balance"], key = "#id"),
            CacheEvict(cacheNames = ["account"], key = "#id")
        ]
    )
    override fun changeBalance(id: Long, amount: Long) {
        runBlocking {
            withContext(Dispatchers.IO) {
                readWriteLock.writeLock().lock()
                accountService.changeBalance(id, amount)
                readWriteLock.writeLock().unlock()
            }
        }
    }
}