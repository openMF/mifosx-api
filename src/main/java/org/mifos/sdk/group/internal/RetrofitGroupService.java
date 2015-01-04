/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.internal;

import org.mifos.sdk.group.domain.Group;
import org.mifos.sdk.group.domain.GroupAccountsSummary;
import org.mifos.sdk.group.domain.PageableGroups;
import org.mifos.sdk.internal.RestConstants;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * Retrofit service interface for communication with the Groups API.
 */
public interface RetrofitGroupService {

    /**
     * Creates a new group.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param group the {@link Group} to create
     * @return a {@link Group} object with the response parameters
     */
    @POST("/groups")
    public Group createGroup(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                             @Header(RestConstants.HEADER_TENANTID) String tenantId,
                             @Body Group group);

    /**
     * Retrieves all available groups.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param filters Optional: the query parameters
     * @return a {@link PageableGroups} with the list of groups and the number of filtered groups
     */
    @GET("/groups")
    public PageableGroups fetchGroups(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                      @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                      @QueryMap Map<String, Object> filters);

    /**
     * Retrieves a particular group.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param groupId the group ID
     * @param filters Optional: the query parameters
     * @return the {@link Group} searched for
     */
    @GET("/groups/{groupId}")
    public Group findGroup(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                           @Header(RestConstants.HEADER_TENANTID) String tenantId,
                           @Path("groupId") Long groupId,
                           @QueryMap Map<String, Object> filters);

    /**
     * Retreives the group accounts summary.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param groupId the group ID
     * @param fields the query fields
     * @return the {@link GroupAccountsSummary} for the given group
     */
    @GET("/groups/{groupId}/accounts")
    public GroupAccountsSummary findGroupsAccountsSummary(@Header(RestConstants.HEADER_AUTHORIZATION)
                                                              String authenticationKey,
                                                          @Header(RestConstants.HEADER_TENANTID)
                                                          String tenantId,
                                                          @Path("groupId") Long groupId,
                                                          @Query("fields") String fields);

    /**
     * Updates a particular group.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param groupId the group ID
     * @param group the {@link Group} object with the changes to make
     * @return the server {@link Response}
     */
    @PUT("/groups/{groupId}")
    public Response updateGroup(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                @Path("groupId") Long groupId,
                                @Body Group group);

    /**
     * Deletes a particular group.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param groupId the group ID
     * @return the server {@link Response}
     */
    @DELETE("/groups/{groupId}")
    public Response deleteGroup(@Header(RestConstants.HEADER_AUTHORIZATION) String authenticationKey,
                                @Header(RestConstants.HEADER_TENANTID) String tenantId,
                                @Path("groupId") Long groupId);

}
