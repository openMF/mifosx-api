/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.group.domain.Group;
import org.mifos.sdk.group.domain.PageableGroups;
import org.mifos.sdk.internal.ErrorCode;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * Test for {@link RestGroupService} and its various methods.
 */
public class RestGroupServiceTest {

    private RetrofitGroupService retrofitGroupService;
    private MifosXProperties properties;
    private String mockedAuthKey;
    private Group defaultGroup;
    private Long defaultGroupId;
    private RestGroupService groupService;
    private String defaultDuplicateJSON;
    private String defaultDuplicateMessage;

    /**
     * Setup all the components before testing.
     */
    @Before
    public void setup() {
        final RestAdapter restAdapter = mock(RestAdapter.class);
        this.retrofitGroupService = mock(RetrofitGroupService.class);
        this.properties = MifosXProperties
            .url("http://demo.openmf.org/mifosng-provider/api/v1")
            .username("mifos")
            .password("password")
            .tenant("default")
            .build();
        this.mockedAuthKey = "=hd$$34dd";
        this.defaultGroup = Group
            .name("Test Group")
            .officeId((long)1)
            .active(false)
            .build();
        this.defaultGroupId = (long)1;
        this.groupService = new RestGroupService(this.properties, restAdapter,
            this.mockedAuthKey);
        this.mockedAuthKey = "Basic " + this.mockedAuthKey;
        this.defaultDuplicateJSON = "{\"errors\":[{\"developerMessage\":\"some random message\"}]}";
        this.defaultDuplicateMessage = "some random message";

        when(restAdapter.create(RetrofitGroupService.class)).thenReturn(this.retrofitGroupService);
    }

    /**
     * Test for successful creation of an group.
     */
    @Test
    public void testCreateGroup() {
        final Group mockedGroup = mock(Group.class);

        when(mockedGroup.getResourceId()).thenReturn(this.defaultGroupId);
        when(this.retrofitGroupService.createGroup(this.mockedAuthKey, this.properties.getTenant(), this.defaultGroup)).thenReturn(mockedGroup);

        try {
            Long id = this.groupService.createGroup(this.defaultGroup).getResourceId();

            Assert.assertNotNull(id);
            Assert.assertEquals(id, this.defaultGroupId);
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for createGroup().
     */
    @Test
    public void testCreateGroupNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitGroupService.createGroup(this.mockedAuthKey, this.properties.getTenant(),
            this.defaultGroup)).thenThrow(error);

        try {
            this.groupService.createGroup(this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for createGroup().
     */
    @Test
    public void testCreateGroupDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(),
            new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.createGroup(this.mockedAuthKey, this.properties.getTenant(),
            this.defaultGroup)).thenThrow(error);

        try {
            this.groupService.createGroup(this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for conversion error exception for createGroup().
     */
    @Test
    public void testCreateGroupConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitGroupService.createGroup(this.mockedAuthKey, this.properties.getTenant(), this.defaultGroup)).thenThrow(error);

        try {
            this.groupService.createGroup(this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for createGroup().
     */
    @Test
    public void testCreateGroupInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.createGroup(this.mockedAuthKey, this.properties.getTenant(),
            this.defaultGroup)).thenThrow(error);

        try {
            this.groupService.createGroup(this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for createGroup().
     */
    @Test
    public void testCreateGroupUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.createGroup(this.mockedAuthKey, this.properties.getTenant(), this.defaultGroup)).thenThrow(error);

        try {
            this.groupService.createGroup(this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for successful retrieval of all groups.
     */
    @Test
    public void testFetchGroups() {
        final PageableGroups mockedPageableGroups = mock(PageableGroups.class);
        final List<Group> groupsList = Arrays.asList(this.defaultGroup, this.defaultGroup);
        final Map<String, Object> map = new HashMap<>();
        map.put("paged", true);

        when(mockedPageableGroups.getGroups()).thenReturn(groupsList);
        when(this.retrofitGroupService.fetchGroups(this.mockedAuthKey,
            this.properties.getTenant(), map)).thenReturn(mockedPageableGroups);

        try {
            final List<Group> responseGroups = this.groupService.fetchGroups(null).getGroups();

            Assert.assertNotNull(responseGroups);
            Assert.assertThat(responseGroups, equalTo(groupsList));
        } catch (MifosXConnectException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for fetchGroups().
     */
    @Test
    public void testFetchOfficesNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Map<String, Object> map = new HashMap<>();
        map.put("paged", true);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitGroupService.fetchGroups(this.mockedAuthKey,
            this.properties.getTenant(), map)).thenThrow(error);

        try {
            this.groupService.fetchGroups(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        }
    }

    /**
     * Test for conversion error exception for fetchGroups().
     */
    @Test
    public void testFetchOfficesConversionException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Map<String, Object> map = new HashMap<>();
        map.put("paged", true);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitGroupService.fetchGroups(this.mockedAuthKey,
            this.properties.getTenant(), map)).thenThrow(error);

        try {
            this.groupService.fetchGroups(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for fetchGroups().
     */
    @Test
    public void testFetchOfficesInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final Map<String, Object> map = new HashMap<>();
        map.put("paged", true);

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.fetchGroups(this.mockedAuthKey,
            this.properties.getTenant(), map)).thenThrow(error);

        try {
            this.groupService.fetchGroups(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for fetchGroups().
     */
    @Test
    public void testFetchOfficesUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final Map<String, Object> map = new HashMap<>();
        map.put("paged", true);

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.fetchGroups(this.mockedAuthKey,
            this.properties.getTenant(), map)).thenThrow(error);

        try {
            this.groupService.fetchGroups(null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        }
    }

    /**
     * Test for successful retrieval of an group.
     */
    @Test
    public void testFindGroup() {
        when(this.retrofitGroupService.findGroup(this.mockedAuthKey, this.properties.getTenant(),
            this.defaultGroupId, null)).thenReturn(this.defaultGroup);

        try {
            final Group responseGroup = this.groupService.findGroup(this.defaultGroupId, null);

            Assert.assertNotNull(responseGroup);
            Assert.assertThat(responseGroup, equalTo(this.defaultGroup));
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for findGroup().
     */
    @Test
    public void testFindGroupNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(this.retrofitGroupService.findGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, null)).thenThrow(error);

        try {
            this.groupService.findGroup(this.defaultGroupId, null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for findGroup().
     */
    @Test
    public void testFindGroupDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.findGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, null)).thenThrow(error);

        try {
            this.groupService.findGroup(this.defaultGroupId, null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for conversion error exception for findGroup().
     */
    @Test
    public void testFindGroupConversionException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(this.retrofitGroupService.findGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, null)).thenThrow(error);

        try {
            this.groupService.findGroup(this.defaultGroupId, null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for findGroup().
     */
    @Test
    public void testFindGroupNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.findGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, null)).thenThrow(error);

        try {
            this.groupService.findGroup(this.defaultGroupId, null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch(MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for findGroup().
     */
    @Test
    public void testFindGroupInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.findGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, null)).thenThrow(error);

        try {
            this.groupService.findGroup(this.defaultGroupId, null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch(MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for findGroup().
     */
    @Test
    public void testFindGroupUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        when(this.retrofitGroupService.findGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, null)).thenThrow(error);

        try {
            this.groupService.findGroup(this.defaultGroupId, null);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for updateGroup().
     */
    @Test
    public void testUpdateOfficeNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).updateGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, this.defaultGroup);

        try {
            this.groupService.updateGroup(this.defaultGroupId, this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for updateGroup().
     */
    @Test
    public void testUpdateOfficeDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).updateGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, this.defaultGroup);

        try {
            this.groupService.updateGroup(this.defaultGroupId, this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for updateGroup().
     */
    @Test
    public void testUpdateOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).updateGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, this.defaultGroup);

        try {
            this.groupService.updateGroup(this.defaultGroupId, this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for updateGroup().
     */
    @Test
    public void testUpdateOfficeNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).updateGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, this.defaultGroup);

        try {
            this.groupService.updateGroup(this.defaultGroupId, this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for updateGroup().
     */
    @Test
    public void testUpdateOfficeUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).updateGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, this.defaultGroup);

        try {
            this.groupService.updateGroup(this.defaultGroupId, this.defaultGroup);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for deleteGroup().
     */
    @Test
    public void testDeleteOfficeNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).deleteGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId);

        try {
            this.groupService.deleteGroup(this.defaultGroupId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for deleteGroup().
     */
    @Test
    public void testDeleteOfficeDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).deleteGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId);

        try {
            this.groupService.deleteGroup(this.defaultGroupId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for deleteGroup().
     */
    @Test
    public void testDeleteOfficeInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).deleteGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId);

        try {
            this.groupService.deleteGroup(this.defaultGroupId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for deleteGroup().
     */
    @Test
    public void testDeleteOfficeNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).deleteGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId);

        try {
            this.groupService.deleteGroup(this.defaultGroupId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for deleteGroup().
     */
    @Test
    public void testDeleteOfficeUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).deleteGroup(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId);

        try {
            this.groupService.deleteGroup(this.defaultGroupId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

}
