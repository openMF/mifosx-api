/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.office.internal;

import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.RetrofitOfficeService;
import org.mifos.sdk.office.OfficeService;
import org.mifos.sdk.office.domain.Office;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.List;

/**
 * Implements {@link OfficeService} and the inner lying methods
 * for communication with the Office API.
 */
public class RestOfficeService implements OfficeService {

    private final MifosXProperties connectionProperties;
    private final String authenticationKey;
    private final RestAdapter restAdapter;

    /**
     * Constructs a new instance of {@link RestOfficeService} with the
     * provided properties and authKey.
     * @param properties the {@link MifosXProperties} with the API URL endpoint
     * @param authKey the authentication key obtain by calling {@link org.mifos.sdk.MifosXClient#login()}
     */
    public RestOfficeService(final MifosXProperties properties,
                             final RestAdapter adapter,
                             final String authKey) {
        super();
        this.connectionProperties = properties;
        this.authenticationKey = authKey;
        this.restAdapter = adapter;
    }

    /**
     * Creates a new office with the details provided by office.
     * @param office the {@link Office} object to create
     * @return the server-returned office ID on successful operation
     * @throws MifosXConnectException
     */
    @Override
    public Long createOffice(final Office office) throws MifosXConnectException {
        final RetrofitOfficeService officeService = this.restAdapter.create(RetrofitOfficeService.class);
        Long officeId = null;
        try {
            final Office createdOffice = officeService.createOffice(this.authenticationKey, this.connectionProperties.getTenant(),
                    office);
            officeId = createdOffice.getOfficeId();
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
        return officeId;
    }

    /**
     * Retrieves all the offices as a list.
     * @return the list of all available office
     * @throws MifosXConnectException
     */
    @Override
    public List<Office> fetchOffices() throws MifosXConnectException {
        final RetrofitOfficeService officeService = this.restAdapter.create(RetrofitOfficeService.class);
        List<Office> offices = null;
        try {
            offices = officeService.fetchOffices(this.authenticationKey, this.connectionProperties.getTenant());
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
        return offices;
    }

    /**
     * Retrieves a particular office.
     * @param id the office ID to look for
     * @return the {@link Office} object that was searched for
     * @throws MifosXConnectException
     */
    @Override
    public Office findOffice(final Long id) throws MifosXConnectException {
        final RetrofitOfficeService officeService = this.restAdapter.create(RetrofitOfficeService.class);
        Office office = null;
        try {
            office = officeService.findOffice(this.authenticationKey, this.connectionProperties.getTenant(), id);
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
        return office;
    }

    /**
     * Updates an office.
     * @param id the office ID
     * @param office the {@link Office} object to update
     * @throws MifosXConnectException
     */
    @Override
    public void updateOffice(final Long id, final Office office) throws MifosXConnectException {
        final RetrofitOfficeService officeService = this.restAdapter.create(RetrofitOfficeService.class);
        try {
            officeService.updateOffice(this.authenticationKey, this.connectionProperties.getTenant(),
                    id, office);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else {
                if (error.getResponse().getStatus() == 401) {
                    throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
                } else {
                    throw new MifosXConnectException(ErrorCode.UNKNOWN);
                }
            }
        }
    }

}
