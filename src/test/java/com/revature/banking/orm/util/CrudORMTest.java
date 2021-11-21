package com.revature.banking.orm.util;

import com.revature.banking.daos.BankDAO;
import com.revature.banking.exceptions.AuthorizationException;
import com.revature.banking.exceptions.NotEnoughBalanceException;
import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.AppUser;
import com.revature.banking.models.BankAccount;
import com.revature.banking.models.BankTransaction;
import com.revature.banking.services.BankService;
import com.revature.banking.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CrudORMTest {

    UserService mockUserService;
    BankDAO mockBankDAO;

    BankService but;

    @Before
    public void testCaseSetup() {
        mockUserService = mock(UserService.class);
        mockBankDAO = mock(BankDAO.class);
        but = new BankService(mockBankDAO, mockUserService);

        when(mockUserService.isSessionActive()).thenReturn(true);
    }

    @After
    public void testCaseCleanUp() {
        mockUserService = null;
        but = null;
    }

    @Test
    public void test_isUserValid_returnsTrue_givenValidUser() {

        BankAccount invalidBankAccountName = new BankAccount("valid", "valid");

        boolean actualResult = but.isBankAccountValid(invalidBankAccountName);

        Assert.assertTrue("Expected bank account to be considered valid", actualResult);
    }

    @Test
    public void test_isBankAccountValid_returnsFalse_givenBankAccountWithInvalidBankAccountName() {

        BankAccount invalidBankAccountName_1 = new BankAccount(null, "valid");
        BankAccount invalidBankAccountName_2 = new BankAccount("", "valid");
        BankAccount invalidBankAccountName_3 = new BankAccount("             ", "valid");

        boolean actualResult_1 = but.isBankAccountValid(invalidBankAccountName_1);
        boolean actualResult_2 = but.isBankAccountValid(invalidBankAccountName_2);
        boolean actualResult_3 = but.isBankAccountValid(invalidBankAccountName_3);

        Assert.assertFalse("Expected bank account to be considered false.", actualResult_1);
        Assert.assertFalse("Expected bank account to be considered false.", actualResult_2);
        Assert.assertFalse("Expected bank account to be considered false.", actualResult_3);
    }

    @Test
    public void test_isBankAccountValid_returnsTrue_givenValidBankAccount() {

        BankAccount validUBankAccount = new BankAccount("valid", "valid");

        boolean actualResult = but.isBankAccountValid(validUBankAccount);

        Assert.assertTrue("Expected BankAccount to be considered valid", actualResult);
    }

    @Test
    public void test_isBankAccountValid_returnsFalse_givenBankAccountWithInvalidFirstName() {

        BankAccount invalidBankAccount_1 = new BankAccount(null, "valid");
        BankAccount invalidBankAccount_2 = new BankAccount("", "valid");
        BankAccount invalidBankAccount_3 = new BankAccount("             ", "valid");

        boolean actualResult_1 = but.isBankAccountValid(invalidBankAccount_1);
        boolean actualResult_2 = but.isBankAccountValid(invalidBankAccount_2);
        boolean actualResult_3 = but.isBankAccountValid(invalidBankAccount_3);

        Assert.assertFalse("Expected BankAccount to be considered false.", actualResult_1);
        Assert.assertFalse("Expected BankAccount to be considered false.", actualResult_2);
        Assert.assertFalse("Expected BankAccount to be considered false.", actualResult_3);
    }

    @Test(expected = AuthorizationException.class)
    public void test_openBankAccount_returnsTrue_givenSessionDeActive() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        when(mockUserService.isSessionActive()).thenReturn(false);
        when(mockBankDAO.save(validBankAccount)).thenReturn(validBankAccount);

        try {
            boolean actualResult = but.openBankAccount(validBankAccount);
        } finally {
            verify(mockBankDAO, times(0)).save(validBankAccount);
        }
    }

    @Test
    public void test_openBankAccount_returnsTrue_givenValidUser() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        when(mockBankDAO.save(validBankAccount)).thenReturn(validBankAccount);

        boolean actualResult = but.openBankAccount(validBankAccount);

        Assert.assertTrue("Expected result to be true with valid bank account provided.", actualResult);
        verify(mockBankDAO, times(1)).save(validBankAccount);
    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_openBankAccount_throwsResourcePersistenceException_givenValidBankAccount() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        when(mockBankDAO.save(validBankAccount)).thenReturn(null);

        try {
            boolean actualResult = but.openBankAccount(validBankAccount);
        } finally {
            verify(mockBankDAO, times(1)).save(validBankAccount);
        }
    }

    @Test
    public void test_getBankAccountsByUserId_returnsTrue_givenValidUser() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        List<BankAccount> validBankAccountsLists = new LinkedList<>();
        validBankAccountsLists.add(validBankAccount);
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid", "valid");
        AppUser mockAppUser = mock(AppUser.class);
        when(mockUserService.getSessionUser()).thenReturn(validUser);
        when(mockAppUser.getId()).thenReturn("validUser");
        when(mockBankDAO.findBankAccountsByUserId("valid","Me")).thenReturn(validBankAccountsLists);

        List<BankAccount> actualResult = but.getBankAccountsByUserId("Me");

        Assert.assertNotNull("Expected result to be true with valid user id provided.", actualResult);
    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_getBankAccountsByUserId_throwsResourcePersistenceException_givenValidBankAccount() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        List<BankAccount> validBankAccountsLists = new LinkedList<>();
        validBankAccountsLists.add(validBankAccount);
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid", "valid");
        AppUser mockAppUser = mock(AppUser.class);
        when(mockUserService.getSessionUser()).thenReturn(validUser);
        when(mockAppUser.getId()).thenReturn("validUser");
        when(mockBankDAO.findBankAccountsByUserId("valid","Me")).thenReturn(null);

        try {
            List<BankAccount> actualResult = but.getBankAccountsByUserId("Me");
        } finally {
            ;
        }
    }

    // ------------------------------------
    @Test
    public void test_getBankAccountsByOthersThanBankAccountId_returnsTrue_givenValidUBankAccountId() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        List<BankAccount> validBankAccountsLists = new LinkedList<>();
        validBankAccountsLists.add(validBankAccount);
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid", "valid");
        AppUser mockAppUser = mock(AppUser.class);
        when(mockUserService.getSessionUser()).thenReturn(validUser);
        when(mockAppUser.getId()).thenReturn("validUser");
        when(mockBankDAO.findBankAccountsByOthersThanBankAccountId("valid")).thenReturn(validBankAccountsLists);

        List<BankAccount> actualResult = but.getBankAccountsByOthersThanBankAccountId("valid");

        Assert.assertNotNull("Expected result to be true with valid user id provided.", actualResult);
    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_getBankAccountsByOthersThanBankAccountId_throwsResourcePersistenceException_givenInValidUBankAccountId() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        List<BankAccount> validBankAccountsLists = new LinkedList<>();
        validBankAccountsLists.add(validBankAccount);
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid", "valid");
        AppUser mockAppUser = mock(AppUser.class);
        when(mockUserService.getSessionUser()).thenReturn(validUser);
        when(mockAppUser.getId()).thenReturn("validUser");
        when(mockBankDAO.findBankAccountsByOthersThanBankAccountId("valid")).thenReturn(null);

        try {
            List<BankAccount> actualResult = but.getBankAccountsByOthersThanBankAccountId("valid");
        } finally {
            ;
        }
    }
    // ------------------------------------
    @Test
    public void test_transact_returnsTrue_givenBankTransaction() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBank_account_id("valid");
        BankTransaction bankTransaction = new BankTransaction(999, validUser, bankAccount);
        when(mockBankDAO.transact(bankTransaction)).thenReturn(bankTransaction);

        BankTransaction actualResult = but.transact(bankTransaction);

        Assert.assertNotNull("Expected result to be true with valid bank account provided.", actualResult);
        verify(mockBankDAO, times(1)).transact(bankTransaction);
    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_transact_throwsResourcePersistenceException_givenBankTransaction() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBank_account_id("valid");
        BankTransaction bankTransaction = new BankTransaction(999, validUser, bankAccount);
        when(mockBankDAO.transact(bankTransaction)).thenReturn(null);

        try {
            BankTransaction actualResult = but.transact(bankTransaction);
        } finally {
            ;
        }
    }

    @Test(expected = NotEnoughBalanceException.class)
    public void test_transact_throwsNotEnoughBalanceException_givenBankTransaction() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBank_account_id("valid");
        bankAccount.setBalance(1);
        BankTransaction bankTransaction = new BankTransaction(-2, validUser, bankAccount);
        when(mockBankDAO.transact(bankTransaction)).thenReturn(null);

        try {
            BankTransaction actualResult = but.transact(bankTransaction);
        } finally {
            verify(mockBankDAO, times(0)).transact(bankTransaction);
        }
    }

    @Test
    public void test_getTransactionsByUserAccountId_returnTrue_givenValidBankAccount() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid", "valid");
        BankAccount validBankAccount = new BankAccount("valid", "valid");

        BankTransaction bankTransaction = new BankTransaction(2, validUser, validBankAccount);
        List<BankTransaction> validBankAccountsLists = new LinkedList<>();
        validBankAccountsLists.add(bankTransaction);

        AppUser mockAppUser = mock(AppUser.class);
        when(mockUserService.getSessionUser()).thenReturn(validUser);
        when(mockAppUser.getId()).thenReturn("validUser");
        when(mockBankDAO.findTransactionsByUserAccountId(validBankAccount.getBank_account_id())).thenReturn(validBankAccountsLists);

        List<BankTransaction> actualResult = but.getTransactionsByUserAccountId(validBankAccount);

        Assert.assertNotNull("Expected result to be true with valid bank account provided.", actualResult);
        verify(mockBankDAO, times(1)).findTransactionsByUserAccountId(validBankAccount.getBank_account_id());
    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_getTransactionsByUserAccountId_throwsResourcePersistenceException_givenValidBankAccount() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid", "valid");
        BankAccount validBankAccount = new BankAccount("valid", "valid");

        BankTransaction bankTransaction = new BankTransaction(2, validUser, validBankAccount);
        List<BankTransaction> validBankAccountsLists = new LinkedList<>();
        validBankAccountsLists.add(bankTransaction);

        AppUser mockAppUser = mock(AppUser.class);
        when(mockUserService.getSessionUser()).thenReturn(validUser);
        when(mockAppUser.getId()).thenReturn("validUser");
        when(mockBankDAO.findTransactionsByUserAccountId(validBankAccount.getBank_account_id())).thenReturn(null);

        try {
            List<BankTransaction> actualResult = but.getTransactionsByUserAccountId(validBankAccount);
        } finally {
            ;
        }
    }
}
