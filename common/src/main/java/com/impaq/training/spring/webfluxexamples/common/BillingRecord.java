package com.impaq.training.spring.webfluxexamples.common;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.Wither;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@JsonInclude(NON_NULL)
public class BillingRecord {

    public static final String COLLECTION_NAME = "billingRecord";

    @Id
    @Wither
    private String id;

    private String firstName;
    private String lastName;
    private String type;
    private Date startTime;
    private int duration;

    public Object[] toArrayOfFields(){
        return new Object[]{getFirstName(), getLastName(), getType(), getStartTime(), getDuration()};
    }

    public String toCsvString() {
        return Arrays
                .stream(toArrayOfFields())
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
