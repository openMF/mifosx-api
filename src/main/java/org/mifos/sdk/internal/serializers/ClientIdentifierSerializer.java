/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers;

import com.google.gson.*;
import org.mifos.sdk.client.domain.ClientIdentifier;

import java.lang.reflect.Type;

/**
 * JSON serializer for ClientIdentifier.
 */
public class ClientIdentifierSerializer implements JsonSerializer<ClientIdentifier>, JsonDeserializer<ClientIdentifier> {

    @Override
    public JsonElement serialize(final ClientIdentifier src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        if (src.getDocumentKey() == null || src.getDocumentKey().isEmpty()) {
            throw new IllegalArgumentException("The document key cannot be null or empty!");
        }

        jsonObject.addProperty("documentKey", src.getDocumentKey());

        if (src.getDocumentTypeId() == null) {
            throw new IllegalArgumentException("Document Type ID cannot be null!");
        }

        jsonObject.addProperty("documentTypeId", src.getDocumentTypeId());

        if (src.getDescription() != null && !src.getDescription().isEmpty()) {
            jsonObject.addProperty("description", src.getDescription());
        }

        return jsonObject;
    }

    @Override
    public ClientIdentifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonObject jsonObject = json.getAsJsonObject();
        final ClientIdentifier clientIdentifier = ClientIdentifier
            .documentKey(jsonObject.has("documentKey") ? jsonObject.get("documentKey").getAsString()
                : null)
            .description(jsonObject.has("description") ? jsonObject.get("description").getAsString()
                : null)
            .documentTypeId(jsonObject.has("documentType") ? jsonObject.get("documentType")
                .getAsJsonObject().get("id").getAsLong() : null)
            .build();

        if (jsonObject.has("documentType")) {
            clientIdentifier.setDocumentTypeName(jsonObject.get("documentType").getAsJsonObject()
                .get("name").getAsString());
        } else if (jsonObject.has("clientId")) {
            clientIdentifier.setClientId(jsonObject.get("clientId").getAsLong());
        } else if (jsonObject.has("officeId")) {
            clientIdentifier.setOfficeId(jsonObject.get("officeId").getAsLong());
        } else if (jsonObject.has("id")) {
            clientIdentifier.setResourceId(jsonObject.get("id").getAsLong());
        } else if (jsonObject.has("resourceId")) {
            clientIdentifier.setResourceId(jsonObject.get("resourceId").getAsLong());
        }

        return clientIdentifier;
    }

}
