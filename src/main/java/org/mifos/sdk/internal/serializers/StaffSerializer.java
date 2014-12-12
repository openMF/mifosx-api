package org.mifos.sdk.internal.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.mifos.sdk.staff.domain.Staff;
/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
import java.lang.reflect.Type;

/**
 * JSON serializer for Staff.
 */
public class StaffSerializer implements JsonSerializer<Staff> {
    @Override
    public JsonElement serialize(final Staff src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("officeId", src.getOfficeId());
        jsonObject.addProperty("firstname", src.getFirstName());
        jsonObject.addProperty("lastname", src.getLastName());
        jsonObject.addProperty("externalId", src.getExternalId());
        jsonObject.addProperty("mobileNo", src.getMobileNo());
        jsonObject.addProperty("isActive", src.getIsActive());
        jsonObject.addProperty("isLoanOfficer", src.getIsLoanOfficer());
        jsonObject.addProperty("locale", src.getLocale());
        jsonObject.addProperty("dateFormat", src.getDateFormat());
        jsonObject.addProperty("joiningDate", src.getJoiningDate());

        return jsonObject;
    }
}
