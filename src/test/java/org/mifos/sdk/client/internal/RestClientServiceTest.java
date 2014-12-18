/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.PageableClients;
import org.mifos.sdk.client.domain.commands.*;
import org.mifos.sdk.internal.ErrorCode;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * Test for {@link RestClientService} and its various methods.
 */
public class RestClientServiceTest {

    private RetrofitClientService retrofitClientService;
    private MifosXProperties properties;
    private String mockedAuthKey;
    private Client defaultClient;
    private Long defaultClientId;
    private RestClientService clientService;
    private String defaultDuplicateJSON;
    private String defaultDuplicateMessage;

    /**
     * Setup all the components before testing.
     */
    @Before
    public void setup() {
        final RestAdapter restAdapter = mock(RestAdapter.class);
        this.retrofitClientService = mock(RetrofitClientService.class);
        this.properties = MifosXProperties
                .url("http://demo.openmf.org/mifosng-provider/api/v1")
                .username("mifos")
                .password("password")
                .tenant("default")
                .build();
        this.mockedAuthKey = "=hd$$34dd";
        this.defaultClient = Client
                .fullname("Davis Jones")
                .officeId((long)1)
                .active(false)
                .build();
        this.defaultClientId = (long)1;
        this.clientService = new RestClientService(this.properties, restAdapter,
                this.mockedAuthKey);
        this.mockedAuthKey = "Basic " + this.mockedAuthKey;
        this.defaultDuplicateJSON = "{\"developerMessage\": \"some random message\"}";
        this.defaultDuplicateMessage = "some random message";

        when(restAdapter.create(RetrofitClientService.class)).thenReturn(this.retrofitClientService);
    }

    /**
     * Test for successful creation of a client.
     */
    @Test
    public void testCreateClient() {
        final Client mockedClient = mock(Client.class);

        when(mockedClient.getClientId()).thenReturn(this.defaultClientId);
        when(this.retrofitClientService.createClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClient)).thenReturn(mockedClient);

        try {
            Long id = this.clientService.createClient(this.defaultClient).getClientId();

            Assert.assertNotNull(id);
            Assert.assertEquals(id, this.defaultClientId);
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link org.mifos.sdk.internal.ErrorCode#NOT_CONNECTED} exception for createClient().
     */
    @Test
    public void testCreateClientNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitClientService.createClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClient)).thenThrow(error);

        try {
            this.clientService.createClient(this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for createClient().
     */
    @Test
    public void testCreateClientDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(),
                new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.createClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClient)).thenThrow(error);

        try {
            this.clientService.createClient(this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for conversion error exception for createClient().
     */
    @Test
    public void testCreateClientConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitClientService.createClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClient)).thenThrow(error);

        try {
            this.clientService.createClient(this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for createClient().
     */
    @Test
    public void testCreateClientInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.createClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClient)).thenThrow(error);

        try {
            this.clientService.createClient(this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for createClient().
     */
    @Test
    public void testCreateClientUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.createClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClient)).thenThrow(error);

        try {
            this.clientService.createClient(this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test to fetch all available clients.
     */
    @Test
    public void testFetchClients() {
        final List<Client> clientsList = Arrays.asList(this.defaultClient, this.defaultClient);
        final PageableClients pageableClients = mock(PageableClients.class);

        when(this.retrofitClientService.fetchClients(this.mockedAuthKey,
                this.properties.getTenant(), null)).thenReturn(pageableClients);
        when(pageableClients.getClients()).thenReturn(clientsList);

        try {
            final List<Client> responseClients = this.clientService.fetchClients(null).getClients();

            Assert.assertNotNull(responseClients);
            Assert.assertThat(responseClients, equalTo(clientsList));
        } catch (MifosXConnectException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for fetchClients().
     */
    @Test
    public void testFetchClientsNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitClientService.fetchClients(this.mockedAuthKey,
                this.properties.getTenant(), null)).thenThrow(error);

        try {
            this.clientService.fetchClients(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test for conversion error exception for fetchClients().
     */
    @Test
    public void testFetchClientsConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitClientService.fetchClients(this.mockedAuthKey,
                this.properties.getTenant(), null)).thenThrow(error);

        try {
            this.clientService.fetchClients(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for fetchClients().
     */
    @Test
    public void testFetchClientsInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.fetchClients(this.mockedAuthKey,
                this.properties.getTenant(), null)).thenThrow(error);

        try {
            this.clientService.fetchClients(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for fetchClients().
     */
    @Test
    public void testFetchClientsUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.fetchClients(this.mockedAuthKey,
                this.properties.getTenant(), null)).thenThrow(error);

        try {
            this.clientService.fetchClients(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }
    }

    /**
     * Test to find one particular client.
     */
    @Test
    public void testFindClient() {
        when(this.retrofitClientService.findClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClientId)).thenReturn(this.defaultClient);

        try {
            final Client responseClient = this.clientService.findClient(this.defaultClientId);

            Assert.assertNotNull(responseClient);
            Assert.assertThat(responseClient, equalTo(this.defaultClient));
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for findClient().
     */
    @Test
    public void testFindOfficeNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitClientService.findClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClientId)).thenThrow(error);

        try {
            this.clientService.findClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for findClient().
     */
    @Test
    public void testFindOfficeDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.findClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClientId)).thenThrow(error);

        try {
            this.clientService.findClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for conversion error exception for findClient().
     */
    @Test
    public void testFindOfficeConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitClientService.findClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClientId)).thenThrow(error);

        try {
            this.clientService.findClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for findClient().
     */
    @Test
    public void testFindOfficeNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.findClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClientId)).thenThrow(error);

        try {
            this.clientService.findClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch(MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for findClient().
     */
    @Test
    public void testFindOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.findClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClientId)).thenThrow(error);

        try {
            this.clientService.findClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch(MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for findClient().
     */
    @Test
    public void testFindOfficeUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitClientService.findClient(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultClientId)).thenThrow(error);

        try {
            this.clientService.findClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for updateClient().
     */
    @Test
    public void testUpdateClientNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).updateClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, this.defaultClient);

        try {
            this.clientService.updateClient(this.defaultClientId, this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for updateClient().
     */
    @Test
    public void testUpdateClientDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).updateClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, this.defaultClient);

        try {
            this.clientService.updateClient(this.defaultClientId, this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for updateClient().
     */
    @Test
    public void testUpdateClientInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).updateClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, this.defaultClient);

        try {
            this.clientService.updateClient(this.defaultClientId, this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for updateClient().
     */
    @Test
    public void testUpdateClientNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).updateClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, this.defaultClient);

        try {
            this.clientService.updateClient(this.defaultClientId, this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for updateClient().
     */
    @Test
    public void testUpdateClientUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).updateClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, this.defaultClient);

        try {
            this.clientService.updateClient(this.defaultClientId, this.defaultClient);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for deleteClient().
     */
    @Test
    public void testDeleteClientNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).deleteClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId);

        try {
            this.clientService.deleteClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for deleteClient().
     */
    @Test
    public void testDeleteClientDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).deleteClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId);

        try {
            this.clientService.deleteClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for deleteClient().
     */
    @Test
    public void testDeleteClientInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).deleteClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId);

        try {
            this.clientService.deleteClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for deleteClient().
     */
    @Test
    public void testDeleteClientNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).deleteClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId);

        try {
            this.clientService.deleteClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for deleteClient().
     */
    @Test
    public void testDeleteClientUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).deleteClient(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId);

        try {
            this.clientService.deleteClient(this.defaultClientId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for activateClient().
     */
    @Test
    public void testActivateClientNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final ActivateClient command = mock(ActivateClient.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "activate", command,
                this.clientService.commandsCallback);

        try {
            this.clientService.activateClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for activateClient().
     */
    @Test
    public void testActivateClientDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final ActivateClient command = mock(ActivateClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "activate", command, this.clientService.commandsCallback);

        try {
            this.clientService.activateClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for activateClient().
     */
    @Test
    public void testActivateClientInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final ActivateClient command = mock(ActivateClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "activate", command, this.clientService.commandsCallback);

        try {
            this.clientService.activateClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for activateClient().
     */
    @Test
    public void testActivateClientNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final ActivateClient command = mock(ActivateClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "activate", command, this.clientService.commandsCallback);

        try {
            this.clientService.activateClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for activateClient().
     */
    @Test
    public void testActivateClientUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final ActivateClient command = mock(ActivateClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "activate", command, this.clientService.commandsCallback);

        try {
            this.clientService.activateClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for closeClient().
     */
    @Test
    public void testCloseClientNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final CloseClient command = mock(CloseClient.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "close", command, this.clientService.commandsCallback);

        try {
            this.clientService.closeClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for closeClient().
     */
    @Test
    public void testCloseClientDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final CloseClient command = mock(CloseClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "close", command, this.clientService.commandsCallback);

        try {
            this.clientService.closeClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for closeClient().
     */
    @Test
    public void testCloseClientInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final CloseClient command = mock(CloseClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "close", command, this.clientService.commandsCallback);

        try {
            this.clientService.closeClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for closeClient().
     */
    @Test
    public void testCloseClientNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final CloseClient command = mock(CloseClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "close", command, this.clientService.commandsCallback);

        try {
            this.clientService.closeClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for closeClient().
     */
    @Test
    public void testCloseClientUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final CloseClient command = mock(CloseClient.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "close", command, this.clientService.commandsCallback);

        try {
            this.clientService.closeClient(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for assignStaff().
     */
    @Test
    public void testAssignStaffNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "assignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.assignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for assignStaff().
     */
    @Test
    public void testAssignStaffDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "assignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.assignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for assignStaff().
     */
    @Test
    public void testAssignStaffInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "assignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.assignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for assignStaff().
     */
    @Test
    public void testAssignStaffNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "assignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.assignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for assignStaff().
     */
    @Test
    public void testAssignStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "assignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.assignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for unassignStaff().
     */
    @Test
    public void testUnassignStaffNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "unassignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.unassignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for unassignStaff().
     */
    @Test
    public void testUnassignStaffDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "unassignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.unassignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for unassignStaff().
     */
    @Test
    public void testUnassignStaffInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "unassignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.unassignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for unassignStaff().
     */
    @Test
    public void testUnassignStaffNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "unassignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.unassignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for unassignStaff().
     */
    @Test
    public void testUnassignStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaff command = mock(AssignUnassignStaff.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "unassignStaff", command, this.clientService.commandsCallback);

        try {
            this.clientService.unassignStaff(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for updateSavingsAccount().
     */
    @Test
    public void testUpdateSavingsAccountNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final UpdateSavingsAccount command = mock(UpdateSavingsAccount.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "updateSavingsAccount", command, this.clientService.commandsCallback);

        try {
            this.clientService.updateSavingsAccount(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for updateSavingsAccount().
     */
    @Test
    public void testUpdateSavingsAccountDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final UpdateSavingsAccount command = mock(UpdateSavingsAccount.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "updateSavingsAccount", command, this.clientService.commandsCallback);

        try {
            this.clientService.updateSavingsAccount(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for updateSavingsAccount().
     */
    @Test
    public void testUpdateSavingsAccountInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final UpdateSavingsAccount command = mock(UpdateSavingsAccount.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "updateSavingsAccount", command, this.clientService.commandsCallback);

        try {
            this.clientService.updateSavingsAccount(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for updateSavingsAccount().
     */
    @Test
    public void testUpdateSavingsAccountNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final UpdateSavingsAccount command = mock(UpdateSavingsAccount.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "updateSavingsAccount", command, this.clientService.commandsCallback);

        try {
            this.clientService.updateSavingsAccount(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for updateSavingsAccount().
     */
    @Test
    public void testUpdateSavingsAccountUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final UpdateSavingsAccount command = mock(UpdateSavingsAccount.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "updateSavingsAccount", command, this.clientService.commandsCallback);

        try {
            this.clientService.updateSavingsAccount(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for proposeTransfer().
     */
    @Test
    public void testProposeTransferNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final ProposeClientTransfer command = mock(ProposeClientTransfer.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for proposeTransfer().
     */
    @Test
    public void testProposeTransferDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final ProposeClientTransfer command = mock(ProposeClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for proposeTransfer().
     */
    @Test
    public void testProposeTransferInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final ProposeClientTransfer command = mock(ProposeClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for proposeTransfer().
     */
    @Test
    public void testProposeTransferNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final ProposeClientTransfer command = mock(ProposeClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for proposeTransfer().
     */
    @Test
    public void testProposeTransferUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final ProposeClientTransfer command = mock(ProposeClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for withdrawTransfer().
     */
    @Test
    public void testWithdrawTransferNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "withdrawTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.withdrawTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for withdrawTransfer().
     */
    @Test
    public void testWithdrawTransferDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "withdrawTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.withdrawTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for withdrawTransfer().
     */
    @Test
    public void testWithdrawTransferInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "withdrawTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.withdrawTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for withdrawTransfer().
     */
    @Test
    public void testWithdrawTransferNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "withdrawTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.withdrawTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for withdrawTransfer().
     */
    @Test
    public void testWithdrawTransferUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "withdrawTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.withdrawTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for rejectTransfer().
     */
    @Test
    public void testRejectTransferNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "rejectTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.rejectTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for rejectTransfer().
     */
    @Test
    public void testRejectTransferDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "rejectTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.rejectTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for rejectTransfer().
     */
    @Test
    public void testRejectTransferInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "rejectTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.rejectTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for rejectTransfer().
     */
    @Test
    public void testRejectTransferNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "rejectTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.rejectTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for rejectTransfer().
     */
    @Test
    public void testRejectTransferUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final WithdrawRejectClientTransfer command = mock(WithdrawRejectClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "rejectTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.rejectTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for acceptTransfer().
     */
    @Test
    public void testAcceptTransferNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final AcceptClientTransfer command = mock(AcceptClientTransfer.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "acceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.acceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for acceptTransfer().
     */
    @Test
    public void testAcceptTransferDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final AcceptClientTransfer command = mock(AcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "acceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.acceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for acceptTransfer().
     */
    @Test
    public void testAcceptTransferInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final AcceptClientTransfer command = mock(AcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "acceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.acceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for acceptTransfer().
     */
    @Test
    public void testAcceptTransferNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final AcceptClientTransfer command = mock(AcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "acceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.acceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for acceptTransfer().
     */
    @Test
    public void testAcceptTransferUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AcceptClientTransfer command = mock(AcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "acceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.acceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for proposeAndAcceptTransfer().
     */
    @Test
    public void testProposeAndAcceptTransferNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final ProposeAndAcceptClientTransfer command = mock(ProposeAndAcceptClientTransfer.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeAndAcceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeAndAcceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for proposeAndAcceptTransfer().
     */
    @Test
    public void testProposeAndAcceptTransferDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final ProposeAndAcceptClientTransfer command = mock(ProposeAndAcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeAndAcceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeAndAcceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for proposeAndAcceptTransfer().
     */
    @Test
    public void testProposeAndAcceptTransferInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final ProposeAndAcceptClientTransfer command = mock(ProposeAndAcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeAndAcceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeAndAcceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for proposeAndAcceptTransfer().
     */
    @Test
    public void testProposeAndAcceptTransferNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final ProposeAndAcceptClientTransfer command = mock(ProposeAndAcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeAndAcceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeAndAcceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.CLIENT_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for proposeAndAcceptTransfer().
     */
    @Test
    public void testProposeAndAcceptTransferUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final ProposeAndAcceptClientTransfer command = mock(ProposeAndAcceptClientTransfer.class);

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitClientService).executeCommand(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultClientId, "proposeAndAcceptTransfer", command, this.clientService.commandsCallback);

        try {
            this.clientService.proposeAndAcceptTransfer(this.defaultClientId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

}
