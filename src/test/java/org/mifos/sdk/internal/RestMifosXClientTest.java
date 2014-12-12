/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.internal.AuthenticationToken;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.RestMifosXClient;
import org.mifos.sdk.internal.RetrofitMifosService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import java.util.ArrayList;

/**
 * Test for {@link RestMifosXClient} and its various methods.
 */
public class RestMifosXClientTest {

    private RestAdapter restAdapter;
    private RetrofitMifosService retrofitMifosService;
    private MifosXProperties properties;
    private AuthenticationToken mockedAuthKey;
    private RestMifosXClient mifosXClient;

    /**
     * Setup all the components before testing.
     */
    @Before
    public void setup() {
        this.restAdapter = mock(RestAdapter.class);
        this.retrofitMifosService = mock(RetrofitMifosService.class);
        this.properties = MifosXProperties
                .url("http://demo.openmf.org/mifosng-provider/api/v1")
                .username("mifos")
                .password("password")
                .tenant("default")
                .build();
        this.mockedAuthKey = new AuthenticationToken("=hd$$34dd");
        this.mifosXClient = new RestMifosXClient(this.properties, this.restAdapter);
    }

    /**
     * Test for successful login.
     */
    @Test
    public void testSuccessfulLogin() {
        when(this.restAdapter.create(RetrofitMifosService.class)).thenReturn(this.retrofitMifosService);
        when(this.retrofitMifosService.authenticate(this.properties.getUsername(),
                this.properties.getPassword(), this.properties.getTenant())).thenReturn(this.mockedAuthKey);

        try {
            this.mifosXClient.login();

            Assert.assertNotNull(this.mifosXClient.getAuthenticationKey());
            Assert.assertEquals(this.mifosXClient.getAuthenticationKey(), mockedAuthKey.getAuthenticationToken());

            this.mifosXClient.logout();
        } catch (MifosXConnectException e) {
            Assert.fail();
        }
    }

    /**
     * Test to check the logged in status.
     */
    @Test
    public void testLoggedInStatus() {
        when(this.restAdapter.create(RetrofitMifosService.class)).thenReturn(this.retrofitMifosService);
        when(this.retrofitMifosService.authenticate(this.properties.getUsername(),
                this.properties.getPassword(), this.properties.getTenant())).thenReturn(this.mockedAuthKey);

        try {
            this.mifosXClient.login();

            Assert.assertTrue(this.mifosXClient.isLoggedIn());

            this.mifosXClient.logout();

            Assert.assertFalse(this.mifosXClient.isLoggedIn());
        } catch (MifosXConnectException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception.
     */
    @Test
    public void testNotConnectedException() {
        when(this.restAdapter.create(RetrofitMifosService.class)).thenReturn(this.retrofitMifosService);

        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitMifosService.authenticate(this.properties.getUsername(),
                this.properties.getPassword(), this.properties.getTenant())).thenThrow(error);

        try {
            this.mifosXClient.login();
            this.mifosXClient.logout();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test conversion error exception.
     */
    @Test
    public void testConversionException() {
        when(this.restAdapter.create(RetrofitMifosService.class)).thenReturn(this.retrofitMifosService);

        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitMifosService.authenticate(this.properties.getUsername(),
                this.properties.getPassword(), this.properties.getTenant())).thenThrow(error);

        try {
            this.mifosXClient.login();
            this.mifosXClient.logout();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNAUTHENTICATED.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNAUTHENTICATED} exception.
     */
    @Test
    public void testUnauthenticatedException() {
        when(this.restAdapter.create(RetrofitMifosService.class)).thenReturn(this.retrofitMifosService);

        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitMifosService.authenticate(this.properties.getUsername(),
                this.properties.getPassword(), this.properties.getTenant())).thenThrow(error);

        try {
            this.mifosXClient.login();
            this.mifosXClient.logout();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNAUTHENTICATED.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception.
     */
    @Test
    public void testUnknownException() {
        when(this.restAdapter.create(RetrofitMifosService.class)).thenReturn(this.retrofitMifosService);

        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitMifosService.authenticate(this.properties.getUsername(),
                this.properties.getPassword(), this.properties.getTenant())).thenThrow(error);

        try {
            this.mifosXClient.login();
            this.mifosXClient.logout();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }
    }

}
