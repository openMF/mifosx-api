/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.office.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.office.domain.Office;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * Test for {@link RestOfficeService} and its various methods.
 */
public class RestOfficeServiceTest {

    private RetrofitOfficeService retrofitOfficeService;
    private MifosXProperties properties;
    private String mockedAuthKey;
    private Office defaultOffice;
    private Long defaultOfficeId;
    private RestOfficeService officeService;
    private String defaultDuplicateJSON;
    private String defaultDuplicateMessage;

    /**
     * Setup all the components before testing.
     */
    @Before
    public void setup() {
        final RestAdapter restAdapter = mock(RestAdapter.class);
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
                .openingDate(new Date())
                .locale("en")
                .dateFormat("dd MMMM yyyy")
                .parentId(1L)
                .build();
        this.defaultOfficeId = 1L;
        this.defaultOffice.setOfficeId(this.defaultOfficeId);
        this.officeService = new RestOfficeService(this.properties, restAdapter,
                this.mockedAuthKey);
        this.mockedAuthKey = "Basic " + this.mockedAuthKey;
        this.defaultDuplicateJSON = "{\"errors\":[{\"developerMessage\":\"some random message\"}]}";
        this.defaultDuplicateMessage = "some random message";

        when(restAdapter.create(RetrofitOfficeService.class)).thenReturn(this.retrofitOfficeService);
    }

    /**
     * Test for successful creation of an office.
     */
    @Test
    public void testCreateOffice() {
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(), this.defaultOffice)).thenReturn(this.defaultOffice);

        try {
            Long id = this.officeService.createOffice(this.defaultOffice);

            Assert.assertNotNull(id);
            Assert.assertEquals(id, this.defaultOfficeId);
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
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
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOffice)).thenThrow(error);

        try {
            this.officeService.createOffice(this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for createOffice().
     */
    @Test
    public void testCreateOfficeDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(),
                new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOffice)).thenThrow(error);

        try {
            this.officeService.createOffice(this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
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
            this.officeService.createOffice(this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for createOffice().
     */
    @Test
    public void testCreateOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.createOffice(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultOffice)).thenThrow(error);

        try {
            this.officeService.createOffice(this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
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
            this.officeService.createOffice(this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for successful retrieval of all offices.
     */
    @Test
    public void testFetchOffices() {
        final List<Office> officesList = Arrays.asList(this.defaultOffice, this.defaultOffice);

        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey,
                this.properties.getTenant())).thenReturn(officesList);

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
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey,
                this.properties.getTenant())).thenThrow(error);

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
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey,
                this.properties.getTenant())).thenThrow(error);

        try {
            this.officeService.fetchOffices();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for fetchOffices().
     */
    @Test
    public void testFetchOfficesInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey,
                this.properties.getTenant())).thenThrow(error);

        try {
            this.officeService.fetchOffices();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
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
        when(this.retrofitOfficeService.fetchOffices(this.mockedAuthKey,
                this.properties.getTenant())).thenThrow(error);

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
                this.defaultOfficeId)).thenReturn(this.defaultOffice);

        try {
            final Office responseOffice = this.officeService.findOffice(this.defaultOfficeId);

            Assert.assertNotNull(responseOffice);
            Assert.assertThat(responseOffice, equalTo(this.defaultOffice));
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
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
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for findOffice().
     */
    @Test
    public void testFindOfficeDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for conversion error exception for findOffice().
     */
    @Test
    public void testFindOfficeConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for findOffice().
     */
    @Test
    public void testFindOfficeNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch(MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.OFFICE_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for findOffice().
     */
    @Test
    public void testFindOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch(MifosXResourceException e) {
            Assert.fail();
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
        when(this.retrofitOfficeService.findOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId)).thenThrow(error);

        try {
            this.officeService.findOffice(this.defaultOfficeId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
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
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for updateOffice().
     */
    @Test
    public void testUpdateOfficeDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitOfficeService).updateOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId, this.defaultOffice);

        try {
            this.officeService.updateOffice(this.defaultOfficeId, this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for updateOffice().
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
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for updateOffice().
     */
    @Test
    public void testUpdateOfficeNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitOfficeService).updateOffice(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultOfficeId, this.defaultOffice);

        try {
            this.officeService.updateOffice(this.defaultOfficeId, this.defaultOffice);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.OFFICE_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for updateOffice().
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
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

}
