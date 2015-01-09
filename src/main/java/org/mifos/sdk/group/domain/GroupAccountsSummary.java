/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain;

import org.mifos.sdk.internal.accounts.LoanAccount;
import org.mifos.sdk.internal.accounts.SavingsAccount;

import java.util.List;

/**
 * Holds group accounts summary related information.
 */
public final class GroupAccountsSummary {

    private List<LoanAccount> loanAccounts;
    private List<SavingsAccount> savingsAccounts;
    private List<LoanAccount> memberLoanAccounts;
    private List<SavingsAccount> memberSavingsAccounts;

    /**
     * Returns the list of loan accounts.
     */
    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    /**
     * Returns the list of savings accounts.
     */
    public List<SavingsAccount> getSavingsAccounts() {
        return savingsAccounts;
    }

    /**
     * Returns the list of member loan accounts.
     */
    public List<LoanAccount> getMemberLoanAccounts() {
        return memberLoanAccounts;
    }

    /**
     * Returns the list of member savings accounts.
     */
    public List<SavingsAccount> getMemberSavingsAccounts() {
        return memberSavingsAccounts;
    }
}
