/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.accounts;

/**
 * Interface for a savings account.
 */
public class SavingsAccount {

    private Long id;
    private String accountNo;
    private Long productId;
    private String productName;
    private StatusCode status;
    private Currency currency;
    private AccountType accountType;
    private String shortProductName;
    private Timeline timeline;
    private DepositType depositType;
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
     * Returns the currency.
     */
    public Currency getCurrency() {
        return this.currency;
    }

    /**
     * Returns the account type.
     */
    public AccountType getAccountType() {
        return this.accountType;
    }

    /**
     * Returns the short product name.
     */
    public String getShortProductName() {
        return this.shortProductName;
    }

    /**
     * Returns the timeline.
     */
    public Timeline getTimeline() {
        return this.timeline;
    }

    /**
     * Returns the deposit type.
     */
    public DepositType getDepositType() {
        return this.depositType;
    }

    /**
     * Returns the account balance.
     */
    public double getAccountBalance() {
        return this.accountBalance;
    }

    private class Currency {

        private String code;
        private String name;
        private Long decimalPlaces;
        private String displaySymbol;
        private String nameCode;
        private String displayLabel;

        /**
         * Returns the currency code.
         */
        public String getCode() {
            return this.code;
        }

        /**
         * Returns the currency name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns the decimal places.
         */
        public Long getDecimalPlaces() {
            return this.decimalPlaces;
        }

        /**
         * Returns the display symbol.
         */
        public String getDisplaySymbol() {
            return this.displaySymbol;
        }

        /**
         * Returns the name code.
         */
        public String getNameCode() {
            return this.nameCode;
        }

        /**
         * Returns the display label.
         */
        public String getDisplayLabel() {
            return this.displayLabel;
        }
    }

}
