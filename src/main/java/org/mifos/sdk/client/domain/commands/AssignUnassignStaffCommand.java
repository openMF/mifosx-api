/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain.commands;

import com.google.common.base.Preconditions;

/**
 * Used for handling 'assign and unassign staff' of the client command.
 */
public final class AssignUnassignStaffCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link AssignUnassignStaffCommand}
     */
    public static class Builder {

        private Long staffId;

        private Builder(final Long id) {
            this.staffId = id;
        }

        /**
         * Constructs a new AssignUnassignStaff instance with the provided parameter.
         * @return a new instance of {@link AssignUnassignStaffCommand}
         */
        public AssignUnassignStaffCommand build() {
            return new AssignUnassignStaffCommand(this.staffId);
        }

    }

    private Long staffId;

    private AssignUnassignStaffCommand(final Long id) {
        this.staffId = id;
    }

    /**
     * Returns the staff ID.
     */
    public Long getStaffId() {
        return this.staffId;
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
