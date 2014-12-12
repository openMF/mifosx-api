/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.staff.internal;

import com.google.common.base.Preconditions;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.ServerResponseUtil;
import org.mifos.sdk.staff.StaffService;
import org.mifos.sdk.staff.domain.Staff;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.Arrays;
import java.util.List;

/**
 * Implements {@link StaffService} and the inner lying methods
 * for communication with the Staff API.
 */
public class RestStaffService implements StaffService {

    private final MifosXProperties connectionProperties;
    private final RestAdapter restAdapter;
    private final String authenticationKey;
    private final List<String> allowedStatuses;

    /**
     * Constructs a new instance of {@link RestStaffService} with the
     * provided properties, adapter and authKey.
     * @param properties the {@link MifosXProperties} with the API URL endpoint
     * @param adapter the rest adapter used for creating Retrofit services
     * @param authKey the authentication key obtain by calling {@link org.mifos.sdk.MifosXClient#login()}
     */
    public RestStaffService(final MifosXProperties properties,
                            final RestAdapter adapter,
                            final String authKey) {
        super();

        Preconditions.checkNotNull(properties);
        Preconditions.checkNotNull(adapter);
        Preconditions.checkNotNull(authKey);

        this.connectionProperties = properties;
        this.restAdapter = adapter;
        this.authenticationKey = authKey;
        this.allowedStatuses = Arrays.asList("active", "inactive", "all");
    }

    /**
     * Creates a new staff.
     * @param staff the {@link Staff} to create
     * @return a {@link Staff} with the office ID and the resource ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    @Override
    public Staff createStaff(final Staff staff) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(staff);
        final RetrofitStaffService staffService = this.restAdapter.create(RetrofitStaffService.class);
        Staff responseStaff = null;
        try {
            responseStaff = staffService.createStaff(this.authenticationKey,
                    this.connectionProperties.getTenant(), staff);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                       error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return responseStaff;
    }

    /**
     * Retrieves all the available staff.
     * @return a list of {@link Staff}
     * @throws MifosXConnectException
     */
    @Override
    public List<Staff> fetchStaff() throws MifosXConnectException {
        final RetrofitStaffService staffService = this.restAdapter.create(RetrofitStaffService.class);
        List<Staff> staffList = null;
        try {
            staffList = staffService.fetchStaff(this.authenticationKey, this.connectionProperties.getTenant());
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return staffList;
    }

    /**
     * Retrieves one particular staff.
     * @param id the staff ID
     * @return a {@link Staff} with all the details of the searched staff
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    @Override
    public Staff findStaff(final Long id) throws MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(id);
        final RetrofitStaffService staffService = this.restAdapter.create(RetrofitStaffService.class);
        Staff responseStaff = null;
        try {
            responseStaff = staffService.findStaff(this.authenticationKey,
                    this.connectionProperties.getTenant(), id);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.STAFF_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return responseStaff;
    }

    /**
     * Retrieves all staff by their status.
     * @param status the status of the staff
     * @return a list of {@link Staff}
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    @Override
    public List<Staff> findStaffByStatus(final String status) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(status);
        Preconditions.checkArgument(!status.isEmpty());
        if (!this.allowedStatuses.contains(status)) {
            throw new MifosXResourceException(ErrorCode.INVALID_STATUS);
        }
        final RetrofitStaffService staffService = this.restAdapter.create(RetrofitStaffService.class);
        List<Staff> staffList = null;
        try {
            staffList = staffService.findStaffByStatus(this.authenticationKey,
                    this.connectionProperties.getTenant(), status);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.STAFF_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return staffList;
    }

    /**
     * Updates one particular staff.
     * @param id the staff ID
     * @param staff a {@link Staff} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    @Override
    public void updateStaff(final Long id, final Staff staff) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(staff);
        final RetrofitStaffService staffService = this.restAdapter.create(RetrofitStaffService.class);
        try {
            staffService.updateStaff(this.authenticationKey,
                    this.connectionProperties.getTenant(), id, staff);
        } catch(RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.STAFF_NOT_FOUND);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.STAFF_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

}
