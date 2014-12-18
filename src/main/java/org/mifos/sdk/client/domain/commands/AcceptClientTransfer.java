/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain.commands;

import com.google.common.base.Preconditions;

/**
 * Used for handling 'accept transfer' of the client command.
 */
public class AcceptClientTransfer {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link AcceptClientTransfer}
     */
    public static class Builder {

        private Long destinationGroupId;
        private Long staffId;
        private String note;

        private Builder(final Long id) {
            this.destinationGroupId = id;
        }

        /**
         * Sets the staff ID.
         * @param id the staff ID
         * @return the current instance of {@link Builder}
         */
        public Builder staffId(final Long id) {
            Preconditions.checkNotNull(id);

            this.staffId = id;
            return this;
        }

        /**
         * Sets the note with the reason.
         * @param description the note with the reason
         * @return the current instance of {@link Builder}
         */
        public Builder note(final String description) {
            Preconditions.checkNotNull(description);
            Preconditions.checkArgument(!description.isEmpty());

            this.note = description;
            return this;
        }

        /**
         * Constructs a new AcceptClientTransfer instance with the provided parameter.
         * @return a new instance of {@link AcceptClientTransfer}
         */
        public AcceptClientTransfer build() {
            return new AcceptClientTransfer(this.destinationGroupId, this.staffId, this.note);
        }

    }

    private Long destinationGroupId;
    private Long staffId;
    private String note;

    private AcceptClientTransfer(final Long destinationGroupId,
                                 final Long staffId,
                                 final String description) {
        this.destinationGroupId = destinationGroupId;
        this.staffId = staffId;
        this.note = description;
    }

    /**
     * Returns the destination group ID.
     */
    public Long getDestinationGroupId() {
        return this.destinationGroupId;
    }

    /**
     * Returns the staff ID.
     */
    public Long getStaffId() {
        return this.staffId;
    }

    /**
     * Returns the note.
     */
    public String getNote() {
        return this.note;
    }

    /**
     * Sets the destination group ID.
     * @param id the destination group ID
     * @return a new instance of {@link Builder}
     */
    public static Builder destinationGroupId(final Long id) {
        Preconditions.checkNotNull(id);

        return new Builder(id);
    }

}
