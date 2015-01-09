/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.staff;

import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.staff.domain.Staff;

import java.util.List;

/**
 * Interface to communicate with the Staff API.
 */
public interface StaffService {

    /**
     * Creates a new staff.
     * @param staff the {@link Staff} to create
     * @return a {@link Staff} with the office ID and the resource ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Staff createStaff(final Staff staff) throws MifosXConnectException, MifosXResourceException;

    /**
     * Retrieves all the available staff.
     * @return a list of {@link Staff}
     * @throws MifosXConnectException
     */
    List<Staff> fetchStaff() throws MifosXConnectException;

    /**
     * Retrieves one particular staff.
     * @param id the staff ID
     * @return a {@link Staff} with all the details of the searched staff
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Staff findStaff(final Long id) throws MifosXConnectException, MifosXResourceException;

    /**
     * Retrieves all staff by their status.
     * @param status the status of the staff
     * @return a list of {@link Staff}
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    List<Staff> findStaffByStatus(final String status) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Updates one particular staff.
     * @param id the staff ID
     * @param staff a {@link Staff} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateStaff(final Long id, final Staff staff) throws MifosXConnectException,
        MifosXResourceException;

}
