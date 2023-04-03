package ru.techtheist.multithreadinginvestigation

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
@SpringBootApplication
class MultithreadingInvestigationApplication

fun main(args: Array<String>) {
    runApplication<MultithreadingInvestigationApplication>(*args)
}
