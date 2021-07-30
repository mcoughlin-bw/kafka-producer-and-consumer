package com.example.kafka.model;

import java.util.Date;

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

    public Call() {
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(final CallType callType) {
        this.callType = callType;
    }

    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }

    public String getCalledNumberCountryAbbreviation() {
        return calledNumberCountryAbbreviation;
    }

    public void setCalledNumberCountryAbbreviation(final String calledNumberCountryAbbreviation) {
        this.calledNumberCountryAbbreviation = calledNumberCountryAbbreviation;
    }

    public String getCalledNumberLata() {
        return calledNumberLata;
    }

    public void setCalledNumberLata(final String calledNumberLata) {
        this.calledNumberLata = calledNumberLata;
    }

    public String getCallingNumberCountryAbbreviation() {
        return callingNumberCountryAbbreviation;
    }

    public void setCallingNumberCountryAbbreviation(final String callingNumberCountryAbbreviation) {
        this.callingNumberCountryAbbreviation = callingNumberCountryAbbreviation;
    }

    public String getCallingNumberLata() {
        return callingNumberLata;
    }

    public void setCallingNumberLata(final String callingNumberLata) {
        this.callingNumberLata = callingNumberLata;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(final String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCustomerGan() {
        return customerGan;
    }

    public void setCustomerGan(final String customerGan) {
        this.customerGan = customerGan;
    }

    public String getCustomerSan() {
        return customerSan;
    }

    public void setCustomerSan(final String customerSan) {
        this.customerSan = customerSan;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    public Integer getSipResponseCode() {
        return sipResponseCode;
    }

    public void setSipResponseCode(final Integer sipResponseCode) {
        this.sipResponseCode = sipResponseCode;
    }
}
