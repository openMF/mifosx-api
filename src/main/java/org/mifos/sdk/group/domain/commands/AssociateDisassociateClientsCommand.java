/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain.commands;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Used for handling 'associate and disassociate clients' of the group command.
 */
public final class AssociateDisassociateClientsCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link AssociateDisassociateClientsCommand}
     */
    public static class Builder {

        final List<Long> clientMembers;

        private Builder(final List<Long> members) {
            this.clientMembers = members;
        }

        /**
         * Constructs a new AssociateDisassociateClientsCommand instance with the provided parameters.
         * @return a new instance of {@link AssociateDisassociateClientsCommand}
         */
        public AssociateDisassociateClientsCommand build() {
            return new AssociateDisassociateClientsCommand(this.clientMembers);
        }

    }

    private List<Long> clientMembers;

    private AssociateDisassociateClientsCommand(final List<Long> members) {
        this.clientMembers = members;
    }

    /**
     * Returns the list of client members.
     */
    public List<Long> getClientMembers() {
        return this.clientMembers;
    }

    /**
     * Sets the list of client members.
     * @param members the client members
     * @return a new instance of {@link Builder}
     */
    public static Builder clientMembers(final List<Long> members) {
        Preconditions.checkNotNull(members);
        Preconditions.checkArgument(!members.isEmpty());

        return new Builder(members);
    }

}
