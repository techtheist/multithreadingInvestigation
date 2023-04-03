package ru.techtheist.multithreadinginvestigation

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*

@RestController
@EnableScheduling
@RequestMapping("/api/balance")
class BalanceController(private val balanceService: DefaultBalanceService) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = getLogger(javaClass.enclosingClass)
    }

    @GetMapping("/{id}")
    fun getBalance(@PathVariable("id") id: Long): ResponseEntity<Long> {
        val balance = balanceService.getBalance(id).orElse(0)
        logger.debug("request for balance, id: $id, balance: $balance")
        RequestStatistics.reqStats.addRequest(true)
        return ResponseEntity.ok(balance)
    }

    @PostMapping("/{id}")
    fun changeBalance(@PathVariable("id") id: Long, @RequestBody request: ChangeBalanceRequest): ResponseEntity<Void> {
        logger.debug("request for change, id: $id, amount: ${request.amount}")
        RequestStatistics.reqStats.addRequest(false)
        balanceService.changeBalance(id, request.amount)
        return ResponseEntity.ok().build()
    }

    data class ChangeBalanceRequest(val amount: Long)

    @Scheduled(fixedDelay = 60000L)
    fun runStats() {
        RequestStatistics.reqStats.runStatsLogging()
    }
}