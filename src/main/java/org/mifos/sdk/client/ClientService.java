/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client;

import com.google.common.collect.ImmutableMap;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.PageableClients;

import java.util.Map;

/**
 * Interface to communicate with the Client API.
 */
public interface ClientService {

    /**
     * Creates a new client.
     * @param client the {@link Client} to create
     * @return a {@link Client} with the response parameters
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Client createClient(Client client) throws MifosXConnectException, MifosXResourceException;

    /**
     * Retrieves all available clients.
     * @param queryMap an {@link ImmutableMap} with all the query parameters
     * @return a {@link PageableClients} with the list of {@link Client}s
     * @throws MifosXConnectException
     */
    PageableClients fetchClients(Map<String, Object> queryMap) throws MifosXConnectException;

    /**
     * Retrieves one particular staff.
     * @param id the client ID
     * @return a {@link Client} with all the details of the searched client
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Client findClient(Long id) throws MifosXConnectException, MifosXResourceException;

    /**
     * Updates one particular staff.
     * @param id the client ID
     * @param client a {@link Client} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateClient(Long id, Client client) throws MifosXConnectException, MifosXResourceException;

    /**
     * Deletes one particular client.
     * @param id the client ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void deleteClient(Long id) throws MifosXConnectException, MifosXResourceException;

}
