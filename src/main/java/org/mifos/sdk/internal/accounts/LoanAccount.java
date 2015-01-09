/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.accounts;

/**
 * Interface for a loan account.
 */
public final class LoanAccount {

    private Long id;
    private String accountNo;
    private Long productId;
    private String shortProductName;
    private String productName;
    private StatusCode status;
    private LoanType loanType;
    private AccountType accountType;
    private DepositType depositType;
    private Timeline timeline;
    private boolean inArrears;
    private double originalLoan;
    private double loanBalance;
    private Long loanCycle;
    private double accountBalance;

    /**
     * Returns the ID.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Returns the account number.
     */
    public String getAccountNo() {
        return this.accountNo;
    }

    /**
     * Returns the product ID.
     */
    public Long getProductId() {
        return this.productId;
    }

    /**
     * Returns the product name.
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * Returns the status.
     */
    public StatusCode getStatus() {
        return this.status;
    }

    /**
     * Returns the loan type.
     */
    public LoanType getLoanType() {
        return this.loanType;
    }

    /**
     * Returns the timeline.
     */
    public Timeline getTimeline() {
        return this.timeline;
    }

    /**
     * Returns true if loan is in arrears, false otherwise.
     */
    public boolean isInArrears() {
        return this.inArrears;
    }

    /**
     * Returns the original loan.
     */
    public double getOriginalLoan() {
        return this.originalLoan;
    }

    /**
     * Returns the short product name.
     */
    public String getShortProductName() {
        return this.shortProductName;
    }

    /**
     * Returns the loan balance.
     */
    public double getLoanBalance() {
        return this.loanBalance;
    }

    /**
     * Returns the loan cycle.
     */
    public Long getLoanCycle() {
        return this.loanCycle;
    }

    /**
     * Returns the account type.
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Returns the deposit type.
     */
    public DepositType getDepositType() {
        return depositType;
    }

    /**
     * Returns the account balance.
     */
    public double getAccountBalance() {
        return accountBalance;
    }

}
