/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain;

import org.mifos.sdk.internal.accounts.Event;
import org.mifos.sdk.internal.accounts.StatusCode;
import org.mifos.sdk.internal.accounts.Timeline;

import java.util.Date;
import java.util.List;

/**
 * Gives an interface to communicate with the Groups API.
 */
public class Group {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link Group}
     */
    public static class Builder {

        private String name;
        private Long officeId;
        private boolean active;
        private Date activationDate;
        private String locale;
        private String dateFormat;
        private String externalId;
        private Long staffId;
        private List<Long> clientMembers;
        private Date submittedOnDate;

        private Builder(final String groupName) {
            this.name = groupName;
        }

        /**
         * Sets the office ID.
         * @param id the office ID
         * @return the current instance of {@link Builder}
         */
        public Builder officeId(final Long id) {
            this.officeId = id;
            return this;
        }

        /**
         * Sets the active status.
         * @param isActive the active status
         * @return the current instance of {@link Builder}
         */
        public Builder active(final boolean isActive) {
            this.active = isActive;
            return this;
        }

        /**
         * Sets the activation date.
         * @param date the activation date
         * @return the current instance of {@link Builder}
         */
        public Builder activationDate(final Date date) {
            this.activationDate = date;
            return this;
        }

        /**
         * Sets the locale.
         * @param lang the locale
         * @return the current instance of {@link Builder}
         */
        public Builder locale(final String lang) {
            this.locale = lang;
            return this;
        }

        /**
         * Sets the date format.
         * @param format the date format
         * @return the current instance of {@link Builder}
         */
        public Builder dateFormat(final String format) {
            this.dateFormat = format;
            return this;
        }

        /**
         * Sets the external ID.
         * @param id the external ID
         * @return the current instance of {@link Builder}
         */
        public Builder externalId(final String id) {
            this.externalId = id;
            return this;
        }

        /**
         * Sets the staff ID.
         * @param id the staff ID
         * @return the current instance of {@link Builder}
         */
        public Builder staffId(final Long id) {
            this.staffId = id;
            return this;
        }

        /**
         * Sets the client members.
         * @param members the client members
         * @return the current instance of {@link Builder}
         */
        public Builder clientMembers(final List<Long> members) {
            this.clientMembers = members;
            return this;
        }

        /**
         * Sets the submitted on date.
         * @param date the submittion date
         * @return the current instance of {@link Builder}
         */
        public Builder submittedOnDate(final Date date) {
            this.submittedOnDate = date;
            return this;
        }

        /**
         * Constructs a new Group instance with the provided parameters.
         * @return a new instance of {@link Group}
         */
        public Group build() {
            return new Group(this.name, this.officeId, this.active, this.activationDate,
                this.locale, this.dateFormat, this.externalId, this.staffId, this.clientMembers,
                this.submittedOnDate);
        }

    }

    private String name;
    private Long officeId;
    private boolean active;
    private Date activationDate;
    private String locale;
    private String dateFormat;
    private String externalId;
    private Long staffId;
    private List<Long> clientMembers;
    private Date submittedOnDate;
    private StatusCode status;
    private Long resourceId;
    private String officeName;
    private Long centerId;
    private String centerName;
    private String staffName;
    private Timeline timeline;

    private Group() {}

    private Group(final String groupName,
                  final Long officeId,
                  final boolean isActive,
                  final Date activationDate,
                  final String lang,
                  final String dateFormat,
                  final String groupExternalId,
                  final Long staffId,
                  final List<Long> members,
                  final Date submittedOnDate) {
        this.name = groupName;
        this.officeId = officeId;
        this.active = isActive;
        this.activationDate = activationDate;
        this.locale = lang;
        this.dateFormat = dateFormat;
        this.externalId = groupExternalId;
        this.staffId = staffId;
        this.clientMembers = members;
        this.submittedOnDate = submittedOnDate;
    }

    /**
     * Returns the client members.
     */
    public List<Long> getClientMembers() {
        return this.clientMembers;
    }

    /**
     * Returns the group name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the office ID of the office, the group is associated with.
     */
    public Long getOfficeId() {
        return this.officeId;
    }

    /**
     * Returns true of the group is active, false otherwise.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Returns the activation date.
     */
    public Date getActivationDate() {
        if (this.timeline != null) {
            for (final Event event : this.timeline.getEvents()) {
                if (event.getType() == Event.Type.ACTIVATED) {
                    return event.getDate();
                }
            }
        }

        return this.activationDate;
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
     * Returns the external ID.
     */
    public String getExternalId() {
        return this.externalId;
    }

    /**
     * Returns the staff ID.
     */
    public Long getStaffId() {
        return this.staffId;
    }

    /**
     * Returns the submitted on date.
     */
    public Date getSubmittedOnDate() {
        if (this.timeline != null) {
            for (final Event event : this.timeline.getEvents()) {
                if (event.getType() == Event.Type.SUBMITTED) {
                    return event.getDate();
                }
            }
        }

        return this.submittedOnDate;
    }

    /**
     * Returns the status.
     */
    public StatusCode getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     * @param status the status
     */
    public void setStatus(final StatusCode status) {
        this.status = status;
    }

    /**
     * Returns the resource ID of the group.
     */
    public Long getResourceId() {
        return this.resourceId;
    }

    /**
     * Sets the resource ID of the group.
     * @param resourceId the resource ID
     */
    public void setResourceId(final Long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Retruns the office name.
     */
    public String getOfficeName() {
        return this.officeName;
    }

    /**
     * Sets the office name.
     * @param officeName the office name
     */
    public void setOfficeName(final String officeName) {
        this.officeName = officeName;
    }

    /**
     * Returns the center ID.
     */
    public Long getCenterId() {
        return this.centerId;
    }

    /**
     * Sets the center ID.
     * @param centerId the center ID
     */
    public void setCenterId(final Long centerId) {
        this.centerId = centerId;
    }

    /**
     * Returns the center name.
     */
    public String getCenterName() {
        return this.centerName;
    }

    /**
     * Sets the center name.
     * @param centerName the center name.
     */
    public void setCenterName(final String centerName) {
        this.centerName = centerName;
    }

    /**
     * Returns the staff name.
     */
    public String getStaffName() {
        return this.staffName;
    }

    /**
     * Sets the staff name.
     * @param staffName the staff name
     */
    public void setStaffName(final String staffName) {
        this.staffName = staffName;
    }

    /**
     * Returns the timeline.
     */
    public Timeline getTimeline() {
        return this.timeline;
    }

    /**
     * Sets the timeline.
     * @param timeline the timeline
     */
    public void setTimeline(final Timeline timeline) {
        this.timeline = timeline;
    }

    /**
     * Sets the group name.
     * @param name the group name.
     * @return a new instance of {@link Builder}
     */
    public static Builder name(final String name) {
        return new Builder(name);
    }

}
