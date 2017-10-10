package com.impaq.training.spring.webfluxexamples.common;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class BillingRecord {

    @Id
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
