package com.epam.brest.faker.config;

import java.util.Locale;

public class FakerSettings {

    private Integer amount;

    private Locale locale;

    private Integer accountMinTotalCards;

    private Integer accountMaxTotalCards;

    private Long unitTimeLimit;

    private Long balanceLimit;

    private Integer balanceScale;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public Long getUnitTimeLimit() {
        return unitTimeLimit;
    }

    public void setUnitTimeLimit(Long unitTimeLimit) {
        this.unitTimeLimit = unitTimeLimit;
    }

    public Long getBalanceLimit() {
        return balanceLimit;
    }

    public void setBalanceLimit(Long balanceLimit) {
        this.balanceLimit = balanceLimit;
    }

    public Integer getBalanceScale() {
        return balanceScale;
    }

    public void setBalanceScale(Integer balanceScale) {
        this.balanceScale = balanceScale;
    }

}
