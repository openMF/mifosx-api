package org.mifos.sdk.group.domain;

import org.mifos.sdk.internal.accounts.LoanAccount;
import org.mifos.sdk.internal.accounts.SavingsAccount;

import java.util.List;

public class GroupAccountsSummary {

    private List<LoanAccount> loanAccounts;
    private List<SavingsAccount> savingsAccounts;
    private List<LoanAccount> memberLoanAccounts;
    private List<SavingsAccount> memberSavingsAccounts;

    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    public List<SavingsAccount> getSavingsAccounts() {
        return savingsAccounts;
    }

    public List<LoanAccount> getMemberLoanAccounts() {
        return memberLoanAccounts;
    }

    public List<SavingsAccount> getMemberSavingsAccounts() {
        return memberSavingsAccounts;
    }
}
