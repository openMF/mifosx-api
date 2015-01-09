/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain.commands;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Used for handling 'save collection sheet' of the group command.
 */
public final class SaveCollectionSheetCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link SaveCollectionSheetCommand}
     */
    public static class Builder {

        private Long calendarId;
        private Date transactionDate;
        private Date actualDisbursementDate;
        private String locale;
        private String dateFormat;
        private List<ClientAttendance> clientsAttendance;
        private List<BulkRepaymentTransaction> bulkRepaymentTransactions;
        private List<BulkDisbursementTransaction> bulkDisbursementTransactions;

        private Builder(final Long calendarId) {
            this.calendarId = calendarId;
        }

        /**
         * Sets the locale.
         * @param locale the locale
         * @return the current instance of {@link Builder}
         */
        public Builder locale(final String locale) {
            Preconditions.checkNotNull(locale);
            Preconditions.checkArgument(!locale.isEmpty());
            this.locale = locale;

            return this;
        }

        /**
         * Sets the date format.
         * @param format the format
         * @return the current instance of {@link Builder}
         */
        public Builder dateFormat(final String format) {
            Preconditions.checkNotNull(format);
            Preconditions.checkArgument(!format.isEmpty());
            this.dateFormat = format;

            return this;
        }

        /**
         * Sets the transaction date.
         * @param date the date
         * @return the current instance of {@link Builder}
         */
        public Builder transactionDate(final Date date) {
            Preconditions.checkNotNull(date);
            this.transactionDate = date;

            return this;
        }

        /**
         * Sets the actual disbursement date.
         * @param date the date
         * @return the current instance of {@link Builder}
         */
        public Builder actualDisbursementDate(final Date date) {
            Preconditions.checkNotNull(date);
            this.actualDisbursementDate = date;

            return this;
        }

        /**
         * Sets the client attendance list.
         * @param clientsAttendance the clients attendance
         * @return the current instance of {@link Builder}
         */
        public Builder clientsAttendance(final List<ClientAttendance> clientsAttendance) {
            Preconditions.checkNotNull(clientsAttendance);
            this.clientsAttendance = clientsAttendance;

            return this;
        }

        /**
         * Sets the bulk repayment transactions.
         * @param bulkRepaymentTransactions the transactions
         * @return the current instance of {@link Builder}
         */
        public Builder bulkRepaymentTransactions(final List<BulkRepaymentTransaction> bulkRepaymentTransactions) {
            Preconditions.checkNotNull(bulkRepaymentTransactions);
            this.bulkRepaymentTransactions = bulkRepaymentTransactions;

            return this;
        }

        /**
         * Sets the bulk disbursemement transactions.
         * @param bulkDisbursementTransactions the transactions
         * @return the current instance of {@link Builder}
         */
        public Builder bulkDisbursementTransactions(final List<BulkDisbursementTransaction> bulkDisbursementTransactions) {
            Preconditions.checkNotNull(bulkDisbursementTransactions);
            this.bulkDisbursementTransactions = bulkDisbursementTransactions;

            return this;
        }

        /**
         * Constructs a new SaveCollectionSheetCommand instance with the provided parameters.
         * @return a new instance of {@link SaveCollectionSheetCommand}
         */
        public SaveCollectionSheetCommand build() {
            Preconditions.checkNotNull(this.calendarId);
            Preconditions.checkNotNull(this.transactionDate);
            Preconditions.checkNotNull(this.actualDisbursementDate);
            Preconditions.checkNotNull(this.locale);
            Preconditions.checkNotNull(this.dateFormat);

            return new SaveCollectionSheetCommand(this.calendarId, this.transactionDate,
                this.actualDisbursementDate, this.locale, this.dateFormat, this.clientsAttendance,
                this.bulkRepaymentTransactions, this.bulkDisbursementTransactions);
        }

    }

    private Long calendarId;
    private Date transactionDate;
    private Date actualDisbursementDate;
    private String locale;
    private String dateFormat;
    private List<ClientAttendance> clientsAttendance;
    private List<BulkRepaymentTransaction> bulkRepaymentTransactions;
    private List<BulkDisbursementTransaction> bulkDisbursementTransactions;

    private SaveCollectionSheetCommand(final Long calendarId,
                                       final Date transactionDate,
                                       final Date actualDisbursementDate,
                                       final String locale,
                                       final String dateFormat,
                                       final List<ClientAttendance> clientsAttendance,
                                       final List<BulkRepaymentTransaction> bulkRepaymentTransactions,
                                       final List<BulkDisbursementTransaction> bulkDisbursementTransactions) {
        this.calendarId = calendarId;
        this.transactionDate = transactionDate;
        this.actualDisbursementDate = actualDisbursementDate;
        this.locale = locale;
        this.dateFormat = dateFormat;
        this.clientsAttendance = clientsAttendance;
        this.bulkRepaymentTransactions = bulkRepaymentTransactions;
        this.bulkDisbursementTransactions = bulkDisbursementTransactions;
    }

    /**
     * Returns the calendar ID.
     */
    public Long getCalendarId() {
        return this.calendarId;
    }

    /**
     * Returns the transaction date.
     */
    public Date getTransactionDate() {
        return this.transactionDate;
    }

    /**
     * Returns the actual disbursement date.
     */
    public Date getActualDisbursementDate() {
        return this.actualDisbursementDate;
    }

    /**
     * Returns the locale.
     */
    public String getLocale() {
        return this.locale;
    }

    /**
     * Returns the date format.
     */
    public String getDateFormat() {
        return this.dateFormat;
    }

    /**
     * Returns the clients attendance.
     */
    public List<ClientAttendance> getClientsAttendance() {
        return this.clientsAttendance;
    }

    /**
     * Returns the bulk repayment transactions.
     */
    public List<BulkRepaymentTransaction> getBulkRepaymentTransactions() {
        return this.bulkRepaymentTransactions;
    }

    /**
     * Returns the bulk disbursement transactions.
     */
    public List<BulkDisbursementTransaction> getBulkDisbursementTransactions() {
        return this.bulkDisbursementTransactions;
    }

    /**
     * Sets the calendar ID.
     * @param id the calendar ID
     * @return a new instance of {@link Builder}
     */
    public static Builder calendarId(final Long id) {
        Preconditions.checkNotNull(id);

        return new Builder(id);
    }

    /**
     * Holds client attendance related information.
     */
    public class ClientAttendance {

        private Long clientId;
        private Long attendanceType;

        /**
         * Returns the client ID.
         */
        public Long getClientId() {
            return this.clientId;
        }

        /**
         * Sets the client ID.
         * @param clientId the client ID
         */
        public void setClientId(final Long clientId) {
            this.clientId = clientId;
        }

        /**
         * Returns the attendance type.
         */
        public Long getAttendanceType() {
            return this.attendanceType;
        }

        /**
         * Sets the attendance type.
         * @param attendanceType the attendance type
         */
        public void setAttendanceType(final Long attendanceType) {
            this.attendanceType = attendanceType;
        }

    }

    /**
     * Holds bulk repayment transaction related information.
     */
    public class BulkRepaymentTransaction {

        private Long loanId;
        private BigDecimal transactionAmount;

        /**
         * Returns the loan ID.
         */
        public Long getLoanId() {
            return this.loanId;
        }

        /**
         * Sets the loan ID.
         * @param loanId the loan ID
         */
        public void setLoanId(final Long loanId) {
            this.loanId = loanId;
        }

        /**
         * Returns the transaction amount.
         */
        public BigDecimal getTransactionAmount() {
            return this.transactionAmount;
        }

        /**
         * Sets the transaction amount.
         * @param transactionAmount the amount
         */
        public void setTransactionAmount(final BigDecimal transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

    }

    /**
     * Holds the bulk disbursement transaction related information.
     */
    public class BulkDisbursementTransaction {

        private Long loanId;
        private BigDecimal transactionAmount;

        /**
         * Returns the loan ID.
         */
        public Long getLoanId() {
            return this.loanId;
        }

        /**
         * Sets the loan ID.
         * @param loanId the loan ID
         */
        public void setLoanId(final Long loanId) {
            this.loanId = loanId;
        }

        /**
         * Returns the transaction amount.
         */
        public BigDecimal getTransactionAmount() {
            return this.transactionAmount;
        }

        /**
         * Sets the transaction amount.
         * @param transactionAmount the amount
         */
        public void setTransactionAmount(final BigDecimal transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

    }

}
