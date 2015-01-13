/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.staff.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.staff.domain.Staff;
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
 * Test for {@link RestStaffService} and its various methods.
 */
public class RestStaffServiceTest {

    private RetrofitStaffService retrofitStaffService;
    private MifosXProperties properties;
    private String mockedAuthKey;
    private Staff defaultStaff;
    private Long defaultStaffId;
    private RestStaffService staffService;
    private String defaultDuplicateJSON;
    private String defaultDuplicateMessage;
    private String defaultStatus;

    @Before
    public void setup() {
        final RestAdapter restAdapter = mock(RestAdapter.class);
        this.retrofitStaffService = mock(RetrofitStaffService.class);
        this.properties = MifosXProperties
                .url("http://demo.openmf.org/mifosng-provider/api/v1")
                .username("mifos")
                .password("password")
                .tenant("default")
                .build();
        this.mockedAuthKey = "=hd$$34dd";
        this.defaultStaff = Staff
                .officeId(1L)
                .firstname("Jacob")
                .lastname("Davis")
                .build();
        this.defaultStaffId = 1L;
        this.staffService = new RestStaffService(this.properties, restAdapter,
                this.mockedAuthKey);
        this.mockedAuthKey = "Basic " + this.mockedAuthKey;
        this.defaultDuplicateJSON = "{\"errors\":[{\"developerMessage\":\"some random message\"}]}";
        this.defaultDuplicateMessage = "some random message";
        this.defaultStatus = "all";

        when(restAdapter.create(RetrofitStaffService.class)).thenReturn(this.retrofitStaffService);
    }

    /**
     * Test for successful creation of a staff.
     */
    @Test
    public void testCreateStaff() {
        when(this.retrofitStaffService.createStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaff)).thenReturn(this.defaultStaff);

        try {
            final Staff staff = this.staffService.createStaff(this.defaultStaff);

            Assert.assertNotNull(staff);
            Assert.assertThat(staff, equalTo(this.defaultStaff));
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for createStaff().
     */
    @Test
    public void testCreateStaffNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitStaffService.createStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaff)).thenThrow(error);

        try {
            this.staffService.createStaff(this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for conversion error exception for createStaff().
     */
    @Test
    public void testCreateStaffConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitStaffService.createStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaff)).thenThrow(error);

        try {
            this.staffService.createStaff(this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for createStaff().
     */
    @Test
    public void testCreateStaffDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(),
                new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.createStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaff)).thenThrow(error);

        try {
            this.staffService.createStaff(this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for createStaff().
     */
    @Test
    public void testCreateStaffInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.createStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaff)).thenThrow(error);

        try {
            this.staffService.createStaff(this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for createStaff().
     */
    @Test
    public void testCreateStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.createStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaff)).thenThrow(error);

        try {
            this.staffService.createStaff(this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test to retrieve all available staff.
     */
    @Test
    public void testFetchStaff() {
        final List<Staff> staffList = Arrays.asList(this.defaultStaff, this.defaultStaff);

        when(this.retrofitStaffService.fetchStaff(this.mockedAuthKey, this.properties.getTenant()))
                .thenReturn(staffList);

        try {
            final List<Staff> responseStaff = this.staffService.fetchStaff();

            Assert.assertNotNull(responseStaff);
            Assert.assertThat(responseStaff, equalTo(staffList));
        } catch (MifosXConnectException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for fetchStaff().
     */
    @Test
    public void testFetchStaffNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitStaffService.fetchStaff(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.staffService.fetchStaff();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test for conversion error exception for fetchStaff().
     */
    @Test
    public void testFetchStaffConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitStaffService.fetchStaff(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.staffService.fetchStaff();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for fetchStaff().
     */
    @Test
    public void testFetchStaffInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.fetchStaff(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.staffService.fetchStaff();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for fetchStaff().
     */
    @Test
    public void testFetchStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.fetchStaff(this.mockedAuthKey, this.properties.getTenant()))
                .thenThrow(error);

        try {
            this.staffService.fetchStaff();

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }

    }

    /**
     * Test to retrieve one particular staff.
     */
    @Test
    public void testFindStaff() {
        when(this.retrofitStaffService.findStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaffId)).thenReturn(this.defaultStaff);

        try {
            final Staff responseStaff = this.staffService.findStaff(this.defaultStaffId);

            Assert.assertNotNull(responseStaff);
            Assert.assertThat(responseStaff, equalTo(this.defaultStaff));
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for findStaff().
     */
    @Test
    public void testFindStaffNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitStaffService.findStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaffId)).thenThrow(error);

        try {
            this.staffService.findStaff(this.defaultStaffId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for conversion error exception for findStaff().
     */
    @Test
    public void testFindStaffConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitStaffService.findStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaffId)).thenThrow(error);

        try {
            this.staffService.findStaff(this.defaultStaffId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for findStaff().
     */
    @Test
    public void testFindStaffDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(),
                new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaffId)).thenThrow(error);

        try {
            this.staffService.findStaff(this.defaultStaffId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for findStaff().
     */
    @Test
    public void testFindStaffInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaffId)).thenThrow(error);

        try {
            this.staffService.findStaff(this.defaultStaffId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for findStaff().
     */
    @Test
    public void testFindStaffNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaffId)).thenThrow(error);

        try {
            this.staffService.findStaff(this.defaultStaffId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.STAFF_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for findStaff().
     */
    @Test
    public void testFindStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaff(this.mockedAuthKey, this.properties.getTenant(),
                this.defaultStaffId)).thenThrow(error);

        try {
            this.staffService.findStaff(this.defaultStaffId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test to retrieve staff by their status.
     */
    @Test
    public void testFindStaffByStatus() {
        final List<Staff> officesList = Arrays.asList(this.defaultStaff, this.defaultStaff);

        when(this.retrofitStaffService.findStaffByStatus(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStatus)).thenReturn(officesList);

        try {
            final List<Staff> responseStaff = this.staffService.findStaffByStatus(this.defaultStatus);

            Assert.assertNotNull(responseStaff);
            Assert.assertThat(responseStaff, equalTo(officesList));
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for findStaffByStatus().
     */
    @Test
    public void testFindStaffByStatusNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitStaffService.findStaffByStatus(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStatus)).thenThrow(error);

        try {
            this.staffService.findStaffByStatus(this.defaultStatus);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for conversion error exception for findStaffByStatus().
     */
    @Test
    public void testFindStaffByStatusConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitStaffService.findStaffByStatus(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStatus)).thenThrow(error);

        try {
            this.staffService.findStaffByStatus(this.defaultStatus);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for findStaffByStatus().
     */
    @Test
    public void testFindStaffByStatusDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(),
                new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaffByStatus(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStatus)).thenThrow(error);

        try {
            this.staffService.findStaffByStatus(this.defaultStatus);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for findStaffByStatus().
     */
    @Test
    public void testFindStaffByStatusInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaffByStatus(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStatus)).thenThrow(error);

        try {
            this.staffService.findStaffByStatus(this.defaultStatus);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for findStaffByStatus().
     */
    @Test
    public void testFindStaffByStatusNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaffByStatus(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStatus)).thenThrow(error);

        try {
            this.staffService.findStaffByStatus(this.defaultStatus);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.STAFF_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for findStaffByStatus().
     */
    @Test
    public void testFindStaffByStatusUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitStaffService.findStaffByStatus(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStatus)).thenThrow(error);

        try {
            this.staffService.findStaffByStatus(this.defaultStatus);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for updateStaff().
     */
    @Test
    public void testUpdateStaffNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitStaffService).updateStaff(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStaffId, this.defaultStaff);

        try {
            this.staffService.updateStaff(this.defaultStaffId, this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for updateStaff().
     */
    @Test
    public void testUpdateStaffDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitStaffService).updateStaff(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStaffId, this.defaultStaff);

        try {
            this.staffService.updateStaff(this.defaultStaffId, this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for updateStaff().
     */
    @Test
    public void testUpdateStaffInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitStaffService).updateStaff(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStaffId, this.defaultStaff);

        try {
            this.staffService.updateStaff(this.defaultStaffId, this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for updateStaff().
     */
    @Test
    public void testUpdateStaffNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitStaffService).updateStaff(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStaffId, this.defaultStaff);

        try {
            this.staffService.updateStaff(this.defaultStaffId, this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.STAFF_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for updateStaff().
     */
    @Test
    public void testUpdateStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitStaffService).updateStaff(this.mockedAuthKey,
                this.properties.getTenant(), this.defaultStaffId, this.defaultStaff);

        try {
            this.staffService.updateStaff(this.defaultStaffId, this.defaultStaff);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

}
