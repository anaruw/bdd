package ru.netology.web.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.data.UserInfo;
import ru.netology.web.data.VerificationCode;

@UtilityClass
public class DataHelper {

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

    public TransferInfo planningTransfer(String cardNumberFrom, String cardNumberTo, int amount) {
        return new TransferInfo(
                cardNumberFrom,
                cardNumberTo,
                amount
        );
    }

    public int extractBalance(String cardInfo) {
        String beforeBalanceText = "баланс: ";
        String afterBalanceText = " р.";

        return Integer.parseInt(cardInfo.substring(cardInfo.indexOf(beforeBalanceText) + beforeBalanceText.length(), cardInfo.indexOf(afterBalanceText)));
    }

    public boolean enabledCardNumbers(TransferInfo planningTransfer) {
        return planningTransfer.getCardNumberFrom().equals("5559 0000 0000 0001") || planningTransfer.getCardNumberFrom().equals("5559 0000 0000 0002");
    }

    public boolean amountAboveZero(TransferInfo planningTransfer) {
        return planningTransfer.getAmount() > 0;
    }

    public boolean cardFromNotEqualsCardTo(TransferInfo planningTransfer) {
        return !planningTransfer.getCardNumberFrom().equals(planningTransfer.getCardNumberTo());
    }

    public String fakeCardNumber() {
        Faker faker = new Faker();
        String template = "5559 0000 0000 000";
        String result;

        for (int i = 0; i < 3; i++) {
            result = faker.business().creditCardNumber().replaceAll("-", " ");
            if (!result.equals(template + 1) || !result.equals(template + 2)) {
                return result;
            }
        }
        return "0000 0000 0000 0000";
    }
}