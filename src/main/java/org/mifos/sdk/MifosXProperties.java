/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

/**
 * Configures properties for authentication into the MifosX platform.
 */
public final class MifosXProperties {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link MifosXProperties}
     */
    public static class Builder {

        private String url;
        private String tenantId;
        private String username;
        private String password;

        private Builder(final String loginUrl) {
            this.url = loginUrl;
        }

        /**
         * Sets the tenant identifier for authentication.
         * @param tenant the tenant identifier
         * @return instance of the current {@link Builder}
         */
        public Builder tenant(final String tenant) {
            this.tenantId = tenant;
            return this;
        }

        /**
         * Sets the username for authentication.
         * @param loginUsername the username
         * @return instance of the current {@link Builder}
         */
        public Builder username(final String loginUsername) {
            this.username = loginUsername;
            return this;
        }

        /**
         * Sets the password for authentication.
         * @param loginPassword the password
         * @return instance of the current {@link Builder}
         */
        public Builder password(final String loginPassword) {
            this.password = loginPassword;
            return this;
        }

        /**
         * Constructs a new MifosXProperties instance
         * with the provided properties.
         * @return a new instance of {@link MifosXProperties}
         */
        public MifosXProperties build() {
            return new MifosXProperties(this.url, this.tenantId,
                    this.username, this.password);
        }

    }

    private String url;
    private String tenantId;
    private String username;
    private String password;

    private MifosXProperties(final String loginUrl,
                             final String tenant,
                             final String loginUsername,
                             final String loginPassword) {
        this.url = loginUrl;
        this.tenantId = tenant;
        this.username = loginUsername;
        this.password = loginPassword;
    }

    /** Returns the URL. */
    public String getUrl() {
        return this.url;
    }

    /** Returns the tenant identifier. */
    public String getTenant() {
        return this.tenantId;
    }

    /** Returns the username. */
    public String getUsername() {
        return this.username;
    }

    /** Returns the password. */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the API endpoint URL.
     * @return a new {@link Builder} instance
     * @param url the URL to the API endpoint
     */
    public static Builder url(final String url) {
        return new Builder(url);
    }

}
