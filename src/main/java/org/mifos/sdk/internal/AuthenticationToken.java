/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

/**
 * Handles the authentication key obtained by calling {@link org.mifos.sdk.MifosXClient#login()}
 */
public class AuthenticationToken {

    private String base64EncodedAuthenticationKey;

    /**
     * Constructs a new {@link AuthenticationToken} with the given key.
     * @param key the authentication key
     */
    public AuthenticationToken(final String key) {
        this.base64EncodedAuthenticationKey = key;
    }

    /**
     * Returns the authentication key or token.
     */
    public String getAuthenticationToken() {
        return this.base64EncodedAuthenticationKey;
    }

}
