/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

import org.mifos.sdk.internal.ErrorCode;

/**
 * Used for all resources related exception handling.
 */
public class MifosXResourceException extends Exception {

    private ErrorCode errorCode;

    /**
     * Constructs a new instance of {@link MifosXResourceException}.
     * @param code an {@link ErrorCode} object
     */
    public MifosXResourceException(final ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    /**
     * Returns the {@link ErrorCode}
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the exception message as a {@link String}.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.errorCode.getCode());
        stringBuilder.append(": ");
        stringBuilder.append(this.errorCode.getMessage());
        return stringBuilder.toString();
    }

}
