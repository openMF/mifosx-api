/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

import org.mifos.sdk.MifosXClient;
import org.mifos.sdk.MifosXProperties;

/**
 * Implements {@link MifosXClient} and the inner lying methods
 * for the basic authentication workflow.
 */
public class RestMifosXClient implements MifosXClient {

    private MifosXProperties connectionProperties;
    private RestAdapter restAdapter;
    private String authenticationKey;
    private boolean loggedIn;

    /**
     * Constructor to initialise a new instance of {@link RestMifosXClient}
     * with parameter properties.
     * @param properties the {@link MifosXProperties} for authentication
     */
    public RestMifosXClient(final MifosXProperties properties) {
        super();
        this.connectionProperties = properties;
        this.restAdapter = new RestAdapter.Builder()
                           .setClient(new OkClient(new OkHttpClient()))
                           .setEndpoint(this.connectionProperties.getUrl())
                           .build();
        this.authenticationKey = null;
        this.loggedIn = false;
    }

    /**
     * Overridden method to authenticate and obtain a valid
     * authentication key from the MifosX server.
     */
    @Override
    public void login() {
        if (!loggedIn) {
            final RetrofitMifosService mifosService = this.restAdapter.create(RetrofitMifosService.class);
            mifosService.authenticate(this.connectionProperties.getUsername(),
                                      this.connectionProperties.getPassword(),
                                      this.connectionProperties.getTenant(),
                                      responseCallback);
        }
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

    /**
     * Callback method to handle response from server
     * after authentication attempt.
     */
    private Callback<JsonObject> responseCallback = new Callback<JsonObject>() {
        @Override
        public void success(JsonObject jsonObject, Response response) {
            authenticationKey = jsonObject.get("base64EncodedAuthenticationKey").getAsString();
            loggedIn = true;
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

}
