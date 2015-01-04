/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group;

import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.group.domain.Group;
import org.mifos.sdk.group.domain.GroupAccountsSummary;
import org.mifos.sdk.group.domain.PageableGroups;

import java.util.List;
import java.util.Map;

/**
 * Interface to communicate with the Groups API.
 */
public interface GroupService {

    /**
     * Creates a new group.
     * @param group the {@link Group} to create
     * @return a {@link Group} with the response parameters
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Group createGroup(Group group) throws MifosXConnectException, MifosXResourceException;

    /**
     * Retrieves all available groups.
     * @param queryMap a {@link Map} with all the query parameters
     * @return a {@link PageableGroups} with the list of {@link Group}s
     * @throws MifosXConnectException
     */
    PageableGroups fetchGroups(Map<String, Object> queryMap) throws MifosXConnectException;

    /**
     * Retrieves oe particular group.
     * @param groupId the group ID
     * @param queryMap a {@link Map} with all the query parameters
     * @return the {@link Group} with all the details of the searched group
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    Group findGroup(Long groupId, Map<String, Object> queryMap) throws MifosXConnectException,
        MifosXResourceException;

    /**
     * Retrieves the accounts summary of a group.
     * @param groupId the group ID
     * @param fields a {@link List} of fields to include in the response
     * @return the {@link Group} with its accounts summary
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    GroupAccountsSummary findGroupsAccountsSummary(Long groupId, List<String> fields) throws
        MifosXConnectException, MifosXResourceException;

    /**
     * Updates a particular group.
     * @param groupId the group ID
     * @param group the {@link Group} object with all the changes to be made
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void updateGroup(Long groupId, Group group) throws MifosXConnectException,
        MifosXResourceException;

    /**
     * Deletes a particular group.
     * @param groupId the group ID
     * @throws MifosXConnectException
     * @throws MifosXResourceException
     */
    void deleteGroup(Long groupId) throws MifosXConnectException, MifosXResourceException;

}
