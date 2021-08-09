package com.example.perf;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.example.kafka.model.Call;
import com.example.kafka.producer.CallCreator;

import static org.awaitility.Awaitility.await;

public class EventProcessorPerfTest {

    private static final Logger LOG = LoggerFactory.getLogger(EventProcessorPerfTest.class);
    private static final Duration POLL_DURATION = Duration.ofSeconds(1);
    private static final Duration TEST_TIMEOUT = Duration.ofMinutes(5);
    private final KafkaTemplate<String, Call> messageEventProducer;
    private final RestHighLevelClient elasticSearchClient;
    private static final int NUM_MESSAGES = 1000;

    public EventProcessorPerfTest(final KafkaTemplate<String, Call> messageEventProducer,
                                  final RestHighLevelClient elasticSearchClient) {
        this.messageEventProducer = messageEventProducer;
        this.elasticSearchClient = elasticSearchClient;
    }

    public void execute() throws IOException {
        clearIndex();
        final Instant startTime = Instant.now();
        publishMessages();
        LOG.info("Events produced to kafka in {} seconds", Duration.between(startTime, Instant.now()).toSeconds());
        waitForEventsToProcess();
        final Duration elapsed = Duration.between(startTime, Instant.now());
        logTestResult(elapsed);
    }

    private void clearIndex() throws IOException {
        LOG.info("Starting deletion of all entries in insights_performance ES index");
        final DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("insights_performance");
        deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
        elasticSearchClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        LOG.info("Finished deletion of all entries in insights_performance ES index");
    }

    private void publishMessages() {
        //create x messages via kafka template
        IntStream.range(0, NUM_MESSAGES).forEach(i -> {
            final ListenableFuture<SendResult<String, Call>> future =
                messageEventProducer.send("insights", CallCreator.createCall());

            future.addCallback(new ListenableFutureCallback<>() {

                @Override
                public void onSuccess(final SendResult<String, Call> result) {
                    System.out.println("Sent call with offset=[" + result.getRecordMetadata().offset() + "]");
                }

                @Override
                public void onFailure(final Throwable ex) {
                    System.out.println("Unable to send call due to : " + ex.getMessage());
                }
            });
        });
    }

    private boolean isRecordCountMatches(final long actualCount) {
        return actualCount >= NUM_MESSAGES;
    }

    private void waitForEventsToProcess() {
        final CountRequest countRequest = new CountRequest("insights_performance");
        await().with()
               .pollDelay(POLL_DURATION)
               .pollInterval(POLL_DURATION)
               .atMost(TEST_TIMEOUT)
               .until(() -> elasticSearchClient.count(countRequest, RequestOptions.DEFAULT).getCount(), this::isRecordCountMatches);
    }

    private void logTestResult(final Duration elapsedTime) {
        final DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        LOG.info("");
        LOG.info("********************************************");
        LOG.info("***  Event Processor Perf Test Result   ****");
        LOG.info("********************************************");
        LOG.info("***  Total events processed - {}   ***", NUM_MESSAGES);
        LOG.info("***  Total time taken - {} second  ***", elapsedTime.toSeconds());
        LOG.info("*** Throughput - {} events/second  ***",
            df.format(NUM_MESSAGES * 1000. / elapsedTime.toMillis()));
        LOG.info("**********************************************");
        LOG.info("");
    }

}
