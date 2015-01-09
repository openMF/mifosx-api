/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Container for the list of groups and total filtered records.
 */
public final class PageableGroups {

    private Long totalFilteredRecords;
    private List<Group> pageItems;

    /**
     * Returns the total number of filtered records.
     */
    public Long getTotalFilteredRecords() {
        return this.totalFilteredRecords;
    }

    /**
     * Sets the total number of filtered records.
     * @param totalFilteredRecords the number of filtered records
     */
    public void setTotalFilteredRecords(final Long totalFilteredRecords) {
        Preconditions.checkNotNull(totalFilteredRecords);

        this.totalFilteredRecords = totalFilteredRecords;
    }

    /**
     * Returns the list of groups.
     */
    public List<Group> getGroups() {
        return this.pageItems;
    }

    /**
     * Sets the list of groups.
     * @param groups the group list
     */
    public void setClients(final List<Group> groups) {
        Preconditions.checkNotNull(groups);

        this.pageItems = groups;
    }

}
