package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.util.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class DashBoardPage {

    private final SelenideElement card1Info = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private final SelenideElement card2Info = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
    private final SelenideElement reloadButton = $("[data-test-id='action-reload']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public DashBoardPage() {
        SelenideElement dashBoardMainHeader = $("[data-test-id='dashboard']~h1");
        dashBoardMainHeader.shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("Ваши карты")
        ), Duration.ofSeconds(15));
    }

    public SelenideElement cardChoice(String cardNumber) {
        if (cardNumber.equals("5559 0000 0000 0001")) {
            return card1Info;
        } else if (cardNumber.equals("5559 0000 0000 0002")) {
            return card2Info;
        } else {
            return null;
        }
    }

    public void checkBalance(String cardNumber, int expectedBalance) {
        cardChoice(cardNumber).shouldHave(Condition.exactText(
                DataHelper.hiddenCardNumber(cardNumber) + ", баланс: " + expectedBalance + " р.\nПополнить"
        ));
    }

    public TransferPage replenishment(TransferInfo planningTransfer) {
        cardChoice(planningTransfer.getCardNumberTo()).$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public void resultOfValidReplenishment(TransferInfo planningTransfer, int fromInitialBalance, int toInitialBalance) {

        checkBalance(planningTransfer.getCardNumberFrom(), fromInitialBalance - planningTransfer.getAmount());
        checkBalance(planningTransfer.getCardNumberTo(), toInitialBalance + planningTransfer.getAmount());
    }

    public void resultOfInvalidReplenishment(TransferInfo planningTransfer, int fromInitialBalance, int toInitialBalance) {

        checkBalance(planningTransfer.getCardNumberFrom(), fromInitialBalance);
        checkBalance(planningTransfer.getCardNumberTo(), toInitialBalance);
    }
}