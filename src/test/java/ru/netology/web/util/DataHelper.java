package ru.netology.web.util;

import com.codeborne.selenide.SelenideElement;
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

    public int cardBalance(SelenideElement selectedCard) {

        String beforeBalanceText = "**** **** **** ****, баланс: ";
        String afterBalanceText = " р.\nПополнить";
        String cardInfo = selectedCard.getText();

        return Integer.parseInt(cardInfo.substring(
                beforeBalanceText.length(),
                cardInfo.length() - afterBalanceText.length()
        ));
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
}