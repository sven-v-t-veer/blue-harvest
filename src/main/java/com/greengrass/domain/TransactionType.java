package com.greengrass.domain;


import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT("deposit"), WITHDRAW("withdraw");

    final String value;

    TransactionType(String value) {
        this.value = value;
    }

}
