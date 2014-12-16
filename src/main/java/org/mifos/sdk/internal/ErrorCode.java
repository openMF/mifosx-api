/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

/**
 * Storehouse of all possible errors that can occur during the communication
 * with the API. Provides an interface for error identification for
 * {@link org.mifos.sdk.MifosXConnectException} and {@link org.mifos.sdk.MifosXResourceException}
 */
public enum ErrorCode {

    UNKNOWN(999, "An unknown error has occured."),

    // for MifosXConnectException
    NOT_CONNECTED(100, "Could not establish a connection!"),
    NOT_LOGGED_IN(101, "Not logged in. A valid authentication key is required to carry on API requests."),
    UNAUTHENTICATED(102, "Invalid authentication details were passed in the API request."),
    INVALID_AUTHENTICATION_TOKEN(103, "Invalid authentication token was passed."),

    // for MifosXResourceException
    OFFICE_NOT_FOUND(200, "Office not found."),
    STAFF_NOT_FOUND(201, "Staff not found."),
    INVALID_STATUS(202, "Invalid staff status passed. Should only be active, inactive or all."),
    CLIENT_NOT_FOUND(203, "Client not found."),
    ;

    private int code;
    private String message;

    ErrorCode(final int errorCode, final String errorMessage) {
        this.code = errorCode;
        this.message = errorMessage;
    }

    /**
     * Returns the error code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns the error message.
     */
    public String getMessage() {
        return this.message;
    }

}
