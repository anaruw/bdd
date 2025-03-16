package ru.netology.web.data;

import lombok.Value;

@Value
public class TransferInfo {
    String cardNumberFrom;
    String cardNumberTo;
    int amount;
}