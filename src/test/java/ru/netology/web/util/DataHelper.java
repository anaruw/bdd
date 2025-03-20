package ru.netology.web.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import ru.netology.web.data.UserInfo;
import ru.netology.web.data.VerificationCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class DataHelper {

    Random rnd = new Random();
    Faker faker = new Faker();
    List<String> enabledCards = new ArrayList<>(List.of(
            "5559 0000 0000 0001",
            "5559 0000 0000 0002"
    ));

    public UserInfo getAuthInfo() {
        return new UserInfo(
                userLogin(),
                userPassword()
        );
    }

    public String userLogin() {
        return "vasya";
    }

    public String userPassword() {
        return "qwerty123";
    }

    public VerificationCode newVerificationCode(UserInfo user) {

        if (user.getLogin().equals("vasya") && user.getPassword().equals("qwerty123")) return new VerificationCode("12345");

        return null;
    }

    public String hiddenCardNumber(String cardNumber) {
        return cardNumber.replaceFirst("\\d{4} \\d{4} \\d{4}", "**** **** ****");
    }

    public String fakeCardNumber() {
        String result;

        for (int i = 0; i < 3; i++) {
            result = faker.business().creditCardNumber().replaceAll("-", " ");
            if (!enabledCards.contains(result)) {
                return result;
            }
        }
        return "0000 0000 0000 0000";
    }

    public String cardNumberTo() {
        int index = rnd.nextInt(enabledCards.size());
        return enabledCards.get(index);
    }

    public String cardNumberFrom(String cardNumberTo, boolean equals) {
        if (equals) {
            return cardNumberTo;
        } else {
            int index = rnd.nextInt(enabledCards.size());

            for (int i = 0; i < 3; i++) {
                if (index == enabledCards.indexOf(cardNumberTo)) {
                    index = rnd.nextInt(enabledCards.size());
                } else break;
            }
            return enabledCards.get(index);
        }
    }

    public int extractBalance(String cardInfo) {
        String beforeBalanceText = "баланс: ";
        String afterBalanceText = " р.";

        return Integer.parseInt(cardInfo.substring(cardInfo.indexOf(beforeBalanceText) + beforeBalanceText.length(), cardInfo.indexOf(afterBalanceText)));
    }

    private int randomAmount(String cardFromInfo) {
        int result = rnd.nextInt(extractBalance(cardFromInfo));

        for (int i = 0; i < 3; i++) {
            if (result == 0) {
                result = rnd.nextInt(extractBalance(cardFromInfo));
            } else break;
        }
        return result;
    }

    public int amount(String cardFromInfo, boolean zeroAmount, boolean positiveAmount) {
        if (zeroAmount) {
            return 0;
        } else if (positiveAmount) {
            return randomAmount(cardFromInfo);
        } else {
            return - randomAmount(cardFromInfo);
        }
    }
}