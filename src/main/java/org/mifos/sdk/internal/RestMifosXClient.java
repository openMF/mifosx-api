/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

import org.mifos.sdk.MifosXConnectException;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import org.mifos.sdk.MifosXClient;
import org.mifos.sdk.MifosXProperties;

/**
 * Implements {@link MifosXClient} and the inner lying methods
 * for the basic authentication workflow.
 */
public class RestMifosXClient implements MifosXClient {

    private final MifosXProperties connectionProperties;
    private final RestAdapter restAdapter;
    private String authenticationKey;
    private boolean loggedIn;

    /**
     * Constructor to initialise a new instance of {@link RestMifosXClient}
     * with parameter properties.
     * @param properties the {@link MifosXProperties} for authentication
     */
    public RestMifosXClient(final MifosXProperties properties,
                            final RestAdapter adapter) {
        super();
        this.connectionProperties = properties;
        this.restAdapter = adapter;
        this.authenticationKey = null;
        this.loggedIn = false;
    }

    /**
     * Overridden method to authenticate and obtain a valid
     * authentication key from the MifosX server.
     * @throws MifosXConnectException
     */
    @Override
    public void login() throws MifosXConnectException {
        if (!loggedIn) {
            try {
                final RetrofitMifosService mifosService = this.restAdapter.create(RetrofitMifosService.class);
                final AuthenticationToken authenticationToken = mifosService.authenticate(this.connectionProperties.getUsername(),
                        this.connectionProperties.getPassword(),
                        this.connectionProperties.getTenant());
                this.authenticationKey = authenticationToken.getAuthenticationToken();
                this.loggedIn = true;
            } catch (RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
                } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                           error.getResponse().getStatus() == 401) {
                    this.loggedIn = false;
                    this.authenticationKey = null;
                    throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
                } else {
                    throw new MifosXConnectException(ErrorCode.UNKNOWN);
                }
            }
        }
    }

    /**
     * Returns the authentication key.
     * @return the authentication key obtained by calling {@link #login()}
     */
    String getAuthenticationKey() {
        return this.authenticationKey;
    }

    /**
     * Overridden method to delete the authentication key.
     */
    @Override
    public void logout() {
        this.authenticationKey = null;
        this.loggedIn = false;
    }

    /** Returns whether the client is logged in. */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

}
