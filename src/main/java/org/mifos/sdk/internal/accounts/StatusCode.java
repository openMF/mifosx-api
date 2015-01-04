/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.accounts;

/**
 * Holds the status information.
 */
public class StatusCode {

    private Long id;
    private String code;
    private String value;

    /**
     * Returns the status ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the status ID.
     * @param id the status ID
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the status code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the status code.
     * @param code the status code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the status value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the status value.
     * @param value the status value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
