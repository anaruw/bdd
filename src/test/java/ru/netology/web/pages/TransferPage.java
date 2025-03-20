package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private final SelenideElement amountInputField = $("[data-test-id='amount'] input");
    private final SelenideElement cardFromInputField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        SelenideElement dashBoardTransferHeader = $("[data-test-id='dashboard']~h1");
        dashBoardTransferHeader.shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("Пополнение карты")
        ), Duration.ofSeconds(15));
    }

    public void inputAmount(int amount) {
        amountInputField.setValue("" + amount);
    }

    public void inputCardFromNumber(String cardFromNumber) {
        cardFromInputField.setValue(cardFromNumber);
    }

    public boolean checkAmountInput(int amount) {
        return amountInputField.getValue().equals("" + amount);
    }

    private void transferButtonClick(String testName) {
        Selenide.screenshot(testName + "_before_click");
        transferButton.click();
        Selenide.screenshot(testName + "_after_click");
    }

    private void cancelButtonClick(String testName) {
        Selenide.screenshot(testName + "_before_click");
        cancelButton.click();
        Selenide.screenshot(testName + "_after_click");
    }

    private void errorNotification(String testName, String notificationText) {
        errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
        Selenide.screenshot(testName + "_notification");
        errorNotification.shouldHave(Condition.exactText(notificationText));
    }

    public DashBoardPage validTransfer(String testName) {
            transferButtonClick(testName);
            return new DashBoardPage();
    }

    public DashBoardPage canceledTransfer(String testName) {
        cancelButtonClick(testName);
        return new DashBoardPage();
    }

    public DashBoardPage invalidTransfer(String testName, String notificationText) {
        transferButtonClick(testName);
        errorNotification(testName, notificationText);

        cancelButton.click();
        return new DashBoardPage();
    }
}