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
    UNAUTHENTICATED(101, "Invalid authentication details were passed in the API request."),
    INVALID_BASIC_AUTHENTICATION(102, "Invalid basic authentication token was passed."),

    // for MifosXResourceException
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
