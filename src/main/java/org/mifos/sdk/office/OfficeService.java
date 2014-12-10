/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.office;

import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.office.domain.Office;

import java.util.List;

/**
 * Interface to communicate with the Office API.
 */
public interface OfficeService {

    /**
     * Creates a new office using the Office API.
     * @param office the {@link Office} object to create
     * @return the office ID
     * @throws MifosXConnectException
     */
    Long createOffice(Office office) throws MifosXConnectException;

    /**
     * Retrieves the list of all available offices.
     * @return list of all offices
     * @throws MifosXConnectException
     */
    List<Office> fetchOffices() throws MifosXConnectException;

    /**
     * Retrieves a particular office by the id given.
     * @param id the office ID to look for
     * @return the office for the given ID
     * @throws MifosXConnectException
     */
    Office findOffice(Long id) throws MifosXConnectException;

    /**
     * Updates a particular office.
     * @param office the {@link Office} object to update
     * @throws MifosXConnectException
     */
    void updateOffice(Long id, Office office) throws MifosXConnectException;

}