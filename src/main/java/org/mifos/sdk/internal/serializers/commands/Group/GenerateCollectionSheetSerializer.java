/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers.commands.Group;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.mifos.sdk.group.domain.commands.GenerateCollectionSheetCommand;
import org.mifos.sdk.internal.ParseUtil;

import java.lang.reflect.Type;

/**
 * JSON serializer for GenerateCollectionSheetCommand.
 */
public class GenerateCollectionSheetSerializer implements JsonSerializer<GenerateCollectionSheetCommand> {

    @Override
    public JsonElement serialize(GenerateCollectionSheetCommand src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("locale", src.getLocale());
        jsonObject.addProperty("dateFormat", src.getDateFormat());
        jsonObject.addProperty("transactionDate", ParseUtil.parseDateToString(src.getTransactionDate(),
            src.getDateFormat(), src.getLocale()));
        jsonObject.addProperty("calendarId", src.getCalendarId());

        return jsonObject;
    }

}
