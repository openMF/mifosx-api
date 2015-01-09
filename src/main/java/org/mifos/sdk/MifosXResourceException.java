/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

import com.google.common.base.Preconditions;
import org.mifos.sdk.internal.ErrorCode;

/**
 * Used for all resources related exception handling.
 */
public final class MifosXResourceException extends Exception {

    private ErrorCode errorCode;
    private String errorMessage;

    /**
     * Constructs a new instance of {@link MifosXResourceException}.
     * @param code an {@link ErrorCode} object
     */
    public MifosXResourceException(final ErrorCode code) {
        super(code.getMessage());
        Preconditions.checkNotNull(code);
        this.errorCode = code;
    }

    /**
     * Constructs a new instance of {@link MifosXResourceException}.
     * @param message the message to throw
     */
    public MifosXResourceException(final String message) {
        super(message);
        Preconditions.checkNotNull(message);
        this.errorMessage = message;
    }

    /**
     * Returns the {@link ErrorCode}
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        if (this.errorCode != null) {
            return this.errorCode.getMessage();
        }
        return this.errorMessage;
    }

    /**
     * Returns the exception message as a {@link String}.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getCanonicalName());
        stringBuilder.append(System.getProperty("line.separator"));
        if (this.errorCode != null) {
            stringBuilder.append(this.errorCode.getCode());
            stringBuilder.append(": ");
            stringBuilder.append(this.errorCode.getMessage());
        } else {
            stringBuilder.append(this.errorMessage);
        }
        return stringBuilder.toString();
    }

}
