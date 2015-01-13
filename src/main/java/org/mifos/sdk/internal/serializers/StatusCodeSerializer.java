/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.mifos.sdk.internal.accounts.StatusCode;

import java.lang.reflect.Type;

/**
 * JSON deserializer for StatusCode.
 */
public class StatusCodeSerializer implements JsonDeserializer<StatusCode> {

    @Override
    public StatusCode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final StatusCode statusCode = new StatusCode();
        final JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("id")) {
            statusCode.setId(jsonObject.get("id").getAsLong());
        }
        if (jsonObject.has("code")) {
            statusCode.setCode(jsonObject.get("code").getAsString());
        }
        if (jsonObject.has("value")) {
            statusCode.setValue(jsonObject.get("value").getAsString());
        }

        return statusCode;
    }

}
