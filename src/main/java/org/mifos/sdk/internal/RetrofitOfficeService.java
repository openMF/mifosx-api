/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

import org.mifos.sdk.office.domain.Office;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

import java.util.List;

/**
 * Retrofit service interface for communication with the Office API.
 */
public interface RetrofitOfficeService {

    /**
     * Creates a new office.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param office the {@link Office} object with the details of the office
     */
    @POST("/offices")
    public Office createOffice(@Header("Authorization: Basic") String authenticationKey,
                               @Header("X-Mifos-Platform-TenantId") String tenantId,
                               @Body Office office);

    /**
     * Retrieves all the available offices.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     */
    @GET("/offices")
    public List<Office> fetchOffices(@Header("Authorization: Basic") String authenticationKey,
                                     @Header("X-Mifos-Platform-TenantId") String tenantId);

    /**
     * Retrieves one particular office.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param id the office ID
     */
    @GET("/offices/{id}")
    public Office findOffice(@Header("Authorization: Basic") String authenticationKey,
                             @Header("X-Mifos-Platform-TenantId") String tenantId,
                             @Path("id") Long id);

    /**
     *
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param id the office ID
     * @param newOffice an {@link Office} object with the changes to be made
     */
    @PUT("/offices/{id}")
    public void updateOffice(@Header("Authorization: Basic") String authenticationKey,
                             @Header("X-Mifos-Platform-TenantId") String tenantId,
                             @Path("id") Long id,
                             @Body Office newOffice);

}
