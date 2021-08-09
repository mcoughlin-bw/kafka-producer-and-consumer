package com.example.kafka.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Call {
    private String id;
    private Direction direction;
    private Date startTime;
    private Date endTime;
    private CallType callType;
    private String calledNumberCountryAbbreviation;
    private String calledNumberLata;
    private String callingNumberCountryAbbreviation;
    private String callingNumberLata;
    private String carrierName;
    private String customerGan;
    private String customerSan;
    private String customerName;
    private Integer sipResponseCode;
}
