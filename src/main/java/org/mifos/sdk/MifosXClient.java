/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

import org.mifos.sdk.client.ClientService;
import org.mifos.sdk.office.OfficeService;
import org.mifos.sdk.staff.StaffService;

/**
 * Principle client interface for the base authentication workflow
 */
public interface MifosXClient {

    /**
     * Authenticates into the MifosX server by obtaining an authentication key.
     * If a valid authentication key already exists, this method does nothing.
     * @throws MifosXConnectException
     */
    void login() throws MifosXConnectException;

    /**
     * Deletes the authnetication key. To be able to carry further operations,
     * you must call {@link #login()} to obtain a new authentication key.
     */
    void logout();

    /**
     * Returns an instance of {@link OfficeService} to use the Office API.
     * @throws MifosXConnectException
     */
    OfficeService officeService() throws MifosXConnectException;

    /**
     * Returns an instance of {@link StaffService} to use the Staff API.
     * @throws MifosXConnectException
     */
    StaffService staffService() throws MifosXConnectException;

    /**
     * Returns an instance of {@link ClientService} to use the Client API.
     * @throws MifosXConnectException
     */
    ClientService clientService() throws MifosXConnectException;

}
