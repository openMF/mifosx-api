/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain.commands;

import com.google.common.base.Preconditions;

/**
 * Used for handling 'propose transfer' of the client command.
 */
public final class ProposeClientTransferCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link ProposeClientTransferCommand}
     */
    public static class Builder {

        private Long destinationOfficeId;
        private String note;

        private Builder(final Long id) {
            this.destinationOfficeId = id;
        }

        /**
         * Sets the note with the reason.
         * @param description the note with the reason
         * @return he current instance of {@link Builder}
         */
        public Builder note(final String description) {
            Preconditions.checkNotNull(description);
            Preconditions.checkArgument(!description.isEmpty());

            this.note = description;
            return this;
        }

        /**
         * Constructs a new ProposeClientTransfer instance with the provided parameter.
         * @return a new instance of {@link ProposeClientTransferCommand}
         */
        public ProposeClientTransferCommand build() {
            return new ProposeClientTransferCommand(this.destinationOfficeId, this.note);
        }

    }

    private Long destinationOfficeId;
    private String note;

    private ProposeClientTransferCommand(final Long id,
                                         final String description) {
        this.destinationOfficeId = id;
        this.note = description;
    }

    /**
     * Returns the destination office ID.
     */
    public Long getDestinationOfficeId() {
        return this.destinationOfficeId;
    }

    /**
     * Returns the note.
     */
    public String getNote() {
        return this.note;
    }

    /**
     * Sets the destination office ID.
     * @param id the destination office ID
     * @return a new instance of {@link Builder}
     */
    public static Builder destinationOfficeId(final Long id) {
        Preconditions.checkNotNull(id);

        return new Builder(id);
    }

}
