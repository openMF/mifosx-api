/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain;

import com.google.common.base.Preconditions;

import java.util.Date;

/**
 * Gives an interface to communicate with the Client API.
 */
public class Client {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link Client}
     */
    public static class Builder {

        private String fullname;
        private String firstname;
        private String middlename;
        private String lastname;
        private Long officeId;
        private boolean active;
        private Date activationDate;
        private String dateFormat;
        private String locale;
        private Long groupId;
        private String externalId;
        private String accountNo;
        private Long staffId;
        private String mobileNo;
        private Long savingsProductId;
        private Long genderId;
        private Long clientTypeId;
        private Long clientClassificationId;
        private Date submittedOnDate;

        private Builder(final String name, final boolean fullname) {
            if (fullname) {
                this.fullname = name;
            } else {
                this.firstname = name;
            }
        }

        /**
         * Optional method to set the middle name of the client.
         * @param name the middle name of the client
         * @return instance of the current {@link Builder}
         */
        public Builder middlename(final String name) {
            if (this.fullname != null) {
                throw new IllegalArgumentException("full name supplied already, no middle name required");
            }

            this.middlename = name;
            return this;
        }

        /**
         * Sets the last name of the client.
         * @param name the decorated name of the office
         * @return instance of the current {@link Builder}
         */
        public Builder lastname(final String name) {
            if (this.fullname != null) {
                throw new IllegalArgumentException("full name supplied already, no last name required");
            }

            this.lastname = name;
            return this;
        }

        /**
         * Sets the office ID.
         * @param id the office ID
         * @return instance of the current {@link Builder}
         */
        public Builder officeId(final Long id) {
            this.officeId = id;
            return this;
        }

        /**
         * Sets whether client is active or not.
         * @param isActive true or false depending on whether client is active or not
         * @return instance of the current {@link Builder}
         */
        public Builder active(final boolean isActive) {
            this.active = isActive;
            return this;
        }

        /**
         * Sets the activation date.
         * @param date the activation date
         * @return instance of the current {@link Builder}
         */
        public Builder activationDate(final Date date) {
            this.activationDate = date;
            return this;
        }

        /**
         * Sets the date format.
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
         * Sets the locale.
         * @param lang the locale
         * @return instance of the current {@link Builder}
         */
        public Builder locale(final String lang) {
            Preconditions.checkNotNull(lang);
            Preconditions.checkArgument(!lang.isEmpty());

            this.locale = lang;
            return this;
        }

        /**
         * Sets the group ID.
         * @param id the group ID
         * @return instance of the current {@link Builder}
         */
        public Builder groupId(final Long id) {
            this.groupId = id;
            return this;
        }

        /**
         * Optional method to set the external ID. Note that the
         * external ID should be unique.
         * @param id the unique external ID.
         * @return instance of the current {@link Builder}
         */
        public Builder externalId(final String id) {
            this.externalId = id;
            return this;
        }

        /**
         * Sets the account number.
         * @param id the account number
         * @return instance of the current {@link Builder}
         */
        public Builder accountNo(final String id) {
            this.accountNo = id;
            return this;
        }

        /**
         * Sets the staff ID.
         * @param id the staff ID.
         * @return instance of the current {@link Builder}
         */
        public Builder staffId(final Long id) {
            this.staffId = id;
            return this;
        }

        /**
         * Sets the mobile number of the client.
         * @param number the mobile number
         * @return instance of the current {@link Builder}
         */
        public Builder mobileNo(final String number) {
            this.mobileNo = number;
            return this;
        }

        /**
         * Sets the savings product ID.
         * @param id the savings product ID
         * @return instance of the current {@link Builder}
         */
        public Builder savingsProductId(final Long id) {
            this.savingsProductId = id;
            return this;
        }

        /**
         * Sets the gender ID.
         * @param id the gender ID
         * @return instance of the current {@link Builder}
         */
        public Builder genderId(final Long id) {
            this.genderId = id;
            return this;
        }

        /**
         * Sets the client type ID.
         * @param id the client type ID.
         * @return instance of the current {@link Builder}
         */
        public Builder clientTypeId(final Long id) {
            this.clientTypeId = id;
            return this;
        }

        /**
         * Sets the client classification ID.
         * @param id the client classification ID
         * @return instance of the current {@link Builder}
         */
        public Builder clientClassificationId(final Long id) {
            this.clientClassificationId = id;
            return this;
        }

        /**
         * Sets the submitted on date.
         * @param date the submitted on date
         * @return instance of the current {@link Builder}
         */
        public Builder submittedOnDate(final Date date) {
            this.submittedOnDate = date;
            return this;
        }

        /**
         * Constructs a new Client instance with the provided parameters.
         * @return a new instance of {@link Client}
         */
        public Client build() {
            Preconditions.checkNotNull(this.officeId);

            return new Client(this.fullname, this.firstname, this.middlename, this.lastname,
                    this.officeId, this.active, this.activationDate, this.dateFormat, this.locale,
                    this.groupId, this.externalId, this.accountNo, this.staffId, this.mobileNo,
                    this.savingsProductId, this.genderId, this.clientTypeId,
                    this.clientClassificationId, this.submittedOnDate);
        }

    }

    private String fullname;
    private String firstname;
    private String middlename;
    private String lastname;
    private Long officeId;
    private boolean active;
    private Date activationDate;
    private String dateFormat;
    private String locale;
    private Long groupId;
    private String externalId;
    private String accountNo;
    private Long staffId;
    private String mobileNo;
    private Long savingsProductId;
    private Long genderId;
    private Long clientTypeId;
    private Long clientClassificationId;
    private Long clientId;
    private Long resourceId;
    private String displayName;
    private String closedByUsername;
    private String closedByFirstname;
    private String closedByLastname;
    private Date submittedOnDate;
    private Date closingDate;
    private Long imageId;
    private boolean imagePresent;
    private String genderName;
    private String clientTypeName;
    private Long statusId;
    private String statusCode;
    private String statusValue;
    private String staffName;
    private String officeName;
    private Long savingsAccountId;
    private Long savingsId;

    private Client(final String clientFullname,
                   final String clientFirstname,
                   final String clientMiddlename,
                   final String clientLastname,
                   final Long clientOfficeId,
                   final boolean isActive,
                   final Date clientActivationDate,
                   final String activationDateFormat,
                   final String lang,
                   final Long clientGroupId,
                   final String clientExternalId,
                   final String clientAccountNo,
                   final Long clientStaffId,
                   final String clientMobileNo,
                   final Long clientSavingsProductId,
                   final Long clientGenderId,
                   final Long clientTypeId,
                   final Long clientClassificationId,
                   final Date clientSubmittedOnDate) {
        this.fullname = clientFullname;
        this.firstname = clientFirstname;
        this.middlename = clientMiddlename;
        this.lastname = clientLastname;
        this.officeId = clientOfficeId;
        this.active = isActive;
        this.activationDate = clientActivationDate;
        this.dateFormat = activationDateFormat;
        this.locale = lang;
        this.groupId = clientGroupId;
        this.externalId = clientExternalId;
        this.accountNo = clientAccountNo;
        this.staffId = clientStaffId;
        this.mobileNo = clientMobileNo;
        this.savingsProductId = clientSavingsProductId;
        this.genderId = clientGenderId;
        this.clientTypeId = clientTypeId;
        this.clientClassificationId = clientClassificationId;
        this.submittedOnDate = clientSubmittedOnDate;
    }

    /**
     * Returns the full name of the client.
     */
    public String getFullname() {
        return this.fullname;
    }

    /**
     * Returns the first name of the client.
     */
    public String getFirstname() {
        if (this.firstname != null) {
            return this.firstname;
        }

        return this.fullname.split(" ")[0];
    }

    /**
     * Returns the middle name of the client.
     */
    public String getMiddlename() {
        if (this.fullname != null) {
            String[] names = this.fullname.split(" ");
            if (names.length == 3) {
                // contains a middle name
                return names[1];
            }
        }

        return this.middlename;
    }

    /**
     * Returns the last name of the client.
     */
    public String getLastname() {
        if (this.fullname != null) {
            String[] names = this.fullname.split(" ");
            if (names.length > 1) {
                // contains a last name
                if (names.length == 2) {
                    return names[1];
                } else {
                    return names[2];
                }
            }
        }

        return this.lastname;
    }

    /**
     * Returns the office ID.
     */
    public Long getOfficeId() {
        return this.officeId;
    }

    /**
     * Returns whether the client is active or not.
     */
    public boolean getActive() {
        return this.active;
    }

    /**
     * Returns the activation date.
     */
    public Date getActivationDate() {
        return this.activationDate;
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
     * Returns the group ID.
     */
    public Long getGroupId() {
        return this.groupId;
    }

    /**
     * Returns the external ID of the client.
     */
    public String getExternalId() {
        return this.externalId;
    }

    /**
     * Returns the account number of the client.
     */
    public String getAccountNo() {
        return this.accountNo;
    }

    /**
     * Returns the staff ID.
     */
    public Long getStaffId() {
        return this.staffId;
    }

    /**
     * Returns the mobile number of the client.
     */
    public String getMobileNo() {
        return this.mobileNo;
    }

    /**
     * Returns the savings product ID.
     */
    public Long getSavingsProductId() {
        return this.savingsProductId;
    }

    /**
     * Returns the gender ID.
     */
    public Long getGenderId() {
        return this.genderId;
    }

    /**
     * Returns the client type ID.
     */
    public Long getClientTypeId() {
        return this.clientTypeId;
    }

    /**
     * Returns the client classification ID.
     */
    public Long getClientClassificationId() {
        return this.clientClassificationId;
    }

    /**
     * Returns the client ID.
     */
    public Long getClientId() {
        return this.clientId;
    }

    /**
     * Returns the resource ID.
     */
    public Long getResourceId() {
        return this.resourceId;
    }

    /**
     * Returns the display name.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Returns the closed by username.
     */
    public String getClosedByUsername() {
        return this.closedByUsername;
    }

    /**
     * Returns the closed by first name.
     */
    public String getClosedByFirstname() {
        return this.closedByFirstname;
    }

    /**
     * Returns the closed by last name.
     */
    public String getClosedByLastname() {
        return this.closedByLastname;
    }

    /**
     * Returns the submitted on date.
     */
    public Date getSubmittedOnDate() {
        return this.submittedOnDate;
    }

    /**
     * Returns the closing date.
     */
    public Date getClosingDate() {
        return this.closingDate;
    }

    /**
     * Returns the gender name.
     */
    public String getGenderName() {
        return this.genderName;
    }

    /**
     * Returns the client type name.
     */
    public String getClientTypeName() {
        return this.clientTypeName;
    }

    /**
     * Returns the image ID.
     */
    public Long getImageId() {
        return this.imageId;
    }

    /**
     * Returns whether the image is present or not.
     */
    public boolean getImagePresent() {
        return this.imagePresent;
    }

    /**
     * Returns the statud ID.
     */
    public Long getStatusId() {
        return this.statusId;
    }

    /**
     * Returns the status code.
     */
    public String getStatusCode() {
        return this.statusCode;
    }

    /**
     * Returns the status value.
     */
    public String getStatusValue() {
        return this.statusValue;
    }

    /**
     * Returns the staff name.
     */
    public String getStaffName() {
        return this.staffName;
    }

    /**
     * Returns the office name.
     */
    public String getOfficeName() {
        return this.officeName;
    }

    /**
     * Returns the savings account ID.
     */
    public Long getSavingsAccountId() {
        return this.savingsAccountId;
    }

    /**
     * Returns the savings ID.
     */
    public Long getSavingsId() {
        return this.savingsId;
    }

    /**
     * Sets the client ID.
     * @param id the client ID
     */
    public void setClientId(final Long id) {
        this.clientId = id;
    }

    /**
     * Sets the resource ID.
     * @param id the resource ID
     */
    public void setResourceId(final Long id) {
        this.resourceId = id;
    }

    /**
     * Sets the display name.
     * @param name the display name
     */
    public void setDisplayName(final String name) {
        this.displayName = name;
    }

    /**
     * Sets the closed by username.
     * @param name the username
     */
    public void setClosedByUsername(final String name) {
        this.closedByUsername = name;
    }

    /**
     * Sets the closed by first name.
     * @param name the first name
     */
    public void setClosedByFirstname(final String name) {
        this.closedByFirstname = name;
    }

    /**
     * Sets the closed by last name.
     * @param name the last name
     */
    public void setClosedByLastname(final String name) {
        this.closedByLastname = name;
    }

    /**
     * Sets the closing date.
     * @param date the closing date
     */
    public void setClosingDate(final Date date) {
        this.closingDate = date;
    }

    /**
     * Sets the gender name.
     * @param name the gender name
     */
    public void setGenderName(final String name) {
        this.genderName = name;
    }

    /**
     * Sets the client type name.
     * @param name the client type name
     */
    public void setClientTypeName(final String name) {
        this.clientTypeName = name;
    }

    /**
     * Sets the image ID.
     * @param id the image ID
     */
    public void setImageId(final Long id) {
        this.imageId = id;
    }

    /**
     * Sets whether image is present or not.
     * @param present true or false depending on the presence of the image
     */
    public void setImagePresent(final boolean present) {
        this.imagePresent = present;
    }

    /**
     * Sets the status ID.
     * @param id the status ID.
     */
    public void setStatusId(final Long id) {
        this.statusId = id;
    }

    /**
     * Sets the status code.
     * @param code the status code.
     */
    public void setStatusCode(final String code) {
        this.statusCode = code;
    }

    /**
     * Sets the status value.
     * @param value the status value
     */
    public void setStatusValue(final String value) {
        this.statusValue = value;
    }

    /**
     * Sets the staff name.
     * @param name the staff name
     */
    public void setStaffName(final String name) {
        this.staffName = name;
    }

    /**
     * Sets the office name
     * @param name the office name
     */
    public void setOfficeName(final String name) {
        this.officeName = name;
    }

    /**
     * Sets the savings account ID.
     * @param id the savings account ID
     */
    public void setSavingsAccountId(final Long id) {
        this.savingsAccountId = id;
    }

    /**
     * Sets the savings ID.
     * @param id the savings ID.
     */
    public void setSavingsId(final Long id) {
        this.savingsId = id;
    }

    /**
     * Sets the first name of the client, cannot be null or empty. Note
     * that the name cannot exceed 100 characters in length.
     * @param name the first name of the client
     * @return a new instance of {@link Builder}
     */
    public static Builder firstname(final String name) {
        return new Builder(name, false);
    }

    /**
     * Sets the full name of the client, cannot be null or empty. Note
     * that the name cannot exceed 100 characters in length.
     * @param name the full name of the client
     * @return a new instance of {@link Builder}
     */
    public static Builder fullname(final String name) {
        return new Builder(name, true);
    }

}
