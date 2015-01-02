/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.accounts;

import java.util.Date;

/**
 * Holds information related to various timeline events.
 */
public class Event {

    public enum Type {
        SUBMITTED,
        ACTIVATED,
        APPROVED,
        WITHDRAWN,
        CLOSED,
        REJECTED,
        WRITEOFF,
        DISBURSED
    }

    private Type type;
    private Date date;
    private String username;
    private String firstname;
    private String lastname;

    /**
     * Returns the event type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the event type.
     * @param type the type
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username the username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns the date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     * @param date the date
     */
    public void setDate(final Date date) {
        this.date = date;
    }

    /**
     * Returns the first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the first name.
     * @param firstname the first name
     */
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    /**
     * Returns the last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the last name.
     * @param lastname the last name
     */
    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }
}
