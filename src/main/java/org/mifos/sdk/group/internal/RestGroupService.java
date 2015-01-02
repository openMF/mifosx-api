/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.internal;

import com.google.common.base.Preconditions;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.group.GroupService;
import org.mifos.sdk.group.domain.Group;
import org.mifos.sdk.group.domain.GroupAccountsSummary;
import org.mifos.sdk.group.domain.PageableGroups;
import org.mifos.sdk.internal.ErrorCode;
import org.mifos.sdk.internal.ServerResponseUtil;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements {@link GroupService} and the inner lying methods
 * for communication with the Groups API.
 */
public class RestGroupService implements GroupService {

    private final MifosXProperties connectionProperties;
    private final RestAdapter restAdapter;
    private final String authenticationKey;

    /**
     * Constructs a new instance of {@link RestGroupService} with the
     * provided properties, adapter and authKey.
     * @param properties the {@link MifosXProperties} with the API URL endpoint
     * @param adapter the rest adapter used for creating Retrofit services
     * @param authKey the authentication key obtain by calling {@link org.mifos.sdk.MifosXClient#login()}
     */
    public RestGroupService(final MifosXProperties properties,
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
     * Creates a new group.
     * @param group the {@link Group} to create
     * @return a {@link Group} with the response parameters
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public Group createGroup(final Group group) throws MifosXConnectException,
        MifosXResourceException {
        Preconditions.checkNotNull(group);
        final RetrofitGroupService groupService = this.restAdapter.create(RetrofitGroupService.class);
        Group responseGroup = null;
        try {
            responseGroup = groupService.createGroup(this.authenticationKey,
                this.connectionProperties.getTenant(), group);
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
        return responseGroup;
    }

    /**
     * Retrieves all available groups.
     * @param queryMap a {@link Map} with all the query parameters
     * @return a {@link PageableGroups} with the list of {@link Group}s
     * @throws MifosXConnectException
     */
    public PageableGroups fetchGroups(Map<String, Object> queryMap) throws
        MifosXConnectException {
        final RetrofitGroupService groupService = this.restAdapter.create(RetrofitGroupService.class);
        PageableGroups groups = null;
        if (queryMap == null) {
            queryMap = new HashMap<String, Object>();
        }
        if (queryMap.isEmpty() && !queryMap.containsKey("paged")) {
            queryMap.put("paged", true);
        }
        try {
            groups = groupService.fetchGroups(this.authenticationKey,
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
        return groups;
    }

    /**
     * Retrieves oe particular group.
     * @param groupId the group ID
     * @param queryMap a {@link Map} with all the query parameters
     * @return the {@link Group} with all the details of the searched group
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public Group findGroup(final Long groupId, final Map<String, Object> queryMap) throws
        MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(groupId);
        final RetrofitGroupService groupService = this.restAdapter.create(RetrofitGroupService.class);
        Group responseGroup = null;
        try {
            responseGroup = groupService.findGroup(this.authenticationKey,
                this.connectionProperties.getTenant(), groupId, queryMap);
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
                throw new MifosXResourceException(ErrorCode.GROUP_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return responseGroup;
    }

    /**
     * Retrieves the accounts summary of a group.
     * @param groupId the group ID
     * @param fields a {@link List} of fields to include in the response
     * @return the {@link Group} with its accounts summary
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public GroupAccountsSummary findGroupsAccountsSummary(final Long groupId,
                                                          final List<String> fields) throws
        MifosXConnectException, MifosXResourceException {
        Preconditions.checkNotNull(groupId);
        final RetrofitGroupService groupService = this.restAdapter.create(RetrofitGroupService.class);
        final String allFields = null;
        if (fields != null) {
            for (int i = 0; i < fields.size(); ++i) {
                allFields.concat(fields.get(i));
                if (i != (fields.size() - 1)) {
                    allFields.concat(",");
                }
            }
        }
        GroupAccountsSummary groupAccountsSummary = null;
        try {
            groupAccountsSummary = groupService.findGroupsAccountsSummary(this.authenticationKey,
                this.connectionProperties.getTenant(), groupId, allFields);
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
                throw new MifosXResourceException(ErrorCode.GROUP_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
        return groupAccountsSummary;
    }

    /**
     * Updates a particular group.
     * @param groupId the group ID
     * @param group the {@link Group} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void updateGroup(final Long groupId, final Group group) throws MifosXConnectException,
        MifosXResourceException {
        Preconditions.checkNotNull(groupId);
        Preconditions.checkNotNull(group);
        final RetrofitGroupService groupService = this.restAdapter.create(RetrofitGroupService.class);
        try {
            groupService.updateGroup(this.authenticationKey, this.connectionProperties.getTenant(),
                groupId, group);
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
                throw new MifosXResourceException(ErrorCode.GROUP_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

    /**
     * Deletes a particular group.
     * @param groupId the group ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    public void deleteGroup(final Long groupId) throws MifosXConnectException,
        MifosXResourceException {
        Preconditions.checkNotNull(groupId);
        final RetrofitGroupService groupService = this.restAdapter.create(RetrofitGroupService.class);
        try {
            groupService.deleteGroup(this.authenticationKey, this.connectionProperties.getTenant(),
                groupId);
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
                throw new MifosXResourceException(ErrorCode.GROUP_NOT_FOUND);
            } else {
                throw new MifosXConnectException(ErrorCode.UNKNOWN);
            }
        }
    }

}
