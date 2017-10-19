package com.impaq.training.spring.webfluxexamples.ex05mongodbreactivestreamsdriver;

import static com.impaq.training.spring.webfluxexamples.common.BillingRecord.COLLECTION_NAME;

import org.bson.Document;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.stereotype.Repository;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.mongodb.reactivestreams.client.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class BillingRepository {

    private final MongoClient mongoClient;

    private final MongoProperties mongoProperties;

    public Flux<BillingRecord> findAll(){
        MongoDatabase database = mongoClient.getDatabase(mongoProperties.getDatabase());
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        return Flux.from(collection.find()).map(this::toBillingRecord);
    }

    private BillingRecord toBillingRecord(Document document) {
        return new BillingRecord(null,
                document.getString("firstName"),
                document.getString("lastName"),
                document.getString("type"),
                document.getDate("startTime"),
                document.getInteger("duration"));
    }

}
