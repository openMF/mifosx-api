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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.mifos.sdk.internal.ParseUtil;
import org.mifos.sdk.office.domain.Office;

import java.lang.reflect.Type;
import java.text.ParseException;

/**
 * JSON serializer for Office.
 */
public class OfficeSerializer implements JsonSerializer<Office>, JsonDeserializer<Office> {

    @Override
    public JsonElement serialize(final Office src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        if (src.getName() == null || src.getParentId() == null || src.getOpeningDate() == null) {
            throw new IllegalArgumentException("Office name, parent ID and opening date cannot be null!");
        } else if (src.getName().isEmpty()) {
            throw new IllegalArgumentException("Office name cannot be empty!");
        } else if (src.getLocale() == null || src.getDateFormat() == null ||
                src.getDateFormat().isEmpty() || src.getLocale().isEmpty()) {
            throw new IllegalArgumentException("Office locale and date format is required " +
                    "by opening date and cannot be null and/or empty!");
        }

        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("dateFormat", src.getDateFormat());
        jsonObject.addProperty("locale", src.getLocale());
        jsonObject.addProperty("parentId", src.getParentId());
        jsonObject.addProperty("openingDate", ParseUtil.parseDateToString(src.getOpeningDate(),
                src.getDateFormat(), src.getLocale()));

        if (src.getNameDecorated() != null) {
            if (src.getNameDecorated().isEmpty()) {
                throw new IllegalArgumentException("Office name decorated cannot be empty!");
            }

            jsonObject.addProperty("nameDecorated", src.getNameDecorated());
        }
        if (src.getExternalId() != null) {
            if (src.getExternalId().isEmpty()) {
                throw new IllegalArgumentException("Office external ID cannot be empty!");
            } else if (src.getExternalId().length() > 100) {
                throw new IllegalArgumentException("Office external ID cannot be greater " +
                        "than 100 characters in length!");
            }

            jsonObject.addProperty("externalId", src.getExternalId());
        }

        return jsonObject;
    }

    @Override
    public Office deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        Office office = null;
        final JsonObject jsonObject = json.getAsJsonObject();

        try {
            office  = Office.name(jsonObject.has("name") ? jsonObject
                    .get("name").getAsString() : null)
                    .externalId(jsonObject.has("externalId") ? jsonObject
                            .get("externalId").getAsString() : null)
                    .nameDecorated(jsonObject.has("nameDecorated") ? jsonObject
                            .get("nameDecorated").getAsString() : null)
                    .openingDate(jsonObject.has("openingDate") ? ParseUtil
                            .parseDateFromJsonArray(jsonObject.get("openingDate")
                                    .getAsJsonArray()) : null)
                    .build();

            String idParam = null;
            if (jsonObject.has("officeId")) {
                idParam = "officeId";
            } else {
                idParam = "id";
            }

            office.setResourceId(jsonObject.has("resourceId") ? jsonObject
                    .get("resourceId").getAsLong() : null);
            office.setOfficeId(jsonObject.get(idParam).getAsLong());
        } catch (ParseException e){
            throw new IllegalStateException("There was error while deserializing the server response from the office API endpoint.");
        }

        return office;
    }

}
