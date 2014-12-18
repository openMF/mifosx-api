/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain.commands;

import com.google.common.base.Preconditions;

/**
 * Used for handling 'update savings account' of the client command.
 */
public class UpdateSavingsAccountCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link UpdateSavingsAccountCommand}
     */
    public static class Builder {

        private Long savingsAccountId;

        private Builder(final Long id) {
            this.savingsAccountId = id;
        }

        /**
         * Constructs a new UpdateSavingsAccount instance with the provided parameter.
         * @return a new instance of {@link UpdateSavingsAccountCommand}
         */
        public UpdateSavingsAccountCommand build() {
            return new UpdateSavingsAccountCommand(this.savingsAccountId);
        }

    }

    private Long savingsAccountId;

    private UpdateSavingsAccountCommand(final Long id) {
        this.savingsAccountId = id;
    }

    /**
     * Returns the savings account ID.
     */
    public Long getSavingsAccountId() {
        return this.savingsAccountId;
    }

    /**
     * Sets the savings account ID of the client.
     * @param id the savings account ID
     * @return a new instance of {@link Builder}
     */
    public static Builder savingsAccountId(final Long id) {
        Preconditions.checkNotNull(id);

        return new Builder(id);
    }

}
