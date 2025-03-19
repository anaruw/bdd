package ru.netology.web.service;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.data.UserInfo;
import ru.netology.web.data.VerificationCode;
import ru.netology.web.pages.DashBoardPage;
import ru.netology.web.pages.LoginPage;
import ru.netology.web.pages.TransferPage;
import ru.netology.web.pages.VerificationPage;
import ru.netology.web.util.DataHelper;

public class MoneyTransferTest {
    DashBoardPage dashBoardPage;
    String cardNumberFrom;
    String cardNumberTo;
    int amount;
    TransferInfo planningTransfer;

    private DashBoardPage prepareToTransfer() {
        planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();
        UserInfo testUser = DataHelper.getAuthInfo();
        VerificationCode verifyCode = DataHelper.newVerificationCode(testUser);

        VerificationPage verificationPage = loginPage.validAuth(testUser);
        verificationPage.verify(verifyCode);

        return new DashBoardPage();
    }

    @BeforeEach
    public void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    @DisplayName("valid_transfer")
    public void validTransfer(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        cardNumberFrom = "5559 0000 0000 0002";
        cardNumberTo = "5559 0000 0000 0001";
        amount = 1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom)) - planningTransfer.getAmount();
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo)) + planningTransfer.getAmount();
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputAmount(planningTransfer);
        transferPage.inputCardFromNumber(planningTransfer.getCardNumberFrom());
        dashBoardPage = transferPage.validTransfer(planningTransfer, testName);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("canceled_transfer")
    public void canceledTransfer(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        cardNumberFrom = "5559 0000 0000 0002";
        cardNumberTo = "5559 0000 0000 0001";
        amount = 1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        dashBoardPage = transferPage.canceledTransfer(testName);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("canceled_transfer_with_input")
    public void canceledTransferWithInput(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        cardNumberFrom = "5559 0000 0000 0002";
        cardNumberTo = "5559 0000 0000 0001";
        amount = 1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputAmount(planningTransfer);
        transferPage.inputCardFromNumber(planningTransfer.getCardNumberFrom());
        dashBoardPage = transferPage.canceledTransfer(testName);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_empty_card_from_field")
    public void transferWithEmptyCardFromField(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Выберите Вашу карту, откуда совершить перевод";
        cardNumberFrom = "5559 0000 0000 0001";
        cardNumberTo = "5559 0000 0000 0002";
        amount = 1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputAmount(planningTransfer);
        dashBoardPage = transferPage.invalidTransfer(testName, notificationText);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_fake_card_from_number")
    public void transferWithFakeCardFromNumber(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Выберите Вашу карту, откуда совершить перевод";
        cardNumberFrom = "5559 0000 0000 0001";
        cardNumberTo = "5559 0000 0000 0002";
        amount = 1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputAmount(planningTransfer);
        transferPage.inputCardFromNumber(DataHelper.fakeCardNumber());
        dashBoardPage = transferPage.invalidTransfer(testName, notificationText);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_card_from_equals_card_to")
    public void transferWithCardFromEqualsCardTo(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Выберите Вашу карту, откуда совершить перевод";
        cardNumberFrom = "5559 0000 0000 0001";
        cardNumberTo = "5559 0000 0000 0001";
        amount = 1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputAmount(planningTransfer);
        transferPage.inputCardFromNumber(planningTransfer.getCardNumberFrom());
        dashBoardPage = transferPage.invalidTransfer(testName, notificationText);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_empty_amount_field")
    public void transferWithEmptyAmountField(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Введите сумму перевода";
        cardNumberFrom = "5559 0000 0000 0001";
        cardNumberTo = "5559 0000 0000 0002";
        amount = 1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputCardFromNumber(planningTransfer.getCardNumberFrom());
        dashBoardPage = transferPage.invalidTransfer(testName, notificationText);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_zero_amount")
    public void transferWithZeroAmount(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Введите сумму перевода";
        cardNumberFrom = "5559 0000 0000 0001";
        cardNumberTo = "5559 0000 0000 0002";
        amount = 0;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputAmount(planningTransfer);
        transferPage.inputCardFromNumber(planningTransfer.getCardNumberFrom());

        if (transferPage.checkAmountInput(planningTransfer)) {
            dashBoardPage = transferPage.invalidTransfer(testName, notificationText);
        } else {
            dashBoardPage = transferPage.canceledTransfer(testName);
        }

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_negative_amount")
    public void transferWithNegativeAmount(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Сумма перевода введена некорректно";
        cardNumberFrom = "5559 0000 0000 0001";
        cardNumberTo = "5559 0000 0000 0002";
        amount = -1;

        dashBoardPage = prepareToTransfer();
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(planningTransfer);

        transferPage.inputAmount(planningTransfer);
        transferPage.inputCardFromNumber(planningTransfer.getCardNumberFrom());

        if (transferPage.checkAmountInput(planningTransfer)) {
            dashBoardPage = transferPage.invalidTransfer(testName, notificationText);
        } else {
            dashBoardPage = transferPage.canceledTransfer(testName);
        }

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }
}