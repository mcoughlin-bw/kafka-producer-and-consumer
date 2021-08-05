package com.example.kafka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Call {
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
