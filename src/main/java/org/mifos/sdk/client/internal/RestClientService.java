package org.mifos.sdk.client.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.client.ClientService;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.PageableClients;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.ServerResponseUtil;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.Map;

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
     * @param id the client ID
     * @return a {@link Client} with all the details of the searched client
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public Client findClient(Long id) throws MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(id);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        Client responseClient = null;
        try {
            responseClient = clientService.findClient(this.authenticationKey,
                    this.connectionProperties.getTenant(), id);
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
     * @param id the client ID
     * @param client a {@link Client} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void updateClient(Long id, Client client) throws MifosXConnectException,
            MifosXResourceException {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(client);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.updateClient(this.authenticationKey, this.connectionProperties.getTenant(),
                    id, client);
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
     * @param id the client ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void deleteClient(Long id) throws MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(id);
        final RetrofitClientService clientService = this.restAdapter.create(RetrofitClientService.class);
        try {
            clientService.deleteClient(this.authenticationKey, this.connectionProperties.getTenant(),
                    id);
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

}
