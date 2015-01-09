/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.group.domain.commands;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Used for handling 'transfer clients' of the group command.
 */
public final class TransferClientsCommand {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link TransferClientsCommand}
     */
    public static class Builder {

        private Long destinationGroupId;
        private List<Long> clients;
        private boolean inheritDestinationGroupLoanOfficer;
        private boolean transferActiveLoans;

        private Builder(final Long destinationGroupId) {
            this.destinationGroupId = destinationGroupId;
        }

        /**
         * Sets the client list.
         * @param clients the clients
         * @return the current instance of {@link Builder}
         */
        public Builder clients(final List<Long> clients) {
            Preconditions.checkNotNull(clients);
            Preconditions.checkArgument(!clients.isEmpty());
            this.clients = clients;

            return this;
        }

        /**
         * Sets whether destination group inherits loan officer.
         * @param inherit true or false
         * @return the current instance of {@link Builder}
         */
        public Builder inheritDestinationGroupLoanOfficer(final boolean inherit) {
            Preconditions.checkNotNull(inherit);
            this.inheritDestinationGroupLoanOfficer = inherit;

            return this;
        }

        /**
         * Sets whether active loans should be transferred.
         * @param transfer true or false
         * @return the current instance of {@link Builder}
         */
        public Builder transferActiveLoans(final boolean transfer) {
            Preconditions.checkNotNull(transfer);
            this.transferActiveLoans = transfer;

            return this;
        }

        /**
         * Constructs a new TransferClientsCommand instance with the provided parameters.
         * @return a new instance of {@link TransferClientsCommand}
         */
        public TransferClientsCommand build() {
            Preconditions.checkNotNull(this.clients);
            Preconditions.checkArgument(!this.clients.isEmpty());

            return new TransferClientsCommand(this.destinationGroupId, this.clients,
                this.inheritDestinationGroupLoanOfficer, this.transferActiveLoans);
        }

    }

    private Long destinationGroupId;
    private List<Long> clients;
    private boolean inheritDestinationGroupLoanOfficer;
    private boolean transferActiveLoans;

    private TransferClientsCommand(final Long destinationGroupId,
                                   final List<Long> clients,
                                   final boolean inheritDestinationGroupLoanOfficer,
                                   final boolean transferActiveLoans) {
        this.destinationGroupId = destinationGroupId;
        this.clients = clients;
        this.inheritDestinationGroupLoanOfficer = inheritDestinationGroupLoanOfficer;
        this.transferActiveLoans = transferActiveLoans;
    }

    /**
     * Returns the destination group ID.
     */
    public Long getDestinationGroupId() {
        return this.destinationGroupId;
    }

    /**
     * Returns the client list.
     */
    public List<Long> getClients() {
        return this.clients;
    }

    /**
     * Returns true if the destination group inherits the loan officer, false otherwise.
     */
    public boolean isInheritDestinationGroupLoanOfficer() {
        return this.inheritDestinationGroupLoanOfficer;
    }

    /**
     * Returns true if active loans are transferred, false otherwise.
     */
    public boolean isTransferActiveLoans() {
        return this.transferActiveLoans;
    }

    /**
     * Sets the destination group ID.
     * @param id the group ID
     * @return a new instance of {@link Builder}
     */
    public static Builder destinationGroupId(final Long id) {
        Preconditions.checkNotNull(id);

        return new Builder(id);
    }

}
