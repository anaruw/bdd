package ru.netology.web.service;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.UserInfo;
import ru.netology.web.data.VerificationCode;
import ru.netology.web.pages.DashBoardPage;
import ru.netology.web.pages.LoginPage;
import ru.netology.web.pages.TransferPage;
import ru.netology.web.pages.VerificationPage;
import ru.netology.web.util.DataHelper;

public class MoneyTransferTest {
    DashBoardPage dashBoardPage;
    String cardNumberTo;
    String cardNumberFrom;
    int amount;

    private DashBoardPage dashBoardPageMain() {
        LoginPage loginPage = new LoginPage();
        UserInfo testUser = DataHelper.getAuthInfo();
        VerificationCode verifyCode = DataHelper.newVerificationCode(testUser);

        VerificationPage verificationPage = loginPage.validAuth(testUser);
        verificationPage.verify(verifyCode);

        return new DashBoardPage();
    }

    private void prepareToTransfer(boolean equalsCardNumbers, boolean zeroAmount, boolean positiveAmount) {
        cardNumberTo = DataHelper.cardNumberTo();
        cardNumberFrom = DataHelper.cardNumberFrom(cardNumberTo, equalsCardNumbers);
        amount = DataHelper.amount(dashBoardPage.cardInfo(cardNumberFrom), zeroAmount, positiveAmount);
    }

    @BeforeEach
    public void setUp() {
        Selenide.open("http://localhost:9999");

        dashBoardPage = dashBoardPageMain();
    }

    @Test
    @DisplayName("valid_transfer")
    public void validTransfer(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        prepareToTransfer(false, false, true);

        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo)) + amount;
        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom)) - amount;
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputAmount(amount);
        transferPage.inputCardFromNumber(cardNumberFrom);
        dashBoardPage = transferPage.validTransfer(testName);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("canceled_transfer")
    public void canceledTransfer(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();

        cardNumberTo = DataHelper.cardNumberTo();
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        dashBoardPage = transferPage.canceledTransfer(testName);
    }

    @Test
    @DisplayName("canceled_transfer_with_input")
    public void canceledTransferWithInput(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        prepareToTransfer(false, false, true);

        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputAmount(amount);
        transferPage.inputCardFromNumber(cardNumberFrom);
        dashBoardPage = transferPage.canceledTransfer(testName);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_empty_card_from_field")
    public void transferWithEmptyCardFromField(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Выберите Вашу карту, откуда совершить перевод";
        prepareToTransfer(false, false, true);

        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputAmount(amount);
        dashBoardPage = transferPage.invalidTransfer(testName, notificationText);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_fake_card_from_number")
    public void transferWithFakeCardFromNumber(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Выберите Вашу карту, откуда совершить перевод";
        prepareToTransfer(false, false, true);

        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputAmount(amount);
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
        prepareToTransfer(true, false, true);

        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputAmount(amount);
        transferPage.inputCardFromNumber(cardNumberFrom);
        dashBoardPage = transferPage.invalidTransfer(testName, notificationText);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_empty_amount_field")
    public void transferWithEmptyAmountField(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Введите сумму перевода";
        prepareToTransfer(false, false, true);

        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputCardFromNumber(cardNumberFrom);
        dashBoardPage = transferPage.invalidTransfer(testName, notificationText);

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }

    @Test
    @DisplayName("transfer_with_zero_amount")
    public void transferWithZeroAmount(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String notificationText = "Введите сумму перевода";
        prepareToTransfer(false, true, true);

        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputAmount(amount);
        transferPage.inputCardFromNumber(cardNumberFrom);

        if (transferPage.checkAmountInput(amount)) {
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
        prepareToTransfer(false, false, false);

        int expectedCardFromBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberFrom));
        int expectedCardToBalance = DataHelper.extractBalance(dashBoardPage.cardInfo(cardNumberTo));
        TransferPage transferPage = dashBoardPage.replenishment(cardNumberTo);

        transferPage.inputAmount(amount);
        transferPage.inputCardFromNumber(cardNumberFrom);

        if (transferPage.checkAmountInput(amount)) {
            dashBoardPage = transferPage.invalidTransfer(testName, notificationText);
        } else {
            dashBoardPage = transferPage.canceledTransfer(testName);
        }

        dashBoardPage.checkBalance(cardNumberFrom, expectedCardFromBalance);
        dashBoardPage.checkBalance(cardNumberTo, expectedCardToBalance);
    }
}