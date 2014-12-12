/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.staff.domain;

import com.google.common.base.Preconditions;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.joda.time.LocalDate;
import org.mifos.sdk.internal.DateParser;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Staff {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link Staff}
     */
    public static class Builder {

        private Long officeId;
        private String firstName;
        private String lastName;
        private String isLoanOfficer;
        private String externalId;
        private String mobileNo;
        private String isActive;
        private String locale;
        private String dateFormat;
        private String joiningDate;

        private Builder(final Long id) {
            this.officeId = id;
        }

        /**
         * Sets the first name of the staff.
         * @param name the first name of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder firstName(final String name) {
            Preconditions.checkNotNull(name);
            Preconditions.checkArgument(!name.isEmpty());

            this.firstName = name;
            return this;
        }

        /**
         * Sets the last name of the staff.
         * @param name the last name of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder lastName(final String name) {
            Preconditions.checkNotNull(name);
            Preconditions.checkArgument(!name.isEmpty());

            this.lastName = name;
            return this;
        }

        /**
         * Optional method to set whether the staff is a loan officer.
         * @param bool "true" or "false" depending on whether the staff is a loan officer
         * @return instance of the current {@link Builder}
         */
        public Builder isLoanOfficer(final String bool) {
            this.isLoanOfficer = bool;
            return this;
        }

        /**
         * Optional method to set the external ID. Note that the
         * external ID should be unique.
         * @param id the external ID
         * @return instance of the current {@link Builder}
         */
        public Builder externalId(final String id) {
            Preconditions.checkArgument(id.length() <= 100);

            this.externalId = id;
            return this;
        }

        /**
         * Optional method to set the mobile number of the staff.
         * @param number the mobile number of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder mobileNo(final String number) {
            final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            try {
                phoneNumberUtil.parse(number, "");
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("invalid mobile number provided");
            }

            this.mobileNo = number;
            return this;
        }

        /**
         * Optional method to set whether the staff is active.
         * @param bool "true" or "false" depending on whether the staff is active
         * @return instance of the current {@link Builder}
         */
        public Builder isActive(final String bool) {
            this.isActive = bool;
            return this;
        }

        /**
         * Optional method to set the locale of the staff.
         * @param lang the locale of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder locale(final String lang) {
            this.locale = lang;
            return this;
        }

        /**
         * Optional method to set the date format.
         * @param format the date format
         * @return instance of the current {@link Builder}
         */
        public Builder dateFormat(final String format) {
            this.dateFormat = format;
            return this;
        }

        /**
         * Optional method to set the joining date of the staff.
         * @param date the joining date of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder joiningDate(final String date) {
            this.joiningDate = date;
            return this;
        }

        /**
         * Constructs a new Staff instance. Throws IllegalArgumentException if the
         * date, date format and/or the locale is/are invalid.
         * with the provided parameters.
         * @return a new instance of {@link Staff}
         */
        public Staff build() {
            Preconditions.checkNotNull(this.firstName);
            Preconditions.checkNotNull(this.lastName);
            if (this.joiningDate != null) {
                Preconditions.checkNotNull(this.dateFormat);
                Preconditions.checkNotNull(this.locale);

                DateParser.checkForValidDate(this.joiningDate, this.dateFormat, this.locale);
            }

            return new Staff(this.officeId, this.firstName, this.lastName, this.isLoanOfficer,
                    this.externalId, this.mobileNo, this.isActive, this.locale, this.dateFormat,
                    this.joiningDate);
        }

    }

    private Long officeId;
    private Long resourceId;
    private String firstname;
    private String lastname;
    private String isLoanOfficer;
    private String externalId;
    private String mobileNo;
    private String isActive;
    private String locale;
    private String dateFormat;
    private List<String> joiningDate;

    private Staff(final Long staffOfficeId,
                  final String staffFirstName,
                  final String staffLastName,
                  final String isStaffLoanOfficer,
                  final String staffExternalId,
                  final String staffMobileNo,
                  final String isStaffActive,
                  final String staffLocale,
                  final String staffDateFormat,
                  final String staffJoiningDate) {
        this.officeId = staffOfficeId;
        this.firstname = staffFirstName;
        this.lastname = staffLastName;
        this.isLoanOfficer = isStaffLoanOfficer;
        this.externalId = staffExternalId;
        this.mobileNo = staffMobileNo;
        this.isActive = isStaffActive;
        this.locale = staffLocale;
        this.dateFormat = staffDateFormat;
        this.joiningDate = Arrays.asList(staffJoiningDate);
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
     * Returns the first name of the staff.
     */
    public String getFirstName() {
        return this.firstname;
    }

    /**
     * Returns the last name of the staff.
     */
    public String getLastName() {
        return this.lastname;
    }

    /**
     * Returns whether the staff is a loan officer.
     */
    public String getIsLoanOfficer() {
        return this.isLoanOfficer;
    }

    /**
     * Returns the external ID.
     */
    public String getExternalId() {
        return this.externalId;
    }

    /**
     * Returns the mobile number of the staff.
     */
    public String getMobileNo() {
        return this.mobileNo;
    }

    /**
     * Returns whether the staff is active.
     */
    public String getIsActive() {
        return this.isActive;
    }

    /**
     * Returns the locale of the staff.
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
     * Returns the joining date of the staff.
     */
    public String getJoiningDate() {
        if (this.joiningDate.size() == 1) {
            return this.joiningDate.get(0);
        } else {
            final LocalDate localDate = DateParser.parseFromList(this.joiningDate);
            return localDate.toString(this.dateFormat, new Locale(this.locale));
        }
    }

    /**
     * Sets the office ID.
     * @param id the office ID
     * @return a new instance of {@link Builder}
     */
    public static Builder officeId(final Long id) {
        Preconditions.checkNotNull(id);

        return new Builder(id);
    }

}
