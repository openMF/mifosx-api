/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.client.domain;

/**
 * Gives an interface to communicate with identifiers for Client API.
 */
public class ClientIdentifier {

    /**
     * Utility class to ease the process of building a
     * new instance of {@link ClientIdentifier}
     */
    public static class Builder {

        private String documentKey;
        private Long documentTypeId;
        private String description;

        private Builder(final String key) {
            this.documentKey = key;
        }

        /**
         * Sets the document type ID.
         * @param id the document type ID
         * @return instance of the current {@link Builder}
         */
        public Builder documentTypeId(final Long id) {
            this.documentTypeId = id;
            return this;
        }

        /**
         * Sets the description.
         * @param text the description text
         * @return instance of the current {@link Builder}
         */
        public Builder description(final String text) {
            this.description = text;
            return this;
        }

        /**
         * Constructs a new ClientIdentifier instance with the provided parameters.
         * @return a new instance of {@link ClientIdentifier}
         */
        public ClientIdentifier build() {
            return new ClientIdentifier(this.documentKey, this.documentTypeId, this.description);
        }

    }

    private String documentKey;
    private Long documentTypeId;
    private String documentTypeName;
    private Long clientId;
    private Long officeId;
    private Long resourceId;
    private String description;

    private ClientIdentifier(final String key,
                             final Long id,
                             final String text) {
        this.documentKey = key;
        this.documentTypeId = id;
        this.description = text;
    }

    /**
     * Returns the document key.
     */
    public String getDocumentKey() {
        return this.documentKey;
    }

    /**
     * Returns the document type ID.
     */
    public Long getDocumentTypeId() {
        return this.documentTypeId;
    }

    /**
     * Returns the document type name.
     */
    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    /**
     * Returns the client ID.
     */
    public Long getClientId() {
        return this.clientId;
    }

    /**
     * Returns the office ID.
     */
    public Long getOfficeId() {
        return this.officeId;
    }

    /**
     * Returns the identifier resource ID.
     */
    public Long getResourceId() {
        return this.resourceId;
    }

    /**
     * Returns the description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the document type name.
     * @param name the document type name
     */
    public void setDocumentTypeName(final String name) {
        this.documentTypeName = name;
    }

    /**
     * Sets the client ID.
     * @param id the client ID
     */
    public void setClientId(final Long id) {
        this.clientId = id;
    }

    /**
     * Sets the office ID.
     * @param id the office ID
     */
    public void setOfficeId(final Long id) {
        this.officeId = id;
    }

    /**
     * Sets the identifier resource ID.
     * @param identifierId the identifier ID
     */
    public void setResourceId(final Long identifierId) {
        this.resourceId = identifierId;
    }

    /**
     * Sets the document key.
     * @param key the document key
     * @return a new instance of {@link Builder}
     */
    public static Builder documentKey(final String key) {
        return new Builder(key);
    }

}
