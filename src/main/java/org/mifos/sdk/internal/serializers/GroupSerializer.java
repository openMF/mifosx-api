/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.mifos.sdk.group.domain.Group;
import org.mifos.sdk.internal.ParseUtil;
import org.mifos.sdk.internal.accounts.StatusCode;
import org.mifos.sdk.internal.accounts.Timeline;

import java.lang.reflect.Type;

/**
 * JSON serializer for Group.
 */
public class GroupSerializer implements JsonSerializer<Group>, JsonDeserializer<Group> {

    @Override
    public JsonElement serialize(final Group src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        if (src.getName() == null || src.getName().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty!");
        }

        jsonObject.addProperty("name", src.getName());

        if (src.getOfficeId() == null) {
            throw new IllegalArgumentException("Office ID for the group cannot be null!");
        }

        jsonObject.addProperty("officeId", src.getOfficeId());
        jsonObject.addProperty("active", src.isActive());

        if (src.isActive()) {
            if (src.getActivationDate() == null) {
                throw new IllegalArgumentException("Activation date cannot be null when " +
                    "active is true!");
            } else if (src.getDateFormat() == null || src.getLocale() == null ||
                src.getDateFormat().isEmpty() || src.getLocale().isEmpty()) {
                throw new IllegalArgumentException("The date format and locale cannot " +
                    "be null or empty when activation date is provided!");
            }

            jsonObject.addProperty("activationDate", ParseUtil
                .parseDateToString(src.getActivationDate(), src.getDateFormat(),
                    src.getLocale()));
            jsonObject.addProperty("dateFormat", src.getDateFormat());
            jsonObject.addProperty("locale", src.getLocale());
        }

        if (src.getExternalId() != null && !src.getExternalId().isEmpty()) {
            jsonObject.addProperty("externalId", src.getExternalId());
        }
        if (src.getClientMembers() != null) {
            jsonObject.addProperty("clientMembers", new Gson().toJson(src.getClientMembers()));
        }
        if (src.getStaffId() != null) {
            jsonObject.addProperty("staffId", src.getStaffId());
        }
        if (src.getSubmittedOnDate() != null) {
            if (jsonObject.has("locale")) {
                jsonObject.addProperty("submittedOnDate", ParseUtil
                    .parseDateToString(src.getSubmittedOnDate(), src.getDateFormat(),
                        src.getLocale()));
            } else {
                if (src.getDateFormat() == null || src.getLocale() == null ||
                    src.getDateFormat().isEmpty() || src.getLocale().isEmpty()) {
                    throw new IllegalArgumentException("The date format and locale cannot " +
                        "be null or empty when submission date is provided!");
                }

                jsonObject.addProperty("submittedOnDate", ParseUtil
                    .parseDateToString(src.getSubmittedOnDate(), src.getDateFormat(),
                        src.getLocale()));
                jsonObject.addProperty("dateFormat", src.getDateFormat());
                jsonObject.addProperty("locale", src.getLocale());
            }
        }

        return jsonObject;
    }

    @Override
    public Group deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonObject jsonObject = json.getAsJsonObject();
        Group group = Group.name(jsonObject.has("name") ? jsonObject
            .get("name").getAsString() : null)
            .active(jsonObject.has("active") && jsonObject.get("active")
                .getAsBoolean())
            .externalId(jsonObject.has("externalId") ? jsonObject
                .get("externalId").getAsString() : null)
            .officeId(jsonObject.get("officeId").getAsLong())
            .staffId(jsonObject.has("staffId") ? jsonObject
                .get("staffId").getAsLong() : null)
            .build();

        if (jsonObject.has("status")) {
            final StatusCode status = new StatusCode();
            final JsonObject statusObject = jsonObject.get("status").getAsJsonObject();
            status.setCode(statusObject.get("code").getAsString());
            status.setId(statusObject.get("id").getAsLong());
            status.setValue(statusObject.get("value").getAsString());

            group.setStatus(status);
        }
        if (jsonObject.has("id") || jsonObject.has("resourceId")) {
            final String idParam = jsonObject.has("id") ? "id" : "resourceId";
            group.setResourceId(jsonObject.get(idParam).getAsLong());
        }
        if (jsonObject.has("officeName")) {
            group.setOfficeName(jsonObject.get("officeName").getAsString());
        }
        if (jsonObject.has("centerId")) {
            group.setCenterId(jsonObject.get("centerId").getAsLong());
        }
        if (jsonObject.has("centerName")) {
            group.setCenterName(jsonObject.get("centerName").getAsString());
        }
        if (jsonObject.has("staffName")) {
            group.setStaffName(jsonObject.get("staffName").getAsString());
        }
        if (jsonObject.has("timeline")) {
            final Timeline timeline = new TimelineSerializer().deserialize(jsonObject
                .get("timeline"), null, null);
            group.setTimeline(timeline);
        }

        return group;
    }

}
