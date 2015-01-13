/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain.commands;

import com.google.common.base.Preconditions;

import java.util.Date;

/**
 * Used for handling 'activation' of the client command.
 */
public final class ActivateClientCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link ActivateClientCommand}
     */
    public static class Builder {

        private String locale;
        private String dateFormat;
        private Date activationDate;

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
         * Sets the activation date.
         * @param date the activate date
         * @return the current instance of {@link Builder}
         */
        public Builder activationDate(final Date date) {
            Preconditions.checkNotNull(date);

            this.activationDate = date;
            return this;
        }

        /**
         * Constructs a new ActivateClient instance with the provided parameter.
         * @return a new instance of {@link ActivateClientCommand}
         */
        public ActivateClientCommand build() {
            return new ActivateClientCommand(this.locale, this.dateFormat, this.activationDate);
        }

    }

    private String locale;
    private String dateFormat;
    private Date activationDate;

    private ActivateClientCommand(final String lang,
                                  final String format,
                                  final Date date) {
        this.locale = lang;
        this.dateFormat = format;
        this.activationDate = date;
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
     * Returns the activate date.
     */
    public Date getActivationDate() {
        return this.activationDate;
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
