package ru.netology.web.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.data.UserInfo;
import ru.netology.web.data.VerificationCode;

import java.util.List;
import java.util.Random;

@UtilityClass
public class DataHelper {

    Random rnd = new Random();
    Faker faker = new Faker();


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

    public TransferInfo transferInfo(String cardNumberFrom, String cardNumberTo, int amount) {
        return new TransferInfo(
                cardNumberFrom,
                cardNumberTo,
                amount
        );
    }

    public String hiddenCardNumber(String cardNumber) {
        return cardNumber.replaceFirst("\\d{4} \\d{4} \\d{4}", "**** **** ****");
    }

    public String fakeCardNumber(List<String> enabledCards) {
        String result;

        for (int i = 0; i < 3; i++) {
            result = faker.business().creditCardNumber().replaceAll("-", " ");
            if (!enabledCards.contains(result)) {
                return result;
            }
        }
        return "0000 0000 0000 0000";
    }

    public String cardNumberTo(List<String> enabledCards) {
        int index = rnd.nextInt(enabledCards.size());
        return enabledCards.get(index);
    }

    public String cardNumberFrom(List<String> enabledCards, String cardNumberTo, boolean equals) {
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

    public int randomAmount(String cardInfo) {
        int result = rnd.nextInt(extractBalance(cardInfo));

        for (int i = 0; i < 3; i++) {
            if (result == 0) {
                result = rnd.nextInt(extractBalance(cardInfo));
            } else break;
        }
        return result;
    }

    public int zeroAmount() {
        return 0;
    }

    public int negativeAmount(String cardInfo) {
        return - randomAmount(cardInfo);
    }
}