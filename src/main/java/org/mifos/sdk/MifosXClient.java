/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

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

}
