/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

import org.mifos.sdk.staff.domain.Staff;
import retrofit.http.*;

import java.util.List;

/**
 * Retrofit service interface for communication with the Staff API.
 */
public interface RetrofitStaffService {

    /**
     * Creates a new staff.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param staff @param office the {@link Staff} object with the details of the office
     * @return a {@link Staff} with the office ID and the resource ID
     */
    @POST("/staff")
    public Staff createStaff(@Header("Authorization: Basic") String authenticationKey,
                             @Header("X-Mifos-Platform-TenantId") String tenantId,
                             @Body Staff staff);

    /**
     * Retrieves all the available staff.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @return a list of all the available {@link Staff}
     */
    @GET("/staff")
    public List<Staff> fetchStaff(@Header("Authorization: Basic") String authenticationKey,
                                  @Header("X-Mifos-Platform-TenantId") String tenantId);

    /**
     * Retrieves one particular staff.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param id the staff ID
     * @return a {@link Staff} with the details of the searched staff
     */
    @GET("/staff/{id}")
    public Staff findStaff(@Header("Authorization: Basic") String authenticationKey,
                           @Header("X-Mifos-Platform-TenantId") String tenantId,
                           @Path("id") Long id);

    /**
     * Retrieves all the staff by their status.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param status the status of the staff, either "active", "inactive" or "all"
     * @return a list of all the {@link Staff} with their status
     */
    @GET("/staff")
    public List<Staff> findStaffByStatus(@Header("Authorization: Basic") String authenticationKey,
                                         @Header("X-Mifos-Platform-TenantId") String tenantId,
                                         @Query("status") String status);

    /**
     * Updates a staff.
     * @param authenticationKey the authentication key obtained by
     *                          calling {@link org.mifos.sdk.MifosXClient#login()}
     * @param tenantId the tenant ID
     * @param id the staff ID
     * @param newStaff a {@link Staff} object with the changes to be made
     */
    @PUT("/staff/{id}")
    public void updateStaff(@Header("Authorization: Basic") String authenticationKey,
                            @Header("X-Mifos-Platform-TenantId") String tenantId,
                            @Path("id") Long id,
                            @Body Staff newStaff);

}
