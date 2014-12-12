/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.mifos.sdk.office.domain.Office;

import java.lang.reflect.Type;

/**
 * JSON serializer for Office.
 */
public class OfficeSerializer implements JsonSerializer<Office> {
    @Override
    public JsonElement serialize(final Office src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("dateFormat", src.getDateFormat());
        jsonObject.addProperty("locale", src.getLocale());
        jsonObject.addProperty("nameDecorated", src.getNameDecorated());
        jsonObject.addProperty("parentId", src.getParentId());
        jsonObject.addProperty("externalId", src.getExternalId());
        jsonObject.addProperty("openingDate", src.getOpeningDate());

        return jsonObject;
    }
}
