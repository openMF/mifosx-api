/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.internal;

import com.google.gson.JsonObject;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.PageableClients;
import org.mifos.sdk.internal.RestConstants;
import retrofit.Callback;
import retrofit.http.*;

import java.util.Map;

/**
 * Retrofit service interface for communication with the Staff API.
 */
public interface RetrofitClientService {

    /**
     * Creates a new client.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param client the {@link Client} object with the details of the client
     * @return a {@link Client} with the response parameters
     */
    @POST("/clients")
    public Client createClient(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                               @Header(RestConstants.HEADER_TENANTID) String tenantId,
                               @Body Client client);

    /**
     * Retrieves all the available clients.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param filters <strong>Optional:</strong> various filters to filter out results
     * @return a {@link PageableClients} with the clients list
     */
    @GET("/clients")
    public PageableClients fetchClients(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                        @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                        @QueryMap Map<String, Object> filters);

    /**
     * Retrieves one particular client.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param id the client ID
     * @return the {@link Client} searched for
     */
    @GET("/clients/{id}")
    public Client findClient(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                             @Header(RestConstants.HEADER_TENANTID) String tenantId,
                             @Path("id") Long id);

    /**
     * Updates one particular client.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param client a {@link Client} with details to update
     */
    @PUT("/clients/{id}")
    public void updateClient(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                             @Header(RestConstants.HEADER_TENANTID) String tenantId,
                             @Path("id") Long id,
                             @Body Client client);

    /**
     * Deletes one particular client.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param id the client ID
     */
    @DELETE("/clients/{id}")
    public void deleteClient(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                             @Header(RestConstants.HEADER_TENANTID) String tenantId,
                             @Path("id") Long id);

    /**
     * Executes a given command related to the Clients API.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param id the client ID
     * @param command the command which is to be executed
     * @param commandBody the command request body with all its parameters
     */
    @POST("/clients/{id}")
    public void executeCommand(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                               @Header(RestConstants.HEADER_TENANTID) String tenantId,
                               @Path("id") Long id,
                               @Query(RestConstants.QUERY_COMMAND) String command,
                               @Body Object commandBody,
                               Callback<JsonObject> callback);

}
