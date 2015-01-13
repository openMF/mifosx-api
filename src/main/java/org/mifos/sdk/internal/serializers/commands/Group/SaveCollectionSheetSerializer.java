/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers.commands.group;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.mifos.sdk.group.domain.commands.SaveCollectionSheetCommand;
import org.mifos.sdk.internal.ParseUtil;

import java.lang.reflect.Type;

/**
 * JSON serializer for SaveCollectionSheetCommand.
 */
public class SaveCollectionSheetSerializer implements JsonSerializer<SaveCollectionSheetCommand> {

    @Override
    public JsonElement serialize(final SaveCollectionSheetCommand src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateFormat", src.getDateFormat());
        jsonObject.addProperty("locale", src.getLocale());
        jsonObject.addProperty("calendarId", src.getCalendarId());
        jsonObject.addProperty("transactionDate", ParseUtil.parseDateToString(src.getTransactionDate(),
            src.getDateFormat(), src.getLocale()));
        jsonObject.addProperty("actualDisbursementDate", ParseUtil.parseDateToString(src.getActualDisbursementDate(),
            src.getDateFormat(), src.getLocale()));
        jsonObject.addProperty("clientsAttendance", new Gson().toJson(src.getClientsAttendance()));
        jsonObject.addProperty("bulkDisbursementTransactions", new Gson().toJson(src.getBulkDisbursementTransactions()));
        jsonObject.addProperty("bulkRepaymentTransactions", new Gson().toJson(src.getBulkRepaymentTransactions()));

        return jsonObject;
    }

}
