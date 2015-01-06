/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain.commands;

import com.google.common.base.Preconditions;

import java.util.Date;

/**
 * Used for handling 'closing' of the group command.
 */
public class CloseGroupCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link CloseGroupCommand}
     */
    public static class Builder {

        private String locale;
        private String dateFormat;
        private Date closureDate;
        private String closureReasonId;

        private Builder(final String lang) {
            this.locale = lang;
        }

        /**
         * Sets the date format.
         * @param format the date format.
         * @return the current instance of {@link Builder}
         */
        public Builder dateFormat(final String format) {
            Preconditions.checkNotNull(format);
            Preconditions.checkArgument(!format.isEmpty());

            this.dateFormat = format;
            return this;
        }

        /**
         * Sets the closure date.
         * @param date the closure date
         * @return the current instance of {@link Builder}
         */
        public Builder closureDate(final Date date) {
            Preconditions.checkNotNull(date);

            this.closureDate = date;
            return this;
        }

        /**
         * Sets the closure reason ID.
         * @param id the closure reason ID
         * @return the current instance of {@link Builder}
         */
        public Builder closureReasonId(final String id) {
            Preconditions.checkNotNull(id);
            Preconditions.checkArgument(!id.isEmpty());

            this.closureReasonId = id;
            return this;
        }

        /**
         * Constructs a new CloseClient instance with the provided parameter.
         * @return a new instance of {@link CloseGroupCommand}
         */
        public CloseGroupCommand build() {
            return new CloseGroupCommand(this.locale, this.dateFormat,
                this.closureDate, this.closureReasonId);
        }

    }

    private String locale;
    private String dateFormat;
    private Date closureDate;
    private String closureReasonId;

    private CloseGroupCommand(final String lang,
                               final String format,
                               final Date date,
                               final String closureReason) {
        this.locale = lang;
        this.dateFormat = format;
        this.closureDate = date;
        this.closureReasonId = closureReason;
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
     * Returns the closure date.
     */
    public Date getClosureDate() {
        return this.closureDate;
    }

    /**
     * Returns the closure reason ID.
     */
    public String getClosureReasonId() {
        return this.closureReasonId;
    }

    /**
     * Sets the locale.
     * @param lang the locale
     * @return a new instance of {@link Builder}
     */
    public static Builder locale(final String lang) {
        Preconditions.checkNotNull(lang);
        Preconditions.checkArgument(!lang.isEmpty());

        return new Builder(lang);
    }

}
