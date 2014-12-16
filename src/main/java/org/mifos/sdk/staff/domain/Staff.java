/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.staff.domain;

import com.google.common.base.Preconditions;

import java.util.Date;

public class Staff {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link Staff}
     */
    public static class Builder {

        private Long officeId;
        private String firstname;
        private String lastname;
        private boolean isLoanOfficer;
        private String externalId;
        private String mobileNo;
        private boolean isActive;
        private String locale;
        private String dateFormat;
        private Date joiningDate;

        private Builder(final Long id) {
            this.officeId = id;
        }

        /**
         * Sets the first name of the staff.
         * @param name the first name of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder firstname(final String name) {
            this.firstname = name;
            return this;
        }

        /**
         * Sets the last name of the staff.
         * @param name the last name of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder lastname(final String name) {
            this.lastname = name;
            return this;
        }

        /**
         * Optional method to set whether the staff is a loan officer.
         * @param loanOfficer true or false depending on whether the staff is a loan officer
         * @return instance of the current {@link Builder}
         */
        public Builder isLoanOfficer(final boolean loanOfficer) {
            this.isLoanOfficer = loanOfficer;
            return this;
        }

        /**
         * Optional method to set the external ID. Note that the
         * external ID should be unique.
         * @param id the external ID
         * @return instance of the current {@link Builder}
         */
        public Builder externalId(final String id) {
            this.externalId = id;
            return this;
        }

        /**
         * Optional method to set the mobile number of the staff. Throws
         * IllegalArgumentException if the mobile number is invalid.
         * @param number the mobile number of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder mobileNo(final String number) {
            this.mobileNo = number;
            return this;
        }

        /**
         * Optional method to set whether the staff is active.
         * @param active true or false depending on whether the staff is active
         * @return instance of the current {@link Builder}
         */
        public Builder isActive(final boolean active) {
            this.isActive = active;
            return this;
        }

        /**
         * Optional method to set the locale of the staff.
         * @param lang the locale of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder locale(final String lang) {
            Preconditions.checkNotNull(lang);
            Preconditions.checkArgument(!lang.isEmpty());

            this.locale = lang;
            return this;
        }

        /**
         * Optional method to set the date format.
         * @param format the date format
         * @return instance of the current {@link Builder}
         */
        public Builder dateFormat(final String format) {
            Preconditions.checkNotNull(format);
            Preconditions.checkArgument(!format.isEmpty());

            this.dateFormat = format;
            return this;
        }

        /**
         * Optional method to set the joining date of the staff.
         * @param date the joining date of the staff
         * @return instance of the current {@link Builder}
         */
        public Builder joiningDate(final Date date) {
            this.joiningDate = date;
            return this;
        }

        /**
         * Constructs a new Staff instance with the provided parameters.
         * @return a new instance of {@link Staff}
         */
        public Staff build() {
            return new Staff(this.officeId, this.firstname, this.lastname, this.isLoanOfficer,
                    this.externalId, this.mobileNo, this.isActive, this.locale, this.dateFormat,
                    this.joiningDate);
        }

    }

    private Long officeId;
    private Long resourceId;
    private String firstname;
    private String lastname;
    private boolean isLoanOfficer;
    private String externalId;
    private String mobileNo;
    private boolean isActive;
    private String locale;
    private String dateFormat;
    private Date joiningDate;
    private String displayName;
    private String officeName;

    private Staff(final Long staffOfficeId,
                  final String staffFirstName,
                  final String staffLastName,
                  final boolean isStaffLoanOfficer,
                  final String staffExternalId,
                  final String staffMobileNo,
                  final boolean isStaffActive,
                  final String staffLocale,
                  final String staffDateFormat,
                  final Date staffJoiningDate) {
        this.officeId = staffOfficeId;
        this.firstname = staffFirstName;
        this.lastname = staffLastName;
        this.isLoanOfficer = isStaffLoanOfficer;
        this.externalId = staffExternalId;
        this.mobileNo = staffMobileNo;
        this.isActive = isStaffActive;
        this.locale = staffLocale;
        this.dateFormat = staffDateFormat;
        this.joiningDate = staffJoiningDate;
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
    public String getFirstname() {
        return this.firstname;
    }

    /**
     * Returns the last name of the staff.
     */
    public String getLastname() {
        return this.lastname;
    }

    /**
     * Returns whether the staff is a loan officer.
     */
    public boolean getIsLoanOfficer() {
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
    public boolean getIsActive() {
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
    public Date getJoiningDate() {
        return this.joiningDate;
    }

    /**
     * Returns the display name.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Returns the office name.
     */
    public String getOfficeName() {
        return this.officeName;
    }

    /**
     * Sets the resource ID.
     * @param id the resource ID
     */
    public void setResourceId(final Long id) {
        this.resourceId = id;
    }

    /**
     * Sets the office name.
     * @param name the office name
     */
    public void setOfficeName(final String name) {
        this.officeName = name;
    }

    /**
     * Sets the display name.
     * @param name the display name
     */
    public void setDisplayName(final String name) {
        this.displayName = name;
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
