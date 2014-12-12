/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.office.internal;

import com.google.common.base.Preconditions;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.ServerResponseUtil;
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
    private final RestAdapter restAdapter;
    private final String authenticationKey;

    /**
     * Constructs a new instance of {@link RestOfficeService} with the
     * provided properties, adapter and authKey.
     * @param properties the {@link MifosXProperties} with the API URL endpoint
     * @param adapter the rest adapter used for creating Retrofit services
     * @param authKey the authentication key obtain by calling {@link org.mifos.sdk.MifosXClient#login()}
     */
    public RestOfficeService(final MifosXProperties properties,
                             final RestAdapter adapter,
                             final String authKey) {
        super();

        Preconditions.checkNotNull(properties);
        Preconditions.checkNotNull(adapter);
        Preconditions.checkNotNull(authKey);

        this.connectionProperties = properties;
        this.authenticationKey = authKey;
        this.restAdapter = adapter;
    }

    /**
     * Creates a new office with the details provided by office.
     * @param office the {@link Office} to create
     * @return the server-returned office ID on successful operation
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    @Override
    public Long createOffice(final Office office) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(office);
        final RetrofitOfficeService officeService = this.restAdapter.create(RetrofitOfficeService.class);
        Long officeId = null;
        try {
            final Office createdOffice = officeService.createOffice(this.authenticationKey,
                    this.connectionProperties.getTenant(), office);
            officeId = createdOffice.getOfficeId();
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
     * @throws MifosXResourceException
     */
    @Override
    public Office findOffice(final Long id) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(id);
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
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.OFFICE_NOT_FOUND);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
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
    public void updateOffice(final Long id, final Office office) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(office);
        final RetrofitOfficeService officeService = this.restAdapter.create(RetrofitOfficeService.class);
        try {
            officeService.updateOffice(this.authenticationKey, this.connectionProperties.getTenant(),
                    id, office);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_BASIC_AUTHENTICATION);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.OFFICE_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

}
