package ru.techtheist.multithreadingInvestigationClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class HighLoadTest {

    private static final AtomicInteger getCount = new AtomicInteger(0);
    private static final AtomicInteger setCount = new AtomicInteger(0);

    @Value("${app.requests.server}")
    private String server;

    @Value("${app.requests.uri}")
    private String uri;

    @Value("${app.requests.accountsTotal}")
    private int accountsTotal;

    @Value("${app.requests.concurrency}")
    private int concurrency;

    @Value("${app.requests.readQuota}")
    private int readQuota;

    @Value("${app.requests.writeQuota}")
    private int writeQuota;

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders headers = new HttpHeaders();

    private static final Logger logger = LoggerFactory.getLogger(HighLoadTest.class);

    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(concurrency);
        executor.setMaxPoolSize(concurrency);
        executor.setQueueCapacity(1000);
        executor.initialize();
        return executor;
    }

    @Async
    @EventListener(ApplicationStartedEvent.class)
    public void performTest() {
        headers.add("Content-Type", "application/json");
        double readProbability = (double) readQuota / (double) (readQuota + writeQuota);

        taskExecutor().execute(() -> {
            while (true) {

                if (ThreadLocalRandom.current().nextDouble() < readProbability) {

                    String accountUri = server + uri + (Math.abs(ThreadLocalRandom.current().nextInt(accountsTotal)) + 1);
                    ResponseEntity<String> responseEntity = restTemplate.exchange(accountUri, HttpMethod.GET, null, String.class);

                    getCount.getAndIncrement();
                    logger.debug("GET Request sent to " + accountUri + " with code: " + responseEntity.getStatusCode().value());
                } else {
                    String requestBody = "{\"amount\": 1}";
                    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

                    String accountUri = server + uri + (Math.abs(ThreadLocalRandom.current().nextInt(accountsTotal)) + 1);
                    ResponseEntity<String> responseEntity = restTemplate.exchange(accountUri, HttpMethod.POST, requestEntity, String.class);

                    setCount.getAndIncrement();
                    logger.debug("POST Request sent to " + accountUri + " with code: " + responseEntity.getStatusCode().value());
                }
            }
        });
    }

    @Scheduled(fixedDelay = 5000)
    public void logStats() {
        logger.info("Total requests send: " + (getCount.get() + setCount.get()) + ", ratio: " +
                Math.floor(((double) getCount.get() / (double) setCount.get()) * 100) / 100);
    }
}