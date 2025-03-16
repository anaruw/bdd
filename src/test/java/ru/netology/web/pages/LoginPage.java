package ru.netology.web.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.UserInfo;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginInputField = $("[data-test-id='login'] input");
    private final SelenideElement passwordInputField = $("[data-test-id='password'] input");
    private final SelenideElement actionButton = $("[data-test-id='action-login']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public void auth(UserInfo user) {
        loginInputField.setValue(user.getLogin());
        passwordInputField.setValue(user.getPassword());
        actionButton.click();
    }

    public VerificationPage validAuth(UserInfo user) {
        auth(user);
        return new VerificationPage();
    }
}