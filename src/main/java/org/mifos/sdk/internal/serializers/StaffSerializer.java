/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers;

import com.google.gson.*;
import org.mifos.sdk.internal.ParseUtil;
import org.mifos.sdk.staff.domain.Staff;

import java.lang.reflect.Type;
import java.text.ParseException;

/**
 * JSON serializer for Staff.
 */
public class StaffSerializer implements JsonSerializer<Staff>, JsonDeserializer<Staff> {

    @Override
    public JsonElement serialize(final Staff src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("officeId", src.getOfficeId());

        if (src.getFirstname() == null || src.getLastname() == null) {
            throw new IllegalArgumentException("Staff first name and last name cannot be null!");
        } else if (src.getFirstname().isEmpty() || src.getLastname().isEmpty()) {
            throw new IllegalArgumentException("Staff first name and last name cannot be empty!");
        }

        jsonObject.addProperty("firstname", src.getFirstname());
        jsonObject.addProperty("lastname", src.getLastname());

        if (src.getExternalId() != null) {
            if (src.getExternalId().isEmpty()) {
                throw new IllegalArgumentException("Staff external ID cannot be empty!");
            } else if (src.getExternalId().length() > 100) {
                throw new IllegalArgumentException("Staff external ID cannot be greater " +
                        "than 100 characters in length!");
            }

            jsonObject.addProperty("externalId", src.getExternalId());
        } else if (src.getMobileNo() != null) {
            if (src.getMobileNo().isEmpty()) {
                throw new IllegalArgumentException("Staff mobile number cannot be empty!");
            }

            jsonObject.addProperty("mobileNo", src.getMobileNo());
        } else if (src.getIsActive()) {
            jsonObject.addProperty("isActive", src.getIsActive());
        } else if (src.getIsLoanOfficer()) {
            jsonObject.addProperty("isLoanOfficer", src.getIsLoanOfficer());
        } else if (src.getJoiningDate() != null) {
            if (src.getLocale() == null || src.getDateFormat() == null ||
                    src.getDateFormat().isEmpty() || src.getLocale().isEmpty()) {
                throw new IllegalArgumentException("Staff locale and date format is required " +
                        "by joining date and cannot be null and/or empty!");
            }

            jsonObject.addProperty("locale", src.getLocale());
            jsonObject.addProperty("dateFormat", src.getDateFormat());
            jsonObject.addProperty("joiningDate", ParseUtil.parseDateToString(src.getJoiningDate(),
                    src.getDateFormat(), src.getLocale()));
        }

        return jsonObject;
    }

    @Override
    public Staff deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        Staff staff = null;
        final JsonObject jsonObject = json.getAsJsonObject();

        try {
            String idParam = null;
            if (jsonObject.get("officeId").isJsonNull()) {
                idParam = "id";
            } else {
                idParam = "officeId";
            }
            staff = Staff.officeId(jsonObject.get(idParam).getAsLong())
                    .externalId(jsonObject.has("externalId") ? jsonObject
                            .get("externalId").getAsString() : null)
                    .firstname(jsonObject.has("firstname") ? jsonObject.get("firstname")
                            .getAsString() : null)
                    .lastname(jsonObject.has("lastname") ? jsonObject
                            .get("lastname").getAsString() : null)
                    .isActive(jsonObject.has("isActive") && jsonObject.get("isActive").getAsBoolean())
                    .isLoanOfficer(jsonObject.has("isLoanOfficer") && jsonObject
                            .get("isLoanOfficer").getAsBoolean())
                    .joiningDate(jsonObject.has("joiningDate") ? ParseUtil
                            .parseDateFromJsonArray(jsonObject
                                    .get("joiningDate").getAsJsonArray()) : null)
                    .mobileNo(jsonObject.has("mobileNo") ? jsonObject
                            .get("mobileNo").getAsString() : null)
                    .build();

            staff.setDisplayName(jsonObject.has("displayName") ? jsonObject
                    .get("displayName").getAsString() : null);
            staff.setResourceId(jsonObject.has("resourceId") ? jsonObject
                    .get("resourceId").getAsLong() : null);
            staff.setOfficeName(jsonObject.has("officeName") ? jsonObject
                    .get("officeName").getAsString() : null);
        } catch (ParseException e) {
            throw new IllegalStateException("There was error while deserializing the server response from the staff API endpoint.");
        }

        return staff;
    }

}
