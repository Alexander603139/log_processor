package io.github.bugrov.log_processor.infrastructure.mongodb;

import io.github.bugrov.log_processor.domain.port.outgoing.IPStatisticsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoIPStatisticsAdapter implements IPStatisticsPort {

    private final MongoTemplate mongoTemplate;

    @Override
    public long countEventsByIP(String ipAddress, Instant since) {
        log.info("=== COUNTING EVENTS for IP: {}, since: {}", ipAddress, since);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("ipAddress").is(ipAddress)
                        .and("timestamp").gte(since)),
                count().as("count")
        );
        AggregationResults<CountResult> results = mongoTemplate.aggregate(
                aggregation, "event_logs", CountResult.class
        );
        long count = results.getMappedResults().isEmpty() ? 0 : results.getMappedResults().get(0).getCount();
        log.info("=== COUNT RESULT: {}", count);
        return count;
    }

    static class CountResult {
        private long count;
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }
}