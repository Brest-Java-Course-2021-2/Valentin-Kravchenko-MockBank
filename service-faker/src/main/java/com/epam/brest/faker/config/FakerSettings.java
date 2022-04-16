package com.epam.brest.faker.config;

import java.util.Locale;

public class FakerSettings {

    private Integer dataVolume;

    private Locale locale;

    private Integer accountMinTotalCards;

    private Integer accountMaxTotalCards;

    private Long amountUnitTimeToSubtract;

    public Integer getDataVolume() {
        return dataVolume;
    }

    public void setDataVolume(Integer dataVolume) {
        this.dataVolume = dataVolume;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Integer getAccountMinTotalCards() {
        return accountMinTotalCards;
    }

    public void setAccountMinTotalCards(Integer accountMinTotalCards) {
        this.accountMinTotalCards = accountMinTotalCards;
    }

    public Integer getAccountMaxTotalCards() {
        return accountMaxTotalCards;
    }

    public void setAccountMaxTotalCards(Integer accountMaxTotalCards) {
        this.accountMaxTotalCards = accountMaxTotalCards;
    }

    public Long getAmountUnitTimeToSubtract() {
        return amountUnitTimeToSubtract;
    }

    public void setAmountUnitTimeToSubtract(Long amountUnitTimeToSubtract) {
        this.amountUnitTimeToSubtract = amountUnitTimeToSubtract;
    }

}
