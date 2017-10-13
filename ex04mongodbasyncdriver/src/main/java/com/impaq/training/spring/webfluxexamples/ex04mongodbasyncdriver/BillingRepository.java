package com.impaq.training.spring.webfluxexamples.ex04mongodbasyncdriver;

import org.bson.Document;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.stereotype.Repository;

import com.impaq.training.spring.webfluxexamples.common.BillingRecord;
import com.mongodb.rx.client.*;

import lombok.RequiredArgsConstructor;
import rx.Observable;

@Repository
@RequiredArgsConstructor
public class BillingRepository {

    private final MongoClient mongoClient;

    private final MongoProperties mongoProperties;

    public Observable<BillingRecord> findAll(){
        MongoDatabase database = mongoClient.getDatabase(mongoProperties.getDatabase());
        MongoCollection<Document> collection = database.getCollection("billingRecord");
        return collection.find().toObservable().map(this::convert);
    }

    private BillingRecord convert(Document document) {
        return new BillingRecord(null, document.getString("firstName"), document.getString("lastName"), document.getString("type"), document.getDate("startTime"), document.getInteger("duration"));
    }

}
