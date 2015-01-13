/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client;

import java.util.List;
import java.util.Map;

import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.ClientIdentifier;
import org.mifos.sdk.client.domain.ClientImage;
import org.mifos.sdk.client.domain.PageableClients;
import org.mifos.sdk.client.domain.commands.*;

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
    Client createClient(final Client client) throws MifosXConnectException, MifosXResourceException;

    /**
     * Retrieves all available clients.
     * @param queryMap an {@link Map} with all the query parameters
     * @return a {@link PageableClients} with the list of {@link Client}s
     * @throws MifosXConnectException
     */
    PageableClients fetchClients(final Map<String, Object> queryMap) throws MifosXConnectException;

    /**
     * Retrieves one particular client.
     * @param clientId the client ID
     * @return a {@link Client} with all the details of the searched client
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Client findClient(final Long clientId) throws MifosXConnectException, MifosXResourceException;

    /**
     * Updates one particular client.
     * @param clientId the client ID
     * @param client a {@link Client} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateClient(final Long clientId, final Client client) throws MifosXConnectException, MifosXResourceException;

    /**
     * Deletes one particular client.
     * @param clientId the client ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void deleteClient(final Long clientId) throws MifosXConnectException, MifosXResourceException;

    /**
     * Activates a pending client or results in an error if the client is already activated.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ActivateClientCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void activateClient(final Long clientId, final ActivateClientCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Closes a client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.CloseClientCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void closeClient(final Long clientId, final CloseClientCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Assigns staff to the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AssignUnassignStaffCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void assignStaff(final Long clientId, final AssignUnassignStaffCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Unassigns staff from the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AssignUnassignStaffCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void unassignStaff(final Long clientId, final AssignUnassignStaffCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Updates the savings account of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.UpdateSavingsAccountCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateSavingsAccount(final Long clientId, final UpdateSavingsAccountCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Proposes the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ProposeClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void proposeTransfer(final Long clientId, final ProposeClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Withdraws transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.WithdrawRejectClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void withdrawTransfer(final Long clientId, final WithdrawRejectClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Rejects transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.WithdrawRejectClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void rejectTransfer(final Long clientId, final WithdrawRejectClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Accepts the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AcceptClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void acceptTransfer(final Long clientId, final AcceptClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Proposes and accepts the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ProposeAndAcceptClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void proposeAndAcceptTransfer(final Long clientId, final ProposeAndAcceptClientTransferCommand command) throws
            MifosXConnectException, MifosXResourceException;

    /**
     * Creates a new identifier for the client.
     * @param clientId the client ID
     * @param identifier a {@link ClientIdentifier} with the details of the identifier to create
     * @return a {@link ClientIdentifier} with the server response parameters
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    ClientIdentifier createIdentifier(final Long clientId, final ClientIdentifier identifier) throws
            MifosXConnectException, MifosXResourceException;

    /**
     * Retrieves all the identifiers for the client.
     * @param clientId the client ID
     * @return a list of {@link ClientIdentifier}
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    List<ClientIdentifier> fetchIdentifiers(final Long clientId) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Retrieves a particular identifier.
     * @param clientId the client ID
     * @param identifierId the identifier ID to retrieve
     * @return a {@link ClientIdentifier} with the details of the identifier searched for
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    ClientIdentifier findIdentifier(final Long clientId, final Long identifierId) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Updates a particular identifer.
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @param identifier a {@link ClientIdentifier} with the details to update
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateIdentifier(final Long clientId, final Long identifierId, final ClientIdentifier identifier) throws
            MifosXConnectException, MifosXResourceException;

    /**
     * Deletes a particular identifier.
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void deleteIdentifier(final Long clientId, final Long identifierId) throws MifosXConnectException,
            MifosXResourceException;

    /**
     * Uploads a client image.
     * @param clientId the client ID
     * @param clientImage the {@link ClientImage}
     * @return a {@link ClientImage} with the resource ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    ClientImage uploadImage(final Long clientId, final ClientImage clientImage) throws MifosXConnectException,
        MifosXResourceException;

    /**
     * Retrieves a client image.
     * @param clientId the client ID
     * @param maxWidth Optional: the maximum width of the image
     * @param maxHeight Optional: the maximum height of the image
     * @return a {@link ClientImage} with the image and the type if found, null otherwise
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    ClientImage findImage(final Long clientId, final Long maxWidth, final Long maxHeight) throws
        MifosXConnectException, MifosXResourceException;

    /**
     * Updates a client image.
     * @param clientId the client ID
     * @param clientImage the {@link ClientImage}
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateImage(final Long clientId, final ClientImage clientImage) throws MifosXConnectException,
        MifosXResourceException;

    /**
     * Deletes a client image.
     * @param clientId the client ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void deleteImage(final Long clientId) throws MifosXConnectException, MifosXResourceException;

}
