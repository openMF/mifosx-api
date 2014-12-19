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
import org.mifos.sdk.client.domain.ClientIdentifier;
import org.mifos.sdk.client.domain.PageableClients;
import org.mifos.sdk.client.domain.commands.*;

import java.util.List;
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
     * @param clientId the client ID
     * @return a {@link Client} with all the details of the searched client
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Client findClient(Long clientId) throws MifosXConnectException, MifosXResourceException;

    /**
     * Updates one particular staff.
     * @param clientId the client ID
     * @param client a {@link Client} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateClient(Long clientId, Client client) throws MifosXConnectException, MifosXResourceException;

    /**
     * Deletes one particular client.
     * @param clientId the client ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void deleteClient(Long id) throws MifosXConnectException, MifosXResourceException;

    /**
     * Activates a pending client or results in an error if the client is already activated.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ActivateClientCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void activateClient(Long clientId, ActivateClientCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Closes a client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.CloseClientCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void closeClient(Long clientId, CloseClientCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Assigns staff to the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AssignUnassignStaffCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void assignStaff(Long clientId, AssignUnassignStaffCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Unassigns staff from the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AssignUnassignStaffCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void unassignStaff(Long clientId, AssignUnassignStaffCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Updates the savings account of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.UpdateSavingsAccountCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateSavingsAccount(Long clientId, UpdateSavingsAccountCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Proposes the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ProposeClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void proposeTransfer(Long clientId, ProposeClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Withdraws transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.WithdrawRejectClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void withdrawTransfer(Long clientId, WithdrawRejectClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Rejects transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.WithdrawRejectClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void rejectTransfer(Long clientId, WithdrawRejectClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Accepts the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AcceptClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void acceptTransfer(Long clientId, AcceptClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Proposes and accepts the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ProposeAndAcceptClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void proposeAndAcceptTransfer(Long clientId, ProposeAndAcceptClientTransferCommand command) throws
            MifosXConnectException, MifosXResourceException;

    /**
     * Creates a new identifier for the client.
     * @param clientId the client ID
     * @param identifier a {@link ClientIdentifier} with the details of the identifier to create
     * @return a {@link ClientIdentifier} with the server response parameters
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    ClientIdentifier createIdentifier(Long clientId, ClientIdentifier identifier) throws
            MifosXConnectException, MifosXResourceException;

    /**
     * Retrieves all the identifiers for the client.
     * @param clientId the client ID
     * @return a list of {@link ClientIdentifier}
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    List<ClientIdentifier> fetchIdentifiers(Long clientId) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Retrieves a particular identifier.
     * @param clientId the client ID
     * @param identifierId the identifier ID to retrieve
     * @return a {@link ClientIdentifier} with the details of the identifier searched for
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    ClientIdentifier findIdentifier(Long clientId, Long identifierId) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Updates a particular identifer.
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @param identifier a {@link ClientIdentifier} with the details to update
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateIdentifier(Long clientId, Long identifierId, ClientIdentifier identifier) throws
            MifosXConnectException, MifosXResourceException;

    /**
     * Deletes a particular identifier.
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void deleteIdentifier(Long clientId, Long identifierId) throws MifosXConnectException,
            MifosXResourceException;

}
