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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link RestClientService} and its various methods.
 */
public class RestClientServiceTest {

    private RestAdapter restAdapter;
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
        this.restAdapter = mock(RestAdapter.class);
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
        this.clientService = new RestClientService(this.properties, this.restAdapter,
                this.mockedAuthKey);
        this.mockedAuthKey = "Basic " + this.mockedAuthKey;
        this.defaultDuplicateJSON = "{\"developerMessage\": \"some random message\"}";
        this.defaultDuplicateMessage = "some random message";

        when(this.restAdapter.create(RetrofitClientService.class)).thenReturn(this.retrofitClientService);
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

}
