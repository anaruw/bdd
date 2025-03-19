package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.VerificationCode;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private final SelenideElement codeInputField = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        codeInputField.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void verify(VerificationCode code) {
        codeInputField.setValue(code.getCode());
        verifyButton.click();
    }
}