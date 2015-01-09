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
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.group.domain.Group;
import org.mifos.sdk.group.domain.PageableGroups;
import org.mifos.sdk.group.domain.commands.ActivateGroupCommand;
import org.mifos.sdk.group.domain.commands.AssignUnassignStaffCommand;
import org.mifos.sdk.group.domain.commands.AssignUpdateRoleCommand;
import org.mifos.sdk.group.domain.commands.AssociateDisassociateClientsCommand;
import org.mifos.sdk.group.domain.commands.CloseGroupCommand;
import org.mifos.sdk.group.domain.commands.GenerateCollectionSheetCommand;
import org.mifos.sdk.group.domain.commands.SaveCollectionSheetCommand;
import org.mifos.sdk.group.domain.commands.TransferClientsCommand;
import org.mifos.sdk.internal.ErrorCode;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private Long defaultRoleId;
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
            .officeId(1L)
            .active(false)
            .build();
        this.defaultGroupId = 1L;
        this.defaultGroup.setResourceId(this.defaultGroupId);
        this.defaultRoleId = 1L;
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
        when(this.retrofitGroupService.createGroup(this.mockedAuthKey, this.properties.getTenant(), this.defaultGroup)).thenReturn(this.defaultGroup);

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
        final List<Group> groupsList = Arrays.asList(this.defaultGroup, this.defaultGroup);
        final PageableGroups pageableGroups = new PageableGroups();
        pageableGroups.setClients(groupsList);
        final Map<String, Object> map = new HashMap<>();
        map.put("paged", true);

        when(this.retrofitGroupService.fetchGroups(this.mockedAuthKey,
            this.properties.getTenant(), map)).thenReturn(pageableGroups);

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

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for activateGroup().
     */
    @Test
    public void testActivateGroupNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final ActivateGroupCommand command = ActivateGroupCommand.locale("en").build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "activate", null, command);

        try {
            this.groupService.activateGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for activateGroup().
     */
    @Test
    public void testActivateGroupDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final ActivateGroupCommand command = ActivateGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "activate", null, command);

        try {
            this.groupService.activateGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for activateGroup().
     */
    @Test
    public void testActivateGroupInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final ActivateGroupCommand command = ActivateGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "activate", null, command);

        try {
            this.groupService.activateGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for activateGroup().
     */
    @Test
    public void testActivateGroupNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final ActivateGroupCommand command = ActivateGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "activate", null, command);

        try {
            this.groupService.activateGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for activateGroup().
     */
    @Test
    public void testActivateGroupUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final ActivateGroupCommand command = ActivateGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "activate", null, command);

        try {
            this.groupService.activateGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for associateClients().
     */
    @Test
    public void testAssociateClientsNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "associateClients", null, command);

        try {
            this.groupService.associateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for associateClients().
     */
    @Test
    public void testAssociateClientsDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "associateClients", null, command);

        try {
            this.groupService.associateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for associateClients().
     */
    @Test
    public void testAssociateClientsInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "associateClients", null, command);

        try {
            this.groupService.associateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for associateClients().
     */
    @Test
    public void testAssociateClientsNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "associateClients", null, command);

        try {
            this.groupService.associateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for associateClients().
     */
    @Test
    public void testAssociateClientsUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "associateClients", null, command);

        try {
            this.groupService.associateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for disassociateClients().
     */
    @Test
    public void testDisassociateClientsNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "disassociateClients", null, command);

        try {
            this.groupService.disassociateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for disassociateClients().
     */
    @Test
    public void testDisassociateClientsDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "disassociateClients", null, command);

        try {
            this.groupService.disassociateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for disassociateClients().
     */
    @Test
    public void testDisassociateClientsInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "disassociateClients", null, command);

        try {
            this.groupService.disassociateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for disassociateClients().
     */
    @Test
    public void testDisassociateClientsNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "disassociateClients", null, command);

        try {
            this.groupService.disassociateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for disassociateClients().
     */
    @Test
    public void testDisassociateClientsUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssociateDisassociateClientsCommand command = AssociateDisassociateClientsCommand.clientMembers(Arrays
            .asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "disassociateClients", null, command);

        try {
            this.groupService.disassociateClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for transferClients().
     */
    @Test
    public void testTransferClientsNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final TransferClientsCommand command = TransferClientsCommand.destinationGroupId(1L)
            .clients(Arrays.asList(1L, 2L, 3L)).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "transferClients", null, command);

        try {
            this.groupService.transferClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for transferClients().
     */
    @Test
    public void testTransferClientsDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final TransferClientsCommand command = TransferClientsCommand.destinationGroupId(1L)
            .clients(Arrays.asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "transferClients", null, command);

        try {
            this.groupService.transferClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for transferClients().
     */
    @Test
    public void testTransferClientsInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final TransferClientsCommand command = TransferClientsCommand.destinationGroupId(1L)
            .clients(Arrays.asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "transferClients", null, command);

        try {
            this.groupService.transferClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for transferClients().
     */
    @Test
    public void testTransferClientsNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final TransferClientsCommand command = TransferClientsCommand.destinationGroupId(1L)
            .clients(Arrays.asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "transferClients", null, command);

        try {
            this.groupService.transferClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for transferClients().
     */
    @Test
    public void testTransferClientsUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final TransferClientsCommand command = TransferClientsCommand.destinationGroupId(1L)
            .clients(Arrays.asList(1L, 2L, 3L)).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "transferClients", null, command);

        try {
            this.groupService.transferClients(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for generateCollectionSheet().
     */
    @Test
    public void testGenerateCollectionSheetNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final GenerateCollectionSheetCommand command = GenerateCollectionSheetCommand.locale("en")
            .calendarId(1L).dateFormat("dd/MM/yyyy").transactionDate(date).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "generateCollectionSheet", null, command);

        try {
            this.groupService.generateCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for generateCollectionSheet().
     */
    @Test
    public void testGenerateCollectionSheetDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final GenerateCollectionSheetCommand command = GenerateCollectionSheetCommand.locale("en")
            .calendarId(1L).dateFormat("dd/MM/yyyy").transactionDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "generateCollectionSheet", null, command);

        try {
            this.groupService.generateCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for generateCollectionSheet().
     */
    @Test
    public void testGenerateCollectionSheetInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final GenerateCollectionSheetCommand command = GenerateCollectionSheetCommand.locale("en")
            .calendarId(1L).dateFormat("dd/MM/yyyy").transactionDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "generateCollectionSheet", null, command);

        try {
            this.groupService.generateCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for generateCollectionSheet().
     */
    @Test
    public void testGenerateCollectionSheetNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final GenerateCollectionSheetCommand command = GenerateCollectionSheetCommand.locale("en")
            .calendarId(1L).dateFormat("dd/MM/yyyy").transactionDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "generateCollectionSheet", null, command);

        try {
            this.groupService.generateCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for generateCollectionSheet().
     */
    @Test
    public void testGenerateCollectionSheetUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final GenerateCollectionSheetCommand command = GenerateCollectionSheetCommand.locale("en")
            .calendarId(1L).dateFormat("dd/MM/yyyy").transactionDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "generateCollectionSheet", null, command);

        try {
            this.groupService.generateCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for saveCollectionSheet().
     */
    @Test
    public void testSaveCollectionSheetNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final SaveCollectionSheetCommand command = SaveCollectionSheetCommand.calendarId(1L)
            .locale("en").dateFormat("dd/MM/yyyy").transactionDate(date)
            .actualDisbursementDate(date).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "saveCollectionSheet", null, command);

        try {
            this.groupService.saveCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for saveCollectionSheet().
     */
    @Test
    public void testSaveCollectionSheetDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final SaveCollectionSheetCommand command = SaveCollectionSheetCommand.calendarId(1L)
            .locale("en").dateFormat("dd/MM/yyyy").transactionDate(date)
            .actualDisbursementDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "saveCollectionSheet", null, command);

        try {
            this.groupService.saveCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for saveCollectionSheet().
     */
    @Test
    public void testSaveCollectionSheetInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final SaveCollectionSheetCommand command = SaveCollectionSheetCommand.calendarId(1L)
            .locale("en").dateFormat("dd/MM/yyyy").transactionDate(date)
            .actualDisbursementDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "saveCollectionSheet", null, command);

        try {
            this.groupService.saveCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for saveCollectionSheet().
     */
    @Test
    public void testSaveCollectionSheetNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final SaveCollectionSheetCommand command = SaveCollectionSheetCommand.calendarId(1L)
            .locale("en").dateFormat("dd/MM/yyyy").transactionDate(date)
            .actualDisbursementDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "saveCollectionSheet", null, command);

        try {
            this.groupService.saveCollectionSheet(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for saveCollectionSheet().
     */
    @Test
    public void testSaveCollectionSheetUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final Date date = new GregorianCalendar(2015, 1, 13).getTime();
        final SaveCollectionSheetCommand command = SaveCollectionSheetCommand.calendarId(1L)
            .locale("en").dateFormat("dd/MM/yyyy").transactionDate(date)
            .actualDisbursementDate(date).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "saveCollectionSheet", null, command);

        try {
            this.groupService.saveCollectionSheet(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignStaff", null, command);

        try {
            this.groupService.unassignStaff(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignStaff", null, command);

        try {
            this.groupService.unassignStaff(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignStaff", null, command);

        try {
            this.groupService.unassignStaff(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignStaff", null, command);

        try {
            this.groupService.unassignStaff(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for unassignStaff().
     */
    @Test
    public void testUnassignStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignStaff", null, command);

        try {
            this.groupService.unassignStaff(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignStaff", null, command);

        try {
            this.groupService.assignStaff(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignStaff", null, command);

        try {
            this.groupService.assignStaff(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignStaff", null, command);

        try {
            this.groupService.assignStaff(this.defaultGroupId, command);

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
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignStaff", null, command);

        try {
            this.groupService.assignStaff(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for assignStaff().
     */
    @Test
    public void testAssignStaffUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUnassignStaffCommand command = AssignUnassignStaffCommand.staffId(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignStaff", null, command);

        try {
            this.groupService.assignStaff(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for closeGroup().
     */
    @Test
    public void testCloseGroupNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final CloseGroupCommand command = CloseGroupCommand.locale("en").build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "close", null, command);

        try {
            this.groupService.closeGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for closeGroup().
     */
    @Test
    public void testCloseGroupDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final CloseGroupCommand command = CloseGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "close", null, command);

        try {
            this.groupService.closeGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for closeGroup().
     */
    @Test
    public void testCloseGroupInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final CloseGroupCommand command = CloseGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "close", null, command);

        try {
            this.groupService.closeGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for closeGroup().
     */
    @Test
    public void testCloseGroupNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final CloseGroupCommand command = CloseGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "close", null, command);

        try {
            this.groupService.closeGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for closeGroup().
     */
    @Test
    public void testCloseGroupUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final CloseGroupCommand command = CloseGroupCommand.locale("en").build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "close", null, command);

        try {
            this.groupService.closeGroup(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for assignRole().
     */
    @Test
    public void testAssignRoleNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignRole", null, command);

        try {
            this.groupService.assignRole(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for assignRole().
     */
    @Test
    public void testAssignRoleDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignRole", null, command);

        try {
            this.groupService.assignRole(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for assignRole().
     */
    @Test
    public void testAssignRoleInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignRole", null, command);

        try {
            this.groupService.assignRole(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for assignRole().
     */
    @Test
    public void testAssignRoleNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignRole", null, command);

        try {
            this.groupService.assignRole(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for assignRole().
     */
    @Test
    public void testAssignRoleUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "assignRole", null, command);

        try {
            this.groupService.assignRole(this.defaultGroupId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for updateRole().
     */
    @Test
    public void testUpdateRoleNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "updateRole", this.defaultRoleId, command);

        try {
            this.groupService.updateRole(this.defaultGroupId, this.defaultRoleId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for updateRole().
     */
    @Test
    public void testUpdateRoleDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "updateRole", this.defaultRoleId, command);

        try {
            this.groupService.updateRole(this.defaultGroupId, this.defaultRoleId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for updateRole().
     */
    @Test
    public void testUpdateRoleInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "updateRole", this.defaultRoleId, command);

        try {
            this.groupService.updateRole(this.defaultGroupId, this.defaultRoleId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for updateRole().
     */
    @Test
    public void testUpdateRoleNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "updateRole", this.defaultRoleId, command);

        try {
            this.groupService.updateRole(this.defaultGroupId, this.defaultRoleId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for updateRole().
     */
    @Test
    public void testUpdateRoleUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));
        final AssignUpdateRoleCommand command = AssignUpdateRoleCommand.role(1L).build();

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "updateRole", this.defaultRoleId, command);

        try {
            this.groupService.updateRole(this.defaultGroupId, this.defaultRoleId, command);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for {@link ErrorCode#NOT_CONNECTED} exception for unassignRole().
     */
    @Test
    public void testUnassignRoleNotConnectedException() {
        final RetrofitError error = mock(RetrofitError.class);

        when(error.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignRole", this.defaultRoleId, null);

        try {
            this.groupService.unassignRole(this.defaultGroupId, this.defaultRoleId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.NOT_CONNECTED.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for duplicate exception for unassignRole().
     */
    @Test
    public void testUnassignRoleDuplicateException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 403, "", new ArrayList<Header>(), new TypedString(this.defaultDuplicateJSON));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignRole", this.defaultRoleId, null);

        try {
            this.groupService.unassignRole(this.defaultGroupId, this.defaultRoleId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), this.defaultDuplicateMessage);
        }
    }

    /**
     * Test for {@link ErrorCode#INVALID_AUTHENTICATION_TOKEN} exception for unassignRole().
     */
    @Test
    public void testUnassignRoleInvalidAuthKeyException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 401, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignRole", this.defaultRoleId, null);

        try {
            this.groupService.unassignRole(this.defaultGroupId, this.defaultRoleId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.INVALID_AUTHENTICATION_TOKEN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

    /**
     * Test for not found exception for unassignRole().
     */
    @Test
    public void testUnassignRoleNotFoundException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 404, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignRole", this.defaultRoleId, null);

        try {
            this.groupService.unassignRole(this.defaultGroupId, this.defaultRoleId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.fail();
        } catch (MifosXResourceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.GROUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * Test for {@link ErrorCode#UNKNOWN} exception for unassignRole().
     */
    @Test
    public void testUnassignRoleUnknownException() {
        final RetrofitError error = mock(RetrofitError.class);
        final Response response = new Response("", 503, "", new ArrayList<Header>(), new TypedString(""));

        when(error.getResponse()).thenReturn(response);
        doThrow(error).when(this.retrofitGroupService).executeCommand(this.mockedAuthKey,
            this.properties.getTenant(), this.defaultGroupId, "unassignRole", this.defaultRoleId, null);

        try {
            this.groupService.unassignRole(this.defaultGroupId, this.defaultRoleId);

            Assert.fail();
        } catch (MifosXConnectException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(e.getMessage(), ErrorCode.UNKNOWN.getMessage());
        } catch (MifosXResourceException e) {
            Assert.fail();
        }
    }

}
