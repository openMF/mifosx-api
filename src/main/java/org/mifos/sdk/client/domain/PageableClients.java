/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain;

import java.util.List;

/**
 * Container for the list of clients and total filtered records.
 */
public class PageableClients {

    private Long totalFilteredRecords;
    private List<Client> pageItems;

    /**
     * Returns the total number of filtered records.
     */
    public Long getTotalFilteredRecords() {
        return this.totalFilteredRecords;
    }

    /**
     * Returns the list of clients.
     */
    public List<Client> getClients() {
        return this.pageItems;
    }

}
