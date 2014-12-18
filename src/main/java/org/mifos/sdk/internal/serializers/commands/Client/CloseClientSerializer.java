/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers.commands.Client;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.mifos.sdk.client.domain.commands.CloseClientCommand;
import org.mifos.sdk.internal.ParseUtil;

import java.lang.reflect.Type;

/**
 * JSON serializer for CloseClient.
 */
public class CloseClientSerializer implements JsonSerializer<CloseClientCommand> {

    @Override
    public JsonElement serialize(final CloseClientCommand src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("locale", src.getLocale());
        jsonObject.addProperty("dateFormat", src.getDateFormat());
        jsonObject.addProperty("closureDate", ParseUtil.parseDateToString(src.getClosureDate(),
                src.getDateFormat(), src.getLocale()));
        jsonObject.addProperty("closureReasonId", src.getClosureReasonId());

        return jsonObject;
    }

}
