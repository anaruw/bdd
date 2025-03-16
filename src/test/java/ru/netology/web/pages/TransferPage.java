package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.util.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private final SelenideElement amountInputField = $("[data-test-id='amount'] input");
    private final SelenideElement cardFromInputField = $("[data-test-id='from'] input");
    private final SelenideElement cardToInputField = $("[data-test-id='to'] input");
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

    public void inputAmount(TransferInfo planningTransfer) {
        amountInputField.setValue("" + planningTransfer.getAmount());
    }

    public void inputCardFrom(TransferInfo planningTransfer) {
        cardFromInputField.setValue(planningTransfer.getCardNumberFrom());
    }

    public void inputValues(TransferInfo planningTransfer) {
        inputAmount(planningTransfer);
        inputCardFrom(planningTransfer);
    }

    public void checkCardTo(TransferInfo planningTransfer) {
        cardToInputField.shouldHave(Condition.value(
                DataHelper.hiddenCardNumber(planningTransfer.getCardNumberTo())
        ));
    }

    public DashBoardPage validTransfer(TransferInfo planningTransfer) {
        inputValues(planningTransfer);
        checkCardTo(planningTransfer);

        transferButton.click();

        return new DashBoardPage();
    }

    public DashBoardPage canceledTransfer() {
        cancelButton.click();
        return new DashBoardPage();
    }

    public void transferWithEmptyAmountField(TransferInfo planningTransfer) {
        inputCardFrom(planningTransfer);
        transferButton.click();
        errorNotification.shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("Ошибка! Произошла ошибка")
        ));
    }

    public void transferWithEmptyCardFromField(TransferInfo planningTransfer) {
        inputAmount(planningTransfer);
        transferButton.click();
        errorNotification.shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("Ошибка! Произошла ошибка")
        ));
    }

    public void transferWithAmountAboveBalance(TransferInfo planningTransfer) {
        inputValues(planningTransfer);
        transferButton.click();
        errorNotification.shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("Ошибка! Произошла ошибка")
        ));
    }

    public DashBoardPage canceledTransferWithCompletedInput(TransferInfo planningTransfer) {
        inputValues(planningTransfer);
        cancelButton.click();
        return new DashBoardPage();
    }
}