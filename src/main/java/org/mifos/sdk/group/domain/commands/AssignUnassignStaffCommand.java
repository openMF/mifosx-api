/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain.commands;

import com.google.common.base.Preconditions;

/**
 * Used for handling 'assign and unassign staff' of the group command.
 */
public class AssignUnassignStaffCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link AssignUnassignStaffCommand}
     */
    public static class Builder {

        private Long staffId;
        private boolean inheritStaffForClientAccounts;

        private Builder(final Long id) {
            this.staffId = id;
        }

        /**
         * Sets whether staff is inherited for client accounts.
         * @param inherit true or false
         * @return the current instance of {@link Builder}
         */
        public Builder inheritStaffForClientAccounts(final boolean inherit) {
            this.inheritStaffForClientAccounts = inherit;

            return this;
        }

        /**
         * Constructs a new AssignUnassignStaffCommand instance with the provided parameter.
         * @return a new instance of {@link AssignUnassignStaffCommand}
         */
        public AssignUnassignStaffCommand build() {
            return new AssignUnassignStaffCommand(this.staffId, this.inheritStaffForClientAccounts);
        }

    }

    private Long staffId;
    private boolean inheritStaffForClientAccounts;

    private AssignUnassignStaffCommand(final Long id,
                                       final boolean inheritStaffForClientAccounts) {
        this.staffId = id;
        this.inheritStaffForClientAccounts = inheritStaffForClientAccounts;
    }

    /**
     * Returns the staff ID.
     */
    public Long getStaffId() {
        return this.staffId;
    }

    /**
     * Returns true if staff is inherited for client accounts, false otherwise.
     */
    public boolean isInheritStaffForClientAccounts() {
        return this.inheritStaffForClientAccounts;
    }

    /**
     * Sets the staff ID.
     * @param id the staff ID
     * @return a new instance of {@link Builder}
     */
    public static Builder staffId(final Long id) {
        Preconditions.checkNotNull(id);

        return new Builder(id);
    }

}
