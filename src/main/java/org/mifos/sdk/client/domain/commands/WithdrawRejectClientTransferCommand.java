/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain.commands;

import com.google.common.base.Preconditions;

/**
 * Used for handling 'withdraw and reject transfer' of the client commands.
 */
public class WithdrawRejectClientTransferCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link WithdrawRejectClientTransferCommand}
     */
    public static class Builder {

        private String note;

        private Builder(final String description) {
            this.note = description;
        }

        /**
         * Constructs a new WithdrawRejectClientTransfer instance with the provided parameter.
         * @return a new instance of {@link WithdrawRejectClientTransferCommand}
         */
        public WithdrawRejectClientTransferCommand build() {
            return new WithdrawRejectClientTransferCommand(this.note);
        }

    }

    private String note;

    private WithdrawRejectClientTransferCommand(final String description) {
        this.note = description;
    }

    /**
     * Returns the note.
     */
    public String getNote() {
        return this.note;
    }

    /**
     * Sets the note with the reason, cannot be null or empty.
     * @param description the note with the reason
     * @return a new instance of {@link Builder}
     */
    public static Builder note(final String description) {
        Preconditions.checkNotNull(description);
        Preconditions.checkArgument(!description.isEmpty());

        return new Builder(description);
    }

}
