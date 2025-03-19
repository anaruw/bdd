package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.util.DataHelper;

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

    private void inputAmount(TransferInfo planningTransfer) {
        amountInputField.setValue("" + planningTransfer.getAmount());
    }

    private void inputCardFromNumber(String cardFromNumber) {
        cardFromInputField.setValue(cardFromNumber);
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

    public DashBoardPage validTransfer(TransferInfo planningTransfer, String testName) {
        if (DataHelper.enabledCardNumbers(planningTransfer) &&
                DataHelper.cardFromNotEqualsCardTo(planningTransfer) &&
                DataHelper.amountAboveZero(planningTransfer)
        ) {
            inputAmount(planningTransfer);
            inputCardFromNumber(planningTransfer.getCardNumberFrom());

            transferButtonClick(testName);
            return new DashBoardPage();
        }
        return null;
    }

    public DashBoardPage canceledTransfer(String testName) {
        cancelButtonClick(testName);
        return new DashBoardPage();
    }

    public DashBoardPage canceledTransferWithInput (TransferInfo planningTransfer, String testName) {
        if (DataHelper.enabledCardNumbers(planningTransfer) &&
                DataHelper.cardFromNotEqualsCardTo(planningTransfer) &&
                DataHelper.amountAboveZero(planningTransfer)
        ) {
            inputAmount(planningTransfer);
            inputCardFromNumber(planningTransfer.getCardNumberFrom());

            cancelButtonClick(testName);
            return new DashBoardPage();
        }
        return null;
    }

    public DashBoardPage transferWithEmptyCardFromField (TransferInfo planningTransfer, String testName) {
        if (DataHelper.amountAboveZero(planningTransfer)) {
            inputAmount(planningTransfer);

            transferButtonClick(testName);
            errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
            Selenide.screenshot(testName + "_notification");
            errorNotification.shouldHave(Condition.exactText("Выберите Вашу карту, откуда совершить перевод"));

            cancelButton.click();
            return new DashBoardPage();
        }
        return null;
    }

    public DashBoardPage transferWithCardFromEqualsCardTo (TransferInfo planningTransfer, String testName) {
        if (DataHelper.enabledCardNumbers(planningTransfer) &&
                !DataHelper.cardFromNotEqualsCardTo(planningTransfer) &&
                DataHelper.amountAboveZero(planningTransfer)
        ) {
            inputAmount(planningTransfer);
            inputCardFromNumber(planningTransfer.getCardNumberFrom());

            transferButtonClick(testName);
            errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
            Selenide.screenshot(testName + "_notification");
            errorNotification.shouldHave(Condition.exactText("Выберите Вашу карту, откуда совершить перевод"));

            cancelButton.click();
            return new DashBoardPage();
        }
        return null;
    }

    public DashBoardPage transferWithFakeCardFromNumber (TransferInfo planningTransfer, String testName) {
        if (DataHelper.amountAboveZero(planningTransfer)) {
            inputAmount(planningTransfer);
            inputCardFromNumber(DataHelper.fakeCardNumber());

            transferButtonClick(testName);
            errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
            Selenide.screenshot(testName + "_notification");
            errorNotification.shouldHave(Condition.exactText("Выберите Вашу карту, откуда совершить перевод"));

            cancelButton.click();
            return new DashBoardPage();
        }
        return null;
    }

    public DashBoardPage transferWithEmptyAmountField (TransferInfo planningTransfer, String testName) {
        if (DataHelper.enabledCardNumbers(planningTransfer) &&
                DataHelper.cardFromNotEqualsCardTo(planningTransfer)
        ) {
            inputCardFromNumber(planningTransfer.getCardNumberFrom());

            transferButtonClick(testName);
            errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
            Selenide.screenshot(testName + "_notification");
            errorNotification.shouldHave(Condition.exactText("Выберите Вашу карту, откуда совершить перевод"));

            cancelButton.click();
            return new DashBoardPage();
        }
        return null;
    }

    public DashBoardPage transferWithZeroAmount (TransferInfo planningTransfer, String testName) {
        if (DataHelper.enabledCardNumbers(planningTransfer) &&
                DataHelper.cardFromNotEqualsCardTo(planningTransfer) &&
                planningTransfer.getAmount() == 0
        ) {
            inputAmount(planningTransfer);
            inputCardFromNumber(planningTransfer.getCardNumberFrom());

            transferButtonClick(testName);
            errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
            Selenide.screenshot(testName + "_notification");
            errorNotification.shouldHave(Condition.exactText("Выберите Вашу карту, откуда совершить перевод"));

            cancelButton.click();
            return new DashBoardPage();
        }
        return null;
    }

    public DashBoardPage transferWithNegativeAmount (TransferInfo planningTransfer, String testName) {
        if (DataHelper.enabledCardNumbers(planningTransfer) &&
                DataHelper.cardFromNotEqualsCardTo(planningTransfer) &&
                planningTransfer.getAmount() < 0
        ) {
            inputAmount(planningTransfer);
            if (Integer.parseInt(amountInputField.getValue()) < 0) {
                inputCardFromNumber(planningTransfer.getCardNumberFrom());

                transferButtonClick(testName);
                errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
                Selenide.screenshot(testName + "_notification");
                errorNotification.shouldHave(Condition.exactText("Выберите Вашу карту, откуда совершить перевод"));
            }
            cancelButton.click();
            return new DashBoardPage();
        }
        return null;
    }
}