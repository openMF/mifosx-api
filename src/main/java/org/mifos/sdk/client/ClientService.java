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
import org.mifos.sdk.client.domain.commands.*;

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

    /**
     * Activates a pending client or results in an error if the client is already activated.
     * @param id the client ID
     * @param command the {@link ActivateClient} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void activateClient(Long id, ActivateClient command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Closes a client.
     * @param id the client ID
     * @param command the {@link CloseClient} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void closeClient(Long id, CloseClient command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Assigns staff to the client.
     * @param id the client ID
     * @param command the {@link AssignUnassignStaff} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void assignStaff(Long id, AssignUnassignStaff command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Unassigns staff from the client.
     * @param id the client ID
     * @param command the {@link AssignUnassignStaff} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void unassignStaff(Long id, AssignUnassignStaff command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Updates the savings account of the client.
     * @param id the client ID
     * @param command the {@link UpdateSavingsAccount} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateSavingsAccount(Long id, UpdateSavingsAccount command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Proposes the transfer of the client.
     * @param id the client ID
     * @param command the {@link ProposeClientTransfer} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void proposeTransfer(Long id, ProposeClientTransfer command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Withdraws transfer of the client.
     * @param id the client ID
     * @param command the {@link WithdrawRejectClientTransfer} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void withdrawTransfer(Long id, WithdrawRejectClientTransfer command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Rejects transfer of the client.
     * @param id the client ID
     * @param command the {@link WithdrawRejectClientTransfer} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void rejectTransfer(Long id, WithdrawRejectClientTransfer command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Accepts the transfer of the client.
     * @param id the client ID
     * @param command the {@link AcceptClientTransfer} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void acceptTransfer(Long id, AcceptClientTransfer command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Proposes and accepts the transfer of the client.
     * @param id the client ID
     * @param command the {@link ProposeAndAcceptClientTransfer} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void proposeAndAcceptTransfer(Long id, ProposeAndAcceptClientTransfer command) throws
            MifosXConnectException, MifosXResourceException;

}
