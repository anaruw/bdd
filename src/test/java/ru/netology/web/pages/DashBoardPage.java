package ru.netology.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.util.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {

    private final ElementsCollection cardList = $$(".list__item div");

    public DashBoardPage() {
        SelenideElement dashBoardMainHeader = $("[data-test-id='dashboard']~h1");
        dashBoardMainHeader.shouldBe(Condition.allOf(
                Condition.visible,
                Condition.exactText("Ваши карты")
        ), Duration.ofSeconds(15));
    }

    private SelenideElement cardChoice(String cardNumber) {
        return cardList.findBy(Condition.text(DataHelper.hiddenCardNumber(cardNumber)));
    }

    public String cardInfo(String cardNumber) {
        return cardChoice(cardNumber).getText();
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
}