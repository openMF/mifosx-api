/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.client.ClientService;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.ClientIdentifier;
import org.mifos.sdk.client.domain.PageableClients;
import org.mifos.sdk.client.domain.commands.*;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.ServerResponseUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Implements {@link ClientService} and the inner lying methods
 * for communication with the Clients API.
 */
public class RestClientService implements ClientService {

    private final MifosXProperties connectionProperties;
    private final RestAdapter restAdapter;
    private final String authenticationKey;

    /**
     * Constructs a new instance of {@link RestClientService} with the
     * provided properties, adapter and authKey.
     * @param properties the {@link MifosXProperties} with the API URL endpoint
     * @param adapter the rest adapter used for creating Retrofit services
     * @param authKey the authentication key obtain by calling {@link org.mifos.sdk.MifosXClient#login()}
     */
    public RestClientService(final MifosXProperties properties,
                             final RestAdapter adapter,
                             final String authKey) {
        super();

        Preconditions.checkNotNull(properties);
        Preconditions.checkNotNull(adapter);
        Preconditions.checkNotNull(authKey);

        this.connectionProperties = properties;
        this.authenticationKey = "Basic " + authKey;
        this.restAdapter = adapter;
    }

    /**
     * Creates a new client.
     * @param client the {@link org.mifos.sdk.client.domain.Client} to create
     * @return a {@link org.mifos.sdk.client.domain.Client} with the response parameters
     * @throws org.mifos.sdk.MifosXConnectException
     * @throws org.mifos.sdk.MifosXResourceException
     */
    public Client createClient(Client client) throws MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(client);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        Client responseClient = null;
        try {
            responseClient = clientService.createClient(this.authenticationKey,
                    this.connectionProperties.getTenant(), client);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return responseClient;
    }

    /**
     * Retrieves all available clients.
     * @param queryMap an {@link ImmutableMap} with all the query parameters
     * @return a {@link PageableClients} with the list of {@link Client}s
     * @throws MifosXConnectException
     */
    public PageableClients fetchClients(Map<String, Object> queryMap) throws MifosXConnectException {
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        PageableClients clients = null;
        try {
            clients = clientService.fetchClients(this.authenticationKey,
                    this.connectionProperties.getTenant(), queryMap);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return clients;
    }

    /**
     * Retrieves one particular staff.
     * @param clientId the client ID
     * @return a {@link Client} with all the details of the searched client
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public Client findClient(Long clientId) throws MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        Client responseClient = null;
        try {
            responseClient = clientService.findClient(this.authenticationKey,
                    this.connectionProperties.getTenant(), clientId);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return responseClient;
    }

    /**
     * Updates one particular staff.
     * @param clientId the client ID
     * @param client a {@link Client} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void updateClient(Long clientId, Client client) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(client);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.updateClient(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, client);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Deletes one particular client.
     * @param clientId the client ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void deleteClient(Long clientId) throws MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.deleteClient(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Activates a pending client or results in an error if the client is already activated.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ActivateClientCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void activateClient(Long clientId, ActivateClientCommand command) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "activate", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Closes a client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.CloseClientCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void closeClient(Long clientId, CloseClientCommand command) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "close", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Assigns staff to the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AssignUnassignStaffCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void assignStaff(Long clientId, AssignUnassignStaffCommand command) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "assignStaff", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Unassigns staff from the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AssignUnassignStaffCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void unassignStaff(Long clientId, AssignUnassignStaffCommand command) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "unassignStaff", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Updates the savings account of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.UpdateSavingsAccountCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void updateSavingsAccount(Long clientId, UpdateSavingsAccountCommand command) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "updateSavingsAccount", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Proposes the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ProposeClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void proposeTransfer(Long clientId, ProposeClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "proposeTransfer", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Withdraws transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.WithdrawRejectClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void withdrawTransfer(Long clientId, WithdrawRejectClientTransferCommand command) throws
            MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "withdrawTransfer", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Rejects transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.WithdrawRejectClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void rejectTransfer(Long clientId, WithdrawRejectClientTransferCommand command) throws
            MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "rejectTransfer", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Accepts the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.AcceptClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void acceptTransfer(Long clientId, AcceptClientTransferCommand command) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "acceptTransfer", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Proposes and accepts the transfer of the client.
     * @param clientId the client ID
     * @param command the {@link org.mifos.sdk.client.domain.commands.ProposeAndAcceptClientTransferCommand} command
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void proposeAndAcceptTransfer(Long clientId, ProposeAndAcceptClientTransferCommand command) throws
            MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(command);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.executeCommand(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, "proposeAndAcceptTransfer", command, commandsCallback);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Creates a new identifier for the client.
     * @param clientId the client ID
     * @param identifier a {@link org.mifos.sdk.client.domain.ClientIdentifier} with the details of the identifier to create
     * @return a {@link org.mifos.sdk.client.domain.ClientIdentifier} with the server response parameters
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public ClientIdentifier createIdentifier(Long clientId, ClientIdentifier identifier) throws
            MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(identifier);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        ClientIdentifier responseIdentifier = null;
        try {
            responseIdentifier = clientService.createIdentifier(this.authenticationKey,
                    this.connectionProperties.getTenant(), clientId, identifier);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return responseIdentifier;
    }

    /**
     * Retrieves all the identifiers for the client.
     * @param clientId the client ID
     * @return a list of {@link ClientIdentifier}
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public List<ClientIdentifier> fetchIdentifiers(Long clientId) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        List<ClientIdentifier> identifiers = null;
        try {
            identifiers = clientService.fetchIdentifiers(this.authenticationKey,
                    this.connectionProperties.getTenant(), clientId);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return identifiers;
    }

    /**
     * Retrieves a particular identifier.
     * @param clientId the client ID
     * @param identifierId the identifier ID to retrieve
     * @return a {@link ClientIdentifier} with the details of the identifier searched for
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public ClientIdentifier findIdentifier(Long clientId, Long identifierId) throws
            MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(identifierId);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        ClientIdentifier responseIdentifier = null;
        try {
            responseIdentifier = clientService.findIdentifier(this.authenticationKey,
                    this.connectionProperties.getTenant(), clientId, identifierId);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                    error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_OR_IDENTIFIER_NOT_FOUND);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return responseIdentifier;
    }

    /**
     * Updates a particular identifer.
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @param identifier a {@link ClientIdentifier} with the details to update
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void updateIdentifier(Long clientId, Long identifierId, ClientIdentifier identifier)
            throws MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(identifierId);
        Preconditions.checkNotNull(identifier);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.updateIdentifier(this.authenticationKey, this.connectionProperties.getTenant(),
                    clientId, identifierId, identifier);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_OR_IDENTIFIER_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Deletes a particular identifier.
     * @param clientId the client ID
     * @param identifierId the identifier ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void deleteIdentifier(Long clientId, Long identifierId) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(clientId);
        Preconditions.checkNotNull(identifierId);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.deleteIdentifier(this.authenticationKey, this.connectionProperties.getTenant(),
                clientId, identifierId);
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw new MifosXConnectException(ErrorCode.NOT_CONNECTED);
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION ||
                error.getResponse().getStatus() == 401) {
                throw new MifosXConnectException(ErrorCode.INVALID_AUTHENTICATION_TOKEN);
            } else if (error.getResponse().getStatus() == 403) {
                final String message = ServerResponseUtil.parseResponse(error.getResponse());
                throw new MifosXResourceException(message);
            } else if (error.getResponse().getStatus() == 404) {
                throw new MifosXResourceException(ErrorCode.CLIENT_OR_IDENTIFIER_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    Callback<JsonObject> commandsCallback = new Callback<JsonObject>() {
        @Override
        public void success(JsonObject o, Response response) {}
        @Override
        public void failure(RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.NETWORK) {
                throw RetrofitError.networkError(error.getUrl(), new IOException());
            } else if (error.getKind() == RetrofitError.Kind.CONVERSION) {
                throw RetrofitError.conversionError(error.getUrl(), error.getResponse(), null, error.getSuccessType(), new ConversionException(error.getCause()));
            } else if (error.getKind() == RetrofitError.Kind.HTTP) {
                throw RetrofitError.httpError(error.getUrl(), error.getResponse(), null,
                        error.getSuccessType());
            } else if (error.getKind() == RetrofitError.Kind.UNEXPECTED) {
                throw RetrofitError.unexpectedError(error.getUrl(), error.getCause());
            }
        }
    };

}
