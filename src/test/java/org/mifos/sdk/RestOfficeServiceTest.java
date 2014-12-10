/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.RetrofitOfficeService;
import org.mifos.sdk.office.domain.Office;
import org.mifos.sdk.office.internal.RestOfficeService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link RestOfficeService} and its various methods.
 */
public class RestOfficeServiceTest {

    private RestAdapter restAdapter;
    private RetrofitOfficeService retrofitOfficeService;
    private MifosXProperties properties;
    private String mockedAuthKey;
    private Office defaultOffice;
    private Long defaultOfficeId;
    private RestOfficeService officeService;

    /**
     * Setup all the components before testing.
     */
    @Before
    public void setup() {
        this.restAdapter = mock(RestAdapter.class);
        this.retrofitOfficeService = mock(RetrofitOfficeService.class);
        this.properties = MifosXProperties
                .url("http://demo.openmf.org/mifosng-provider/api/v1")
                .username("mifos")
                .password("password")
                .tenant("default")
                .build();
        this.mockedAuthKey = "=hd$$34dd";
        this.defaultOffice = Office
                .name("Head Office")
                .openingDate("10 December 2014")
                .parentId((long)1)
                .build();
        this.defaultOfficeId = (long)1;
        this.officeService = new RestOfficeService(this.properties, this.restAdapter,
                this.mockedAuthKey);

        when(this.restAdapter.create(RetrofitOfficeService.class)).thenReturn(this.retrofitOfficeService);
    }

    /**
     * Test for successful creation of an office.
     */
    @Test
    public void testCreateOffice() {
        final Office mockedOffice = mock(Office.class);

        when(mockedOffice.getOfficeId()).thenReturn(this.defaultOfficeId);
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),             this.defaultOffice)).thenReturn(mockedOffice);

        try {
            Long id = this.officeService.createOffice(defaultOffice);

            Assert.assertNotNull(id);
            Assert.assertEquals(id, this.defaultOfficeId);
        } catch (MifosXConnectException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for createOffice().
     */
    @Test
    public void testCreateOfficeNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),             this.defaultOffice)).thenThrow(error);

        try {
            this.officeService.createOffice(defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test for conversion error exception for createOffice().
     */
    @Test
    public void testCreateOfficeConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),             this.defaultOffice)).thenThrow(error);

        try {
            this.officeService.createOffice(defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_BASIC_AUTHENTICATION.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_BASIC_AUTHENTICATION} exception for createOffice().
     */
    @Test
    public void testCreateOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),             this.defaultOffice)).thenThrow(error);

        try {
            this.officeService.createOffice(defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_BASIC_AUTHENTICATION.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for createOffice().
     */
    @Test
    public void testCreateOfficeUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),             this.defaultOffice)).thenThrow(error);

        try {
            this.officeService.createOffice(defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }
    }

    /**
     * Test for successful retrieval of all offices.
     */
    @Test
    public void testFetchOffices() {
        final List officesList = Arrays.asList(defaultOffice, defaultOffice);

        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey, this.properties.getTenant()))
                .thenReturn(officesList);

         try {
             final List<Office> responseOffices = this.officeService.fetchOffices();

             Assert.assertNotNull(responseOffices);
             Assert.assertThat(responseOffices, equalTo(officesList));
         } catch (MifosXConnectException e) {
             Assert.fail();
         }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for fetchOffices().
     */
    @Test
    public void testFetchOfficesNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.officeService.fetchOffices();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test for conversion error exception for fetchOffices().
     */
    @Test
    public void testFetchOfficesConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.officeService.fetchOffices();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_BASIC_AUTHENTICATION.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_BASIC_AUTHENTICATION} exception for fetchOffices().
     */
    @Test
    public void testFetchOfficesInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.officeService.fetchOffices();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_BASIC_AUTHENTICATION.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for fetchOffices().
     */
    @Test
    public void testFetchOfficesUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.officeService.fetchOffices();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }
    }

    /**
     * Test for successful retrieval of an office.
     */
    @Test
    public void testFindOffice() {
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOfficeId)).thenReturn(defaultOffice);

        try {
            Office responseOffice = this.officeService.findOffice(this.defaultOfficeId);

            Assert.assertNotNull(responseOffice);
            Assert.assertThat(responseOffice, equalTo(this.defaultOffice));
        } catch (MifosXConnectException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for findOffice().
     */
    @Test
    public void testFindOfficeNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test for conversion error exception for findOffice().
     */
    @Test
    public void testFindOfficeConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_BASIC_AUTHENTICATION.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_BASIC_AUTHENTICATION} exception for findOffice().
     */
    @Test
    public void testFindOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_BASIC_AUTHENTICATION.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for findOffice().
     */
    @Test
    public void testFindOfficeUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for updateOffice().
     */
    @Test
    public void testUpdateOfficeNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitOfficeService).updateOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId, this.defaultOffice);

        try {
            this.officeService.updateOffice(this.defaultOfficeId, this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_BASIC_AUTHENTICATION} exception for updateOffice().
     */
    @Test
    public void testUpdateOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitOfficeService).updateOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId, this.defaultOffice);

        try {
            this.officeService.updateOffice(this.defaultOfficeId, this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_BASIC_AUTHENTICATION.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for findOffice().
     */
    @Test
    public void testUpdateOfficeUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitOfficeService).updateOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId, this.defaultOffice);

        try {
            this.officeService.updateOffice(this.defaultOfficeId, this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }
    }

}
