/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain.commands;

import com.google.common.base.Preconditions;

/**
 * Used for handling 'assign and update role' of the group command.
 */
public final class AssignUpdateRoleCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link AssignUpdateRoleCommand}
     */
    public static class Builder {

        private Long clientId;
        private Long role;

        private Builder(final Long role) {
            this.role = role;
        }

        /**
         * Sets the client ID.
         * @param id the client ID
         * @return the current instance of {@link Builder}
         */
        public Builder clientId(final Long id) {
            Preconditions.checkNotNull(id);
            this.clientId = id;

            return this;
        }

        /**
         * Constructs a new AssignUpdateRoleCommand instance with the provided parameters.
         * @return a new instance of {@link AssignUpdateRoleCommand}
         */
        public AssignUpdateRoleCommand build() {
            return new AssignUpdateRoleCommand(this.role, this.clientId);
        }

    }

    private Long clientId;
    private Long role;

    private AssignUpdateRoleCommand(final Long role,
                                    final Long clientId) {
        this.role = role;
        this.clientId = clientId;
    }

    /**
     * Returns the client ID if exists, null otherwise.
     */
    public Long getClientId() {
        return this.clientId;
    }

    /**
     * Returns the role.
     */
    public Long getRole() {
        return this.role;
    }

    /**
     * Sets the role.
     * @param role the role
     * @return a new instance of {@link Builder}
     */
    public static Builder role(final Long role) {
        Preconditions.checkNotNull(role);

        return new Builder(role);
    }

}
