/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain.commands;

import com.google.common.base.Preconditions;

import java.util.Date;

/**
 * Used for handling 'generate client sheet' of the group command.
 */
public class GenerateCollectionSheetCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link GenerateCollectionSheetCommand}
     */
    public static class Builder {

        private String dateFormat;
        private String locale;
        private Long calendarId;
        private Date transactionDate;

        private Builder(final String locale) {
            this.locale = locale;
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
         * Sets the calendar ID.
         * @param id the calendar ID
         * @return the current instance of {@link Builder}
         */
        public Builder calendarId(final Long id) {
            Preconditions.checkNotNull(id);
            this.calendarId = id;

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
         * Constructs a new GenerateCollectionSheetCommand instance with the provided parameters.
         * @return a new instance of {@link GenerateCollectionSheetCommand}
         */
        public GenerateCollectionSheetCommand build() {
            Preconditions.checkNotNull(this.dateFormat);
            Preconditions.checkNotNull(this.calendarId);
            Preconditions.checkNotNull(this.transactionDate);

            return new GenerateCollectionSheetCommand(this.locale, this.dateFormat,
                this.calendarId, this.transactionDate);
        }

    }

    private String dateFormat;
    private String locale;
    private Long calendarId;
    private Date transactionDate;

    private GenerateCollectionSheetCommand(final String locale,
                                           final String dateFormat,
                                           final Long calendarId,
                                           final Date transactionDate) {
        this.locale = locale;
        this.dateFormat = dateFormat;
        this.calendarId = calendarId;
        this.transactionDate = transactionDate;
    }

    /**
     * Returns the date format.
     */
    public String getDateFormat() {
        return this.dateFormat;
    }

    /**
     * Returns the locale.
     */
    public String getLocale() {
        return this.locale;
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
     * Sets the locale.
     * @param locale the locale
     * @return a new instance of {@link Builder}
     */
    public static Builder locale(final String locale) {
        Preconditions.checkNotNull(locale);
        Preconditions.checkArgument(!locale.isEmpty());

        return new Builder(locale);
    }

}
