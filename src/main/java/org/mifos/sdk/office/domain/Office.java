/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.office.domain;

import com.google.common.base.Preconditions;
import org.joda.time.LocalDate;
import org.mifos.sdk.internal.DateParser;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Gives an interface to communicate with the Office API.
 */
public class Office {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link Office}
     */
    public static class Builder {

        private String name;
        private String nameDecorated;
        private String dateFormat;
        private String locale;
        private String openingDate;
        private Long parentId;
        private String externalId;

        private Builder(final String officeName) {
            this.name = officeName;
        }

        /**
         * Optional method to set the decorated name of the office.
         * @param officeNameDecorated the decorated name of the office
         * @return instance of the current {@link Builder}
         */
        public Builder nameDecorated(final String officeNameDecorated) {
            this.nameDecorated = officeNameDecorated;
            return this;
        }

        /**
         * Sets the date format, cannot be null.
         * @param format the date format
         * @return instance of the current {@link Builder}
         */
        public Builder dateFormat(final String format) {
            this.dateFormat = format;
            return this;
        }

        /**
         * Sets the locale, cannot be null.
         * @param lang the locale, 'en' for instance
         * @return instance of the current {@link Builder}
         */
        public Builder locale(final String lang) {
            this.locale = lang;
            return this;
        }

        /**
         * Sets the opening date of the office, cannot be null.
         * @param date the opening date
         * @return instance of the current {@link Builder}
         */
        public Builder openingDate(final String date) {
            Preconditions.checkNotNull(date);
            Preconditions.checkArgument(!date.isEmpty());

            this.openingDate = date;
            return this;
        }

        /**
         * Sets the parent ID of the office, cannot be null.
         * @param id the parent ID
         * @return instance of the current {@link Builder}
         */
        public Builder parentId(final Long id) {
            Preconditions.checkNotNull(id);

            this.parentId = id;
            return this;
        }

        /**
         * Optional method to set the external ID. Note that the
         * external ID should be unique.
         * @param id the unique external ID.
         * @return instance of the current {@link Builder}
         */
        public Builder externalId(final String id) {
            Preconditions.checkArgument(id.length() <= 100);

            this.externalId = id;
            return this;
        }

        /**
         * Constructs a new Office instance. Throws IllegalArgumentException if the
         * date, date format and/or the locale is/are invalid.
         * with the provided parameters.
         * @return a new instance of {@link Office}
         */
        public Office build() {
            Preconditions.checkNotNull(this.name);
            Preconditions.checkNotNull(this.parentId);
            Preconditions.checkNotNull(this.openingDate);
            Preconditions.checkNotNull(this.dateFormat);
            Preconditions.checkNotNull(this.locale);

            DateParser.checkForValidDate(this.openingDate, this.dateFormat, this.locale);

            return new Office(this.name, this.nameDecorated, this.dateFormat,
                              this.locale, this.openingDate, this.parentId, this.externalId);
        }

    }

    private Long officeId;
    private Long resourceId;
    private String name;
    private String nameDecorated;
    private String dateFormat;
    private String locale;
    //private String openingDate;
    private List<String> openingDate;
    private Long parentId;
    private String externalId;

    private Office(final String officeName, final String officeNameDecorated,
                   final String format, final String lang, final String date,
                   final Long officeParentId, final String officeExternalId) {
        this.name = officeName;
        this.nameDecorated = officeNameDecorated;
        this.dateFormat = format;
        this.locale = lang;
        this.openingDate = Arrays.asList(date);
        this.parentId = officeParentId;
        this.externalId = officeExternalId;
    }

    /**
     * Returns the office ID.
     */
    public Long getOfficeId() {
        return this.officeId;
    }

    /**
     * Returns the resource ID.
     */
    public Long getResourceId() {
        return this.resourceId;
    }

    /**
     * Returns the name of the office.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the decorated name of the office.
     */
    public String getNameDecorated() {
        return this.nameDecorated;
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
     * Returns the opening date.
     */
    public String getOpeningDate() {
        if (this.openingDate.size() == 1) {
            return this.openingDate.get(0);
        } else {
            final LocalDate localDate = DateParser.parseFromList(this.openingDate);
            return localDate.toString(this.dateFormat, new Locale(this.locale));
        }
    }

    /**
     * Returns the parent ID.
     */
    public Long getParentId() {
        return this.parentId;
    }

    /**
     * Returns the external ID.
     */
    public String getExternalId() {
        return this.externalId;
    }

    /**
     * Sets the name of the office, cannot be null or empty. Note
     * that the name cannot exceed 100 characters in length.
     * @param officeName the name of the office
     * @return a new instance of {@link Builder}
     */
    public static Builder name(final String officeName) {
        Preconditions.checkNotNull(officeName);
        Preconditions.checkArgument(officeName.length() <= 100);
        Preconditions.checkArgument(!officeName.isEmpty());

        return new Builder(officeName);
    }

}
