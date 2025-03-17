package ru.netology.web.service;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.TransferInfo;
import ru.netology.web.data.UserInfo;
import ru.netology.web.data.VerificationCode;
import ru.netology.web.pages.DashBoardPage;
import ru.netology.web.pages.LoginPage;
import ru.netology.web.pages.TransferPage;
import ru.netology.web.pages.VerificationPage;
import ru.netology.web.util.DataHelper;

public class MoneyTransferTest {

    @BeforeEach
    public void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    public void validTransferTest() {

        String cardNumberFrom = "5559 0000 0000 0001";
        String cardNumberTo = "5559 0000 0000 0002";
        int amount = 1000;

        UserInfo info = DataHelper.getAuthInfo();
        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();

        VerificationPage verificationPage = loginPage.validAuth(info);
        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);

        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));

        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
        dashBoardMain = testTransfer.validTransfer(planningTransfer);

        dashBoardMain.resultOfValidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
    }

    @Test
    public void canceledTransferWithCompletedInput() {
        String cardNumberFrom = "5559 0000 0000 0001";
        String cardNumberTo = "5559 0000 0000 0002";
        int amount = 10;

        UserInfo info = DataHelper.getAuthInfo();
        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();

        VerificationPage verificationPage = loginPage.validAuth(info);
        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);

        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));

        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
        dashBoardMain = testTransfer.canceledTransferWithCompletedInput(planningTransfer);

        dashBoardMain.resultOfInvalidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
    }

    @Test
    public void transferWithEmptyCardFromInput() {
        String cardNumberFrom = "5559 0000 0000 0001";
        String cardNumberTo = "5559 0000 0000 0002";
        int amount = 11001;

        UserInfo info = DataHelper.getAuthInfo();
        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();

        VerificationPage verificationPage = loginPage.validAuth(info);
        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);

        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));

        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
        testTransfer.transferWithEmptyCardFromField(planningTransfer);
        dashBoardMain = testTransfer.canceledTransfer();

        dashBoardMain.resultOfInvalidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
    }

    @Test
    public void transferWithEmptyAmountField() {
        String cardNumberFrom = "5559 0000 0000 0001";
        String cardNumberTo = "5559 0000 0000 0002";
        int amount = 11001;

        UserInfo info = DataHelper.getAuthInfo();
        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();

        VerificationPage verificationPage = loginPage.validAuth(info);
        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);

        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));

        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
        testTransfer.transferWithEmptyAmountField(planningTransfer);
        dashBoardMain = testTransfer.canceledTransfer();

        dashBoardMain.resultOfInvalidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
    }

    @Test
    public void transferWithZeroAmount() {
        String cardNumberFrom = "5559 0000 0000 0001";
        String cardNumberTo = "5559 0000 0000 0002";
        int amount = 0;

        UserInfo info = DataHelper.getAuthInfo();
        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();

        VerificationPage verificationPage = loginPage.validAuth(info);
        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);

        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));

        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
        testTransfer.transferWithZeroAmount(planningTransfer);
        dashBoardMain = testTransfer.canceledTransfer();

        dashBoardMain.resultOfInvalidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
    }

    @Test
    public void transferWithCardFromEqualsCardTo() {
        String cardNumberFrom = "5559 0000 0000 0002";
        String cardNumberTo = "5559 0000 0000 0002";
        int amount = 1;

        UserInfo info = DataHelper.getAuthInfo();
        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();

        VerificationPage verificationPage = loginPage.validAuth(info);
        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);

        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));

        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
        testTransfer.transferWithCardFromEqualsCardTo(planningTransfer);
        dashBoardMain = testTransfer.canceledTransfer();

        dashBoardMain.resultOfInvalidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
    }

    @Test
    public void transferWithNegativeAmount() {
        String cardNumberFrom = "5559 0000 0000 0001";
        String cardNumberTo = "5559 0000 0000 0002";
        int amount = -1;

        UserInfo info = DataHelper.getAuthInfo();
        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);

        LoginPage loginPage = new LoginPage();

        VerificationPage verificationPage = loginPage.validAuth(info);
        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);

        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));

        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
        testTransfer.transferWithNegativeAmount(planningTransfer);
        dashBoardMain = testTransfer.canceledTransfer();

        dashBoardMain.resultOfInvalidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
    }

//    @Test
//    public void transferWithAmountAboveBalance() {
//        String cardNumberFrom = "5559 0000 0000 0001";
//        String cardNumberTo = "5559 0000 0000 0002";
//        int amount = 30000;
//
//        UserInfo info = DataHelper.getAuthInfo();
//        VerificationCode verificationCode = DataHelper.newVerificationCode(info);
//        TransferInfo planningTransfer = DataHelper.planningTransfer(cardNumberFrom, cardNumberTo, amount);
//
//        LoginPage loginPage = new LoginPage();
//
//        VerificationPage verificationPage = loginPage.validAuth(info);
//        DashBoardPage dashBoardMain = verificationPage.validVerify(verificationCode);
//
//        int fromInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberFrom));
//        int toInitialBalance = DataHelper.cardBalance(dashBoardMain.cardChoice(cardNumberTo));
//
//        TransferPage testTransfer = dashBoardMain.replenishment(planningTransfer);
//        testTransfer.transferWithAmountAboveBalance(planningTransfer, fromInitialBalance);
//        dashBoardMain = testTransfer.canceledTransfer();
//
//        dashBoardMain.resultOfInvalidReplenishment(planningTransfer, fromInitialBalance, toInitialBalance);
//    }
}