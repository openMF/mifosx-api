/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.internal;

import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.ClientIdentifier;
import org.mifos.sdk.client.domain.ClientImage;
import org.mifos.sdk.client.domain.PageableClients;
import org.mifos.sdk.internal.RestConstants;
import retrofit.client.Response;
import retrofit.http.*;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

import java.util.List;
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
     * @param clientId the client ID
     * @return the {@link Client} searched for
     */
    @GET("/clients/{clientId}")
    public Client findClient(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                             @Header(RestConstants.HEADER_TENANTID) String tenantId,
                             @Path("clientId") Long clientId);

    /**
     * Updates one particular client.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param client a {@link Client} with details to update
     * @return the server {@link retrofit.client.Response}
     */
    @PUT("/clients/{clientId}")
    public Response updateClient(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                 @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                 @Path("clientId") Long clientId,
                                 @Body Client client);

    /**
     * Deletes one particular client.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @return the server {@link retrofit.client.Response}
     */
    @DELETE("/clients/{clientId}")
    public Response deleteClient(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                 @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                 @Path("clientId") Long clientId);

    /**
     * Executes a given command related to the Clients API.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param command the command which is to be executed
     * @param commandBody the command request body with all its parameters
     * @return the server {@link retrofit.client.Response}
     */
    @POST("/clients/{clientId}")
    public Response executeCommand(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                   @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                   @Path("clientId") Long clientId,
                                   @Query(RestConstants.QUERY_COMMAND) String command,
                                   @Body Object commandBody);

    /**
     * Creates a new identifier.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param identifier a {@link ClientIdentifier} object
     * @return a {@link ClientIdentifier} containing the server response parameters
     */
    @POST("/clients/{clientId}/identifiers")
    public ClientIdentifier createIdentifier(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                             @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                             @Path("clientId") Long clientId,
                                             @Body ClientIdentifier identifier);

    /**
     * Retrieves all available clients.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @return a list of {@link ClientIdentifier}
     */
    @GET("/clients/{clientId}/identifiers")
    public List<ClientIdentifier> fetchIdentifiers(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                                   @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                                   @Path("clientId") Long clientId);

    /**
     * Retrieves a particular identifier.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @return the {@link ClientIdentifier} searched for
     */
    @GET("/clients/{clientId}/identifiers/{identifierId}")
    public ClientIdentifier findIdentifier(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                           @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                           @Path("clientId") Long clientId,
                                           @Path("identifierId") Long identifierId);

    /**
     * Updates a particular identifier.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @param identifier a {@link ClientIdentifier} object with details to update
     * @return the server {@link retrofit.client.Response}
     */
    @PUT("/clients/{clientId}/identifiers/{identifierId}")
    public Response updateIdentifier(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                     @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                     @Path("clientId") Long clientId,
                                     @Path("identifierId") Long identifierId,
                                     @Body ClientIdentifier identifier);

    /**
     * Deletes a particular identifier.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @return the server {@link retrofit.client.Response}
     */
    @DELETE("/clients/{clientId}/identifiers/{identifierId}")
    public Response deleteIdentifier(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                     @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                     @Path("clientId") Long clientId,
                                     @Path("identifierId") Long identifierId);

    /**
     * Uploads the client image.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param base64Data the Base64 Data URI of the client image
     * @return a {@link ClientImage} with the resource ID
     */
    @POST("/clients/{clientId}/images")
    public ClientImage uploadImage(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                            @Header(RestConstants.HEADER_TENANTID) String tenantId,
                            @Path("clientId") Long clientId,
                            @Body TypedString base64Data);

    /**
     * Retrieves the client image.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param maxWidth Optional: the maximum width of the image
     * @param maxHeight Optional: the maximum height of the image
     * @return a {@link TypedFile} of the client image if found, null otherwise
     */
    @GET("/clients/{clientId}/images")
    public String findImage(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                            @Header(RestConstants.HEADER_TENANTID) String tenantId,
                            @Path("clientId") Long clientId,
                            @Query("maxWidth") Long maxWidth,
                            @Query("maxHeight") Long maxHeight);

    /**
     * Updates the client image.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @param base64Data the Base64 Data URI of the client image
     * @return the server {@link retrofit.client.Response}
     */
    @PUT("/clients/{clientId}/images")
    public Response updateImage(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                @Path("clientId") Long clientId,
                                @Body TypedString base64Data);

    /**
     * Deletes the client image.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param clientId the client ID
     * @return the server {@link retrofit.client.Response}
     */
    @DELETE("/clients/{clientId}/images")
    public Response deleteImage(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                @Path("clientId") Long clientId);

}
