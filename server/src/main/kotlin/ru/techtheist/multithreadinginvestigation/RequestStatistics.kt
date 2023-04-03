package ru.techtheist.multithreadinginvestigation

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

data class RequestStatistics(
    private val totalRequests: AtomicInteger = AtomicInteger(0),
    private val getRequests: AtomicInteger = AtomicInteger(0),
    private val setRequests: AtomicInteger = AtomicInteger(0),
    private val head: AtomicInteger = AtomicInteger(0)
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)

        val reqStats = RequestStatistics()
    }
    fun addRequest(isGetRequest: Boolean) {
        totalRequests.incrementAndGet()
        if (isGetRequest) getRequests.incrementAndGet()
        else setRequests.getAndIncrement()
    }

    fun runStatsLogging() {
        logger.info("Requests per minute: ${reqStats.totalRequests.get()-reqStats.head.get()}, " +
                "total: ${reqStats.totalRequests.get()}, get: ${reqStats.getRequests.get()}, set: ${reqStats.setRequests.get()}")
        reqStats.head.set(totalRequests.get())
    }

}