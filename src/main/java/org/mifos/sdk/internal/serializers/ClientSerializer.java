/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.serializers;

import com.google.gson.*;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.internal.ParseUtil;

import java.lang.reflect.Type;
import java.text.ParseException;

/**
 * JSON serializer for Client.
 */
public class ClientSerializer implements JsonSerializer<Client>, JsonDeserializer<Client> {

    @Override
    public JsonElement serialize(final Client src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        if (src.getOfficeId() == null) {
            throw new IllegalArgumentException("The office Id cannot be null!");
        }

        jsonObject.addProperty("officeId", src.getOfficeId());

        if (src.getFullname() == null && src.getFirstname() == null && src.getLastname() == null) {
            throw new IllegalArgumentException("Client full name or first name & last name " +
                    "should be provided!");
        }

        if (src.getFullname() == null) {
            if (src.getFirstname() == null || src.getLastname() == null ||
                    src.getFirstname().isEmpty() || src.getLastname().isEmpty()) {
                throw new IllegalArgumentException("Client first name and last name cannot " +
                        "be null or empty when full name is not provided.");
            }

            jsonObject.addProperty("firstname", src.getFirstname());
            jsonObject.addProperty("middlename", src.getMiddlename());
            jsonObject.addProperty("lastname", src.getLastname());
        }  else {
            if (src.getFullname().isEmpty()) {
                throw new IllegalArgumentException("Client full name cannot be empty!");
            }

            jsonObject.addProperty("fullname", src.getFullname());
        }

        jsonObject.addProperty("active", src.getActive());
        if (src.getActive()) {
            if (src.getActivationDate() == null) {
                throw new IllegalArgumentException("Client activation date cannot be " +
                        "null when active is true!");
            }  else if (src.getDateFormat() == null || src.getLocale() == null ||
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

        if (src.getGroupId() != null) {
            jsonObject.addProperty("groupId", src.getGroupId());
        } else if (src.getExternalId() != null) {
            if (src.getExternalId().length() > 100) {
                throw new IllegalArgumentException("The client external Id cannot exceed " +
                        "100 characters in length!");
            }

            jsonObject.addProperty("externalId", src.getExternalId());
        } else if (src.getAccountNo() != null) {
            jsonObject.addProperty("accountNo", src.getAccountNo());
        }  else if (src.getStaffId() != null) {
            jsonObject.addProperty("staffId", src.getStaffId());
        }  else if (src.getMobileNo() != null) {
            if (src.getMobileNo().isEmpty()) {
                throw new IllegalArgumentException("Client mobile number cannot be empty!");
            }

            jsonObject.addProperty("mobileNo", src.getMobileNo());
        } else if (src.getSavingsProductId() != null) {
            jsonObject.addProperty("savingsProductId", src.getSavingsProductId());
        } else if (src.getGenderId() != null) {
            jsonObject.addProperty("genderId", src.getGenderId());
        } else if (src.getClientTypeId() != null) {
            jsonObject.addProperty("clientTypeId", src.getClientTypeId());
        } else if (src.getClientClassificationId() != null) {
            jsonObject.addProperty("clientClassificationId", src.getClientClassificationId());
        }

        return jsonObject;
    }

    @Override
    public Client deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        Client.Builder clientBuilder = null;
        Client client = null;
        final JsonObject jsonObject = json.getAsJsonObject();

        try {
            if (jsonObject.has("fullname")) {
                clientBuilder = Client.fullname(jsonObject.get("fullname").getAsString());
            } else {
                clientBuilder = Client.firstname(jsonObject.has("firstname") ? jsonObject
                        .get("firstname").getAsString() : null)
                        .middlename(jsonObject.has("middlename") ? jsonObject
                                .get("middlename").getAsString() : null)
                        .lastname(jsonObject.has("lastname") ? jsonObject
                                .get("lastname").getAsString() : null);
            }

            client = clientBuilder.clientClassificationId(jsonObject.has("clientClassification") &&
                    jsonObject.get("clientClassification")
                            .getAsJsonObject().has("id") ?
                    jsonObject.get("clientClassification")
                            .getAsJsonObject().get("id").getAsLong() : null)
                    .clientTypeId(jsonObject.has("clientType") && jsonObject
                            .get("clientType").getAsJsonObject().has("id") ?
                            jsonObject.get("clientType").getAsJsonObject()
                                    .get("id").getAsLong() : null)
                    .accountNo(jsonObject.has("accountNo") ? jsonObject
                            .get("accountNo").getAsString() : null)
                    .activationDate(jsonObject.has("activationDate") ?
                            ParseUtil.parseDateFromJsonArray(jsonObject
                                    .get("activationDate").getAsJsonArray()) : null)
                    .active(jsonObject.has("active") && jsonObject.get("active")
                            .getAsBoolean())
                    .externalId(jsonObject.has("externalId") ? jsonObject
                            .get("externalId").getAsString() : null)
                    .genderId(jsonObject.has("gender") && jsonObject.get("gender")
                            .getAsJsonObject().has("id") ? jsonObject.get("gender")
                            .getAsJsonObject().get("id").getAsLong() : null)
                    .mobileNo(jsonObject.has("mobileNo") ? jsonObject
                            .get("mobileNo").getAsString() : null)
                    .officeId(jsonObject.get("officeId").getAsLong())
                    .staffId(jsonObject.has("staffId") ? jsonObject
                            .get("staffId").getAsLong() : null)
                    .submittedOnDate(jsonObject.has("timeline") && jsonObject.get("timeline")
                            .getAsJsonObject().has("submittedOnDate") ?
                            ParseUtil.parseDateFromJsonArray(jsonObject.get("timeline")
                                    .getAsJsonObject().get("submittedOnDate").getAsJsonArray()) : null)
                    .build();

            if (jsonObject.has("clientId")) {
                client.setClientId(jsonObject.get("clientId").getAsLong());
            } else {
                client.setClientId(jsonObject.get("id").getAsLong());
            }

            if (jsonObject.has("clientType") && jsonObject.get("clientType").getAsJsonObject()
                    .has("name")) {
                client.setClientTypeName(jsonObject.get("clientType").getAsJsonObject()
                        .get("name").getAsString());
            } else if (jsonObject.has("timeline")) {
                if (jsonObject.get("timeline").getAsJsonObject().has("closedByLastname")) {
                    client.setClosedByLastname(jsonObject.get("timeline").getAsJsonObject()
                            .get("closedByLastname").getAsString());
                } else if (jsonObject.get("timeline").getAsJsonObject().has("closedByUsername")) {
                    client.setClosedByUsername(jsonObject.get("timeline").getAsJsonObject()
                            .get("closedByUsername").getAsString());
                } else if (jsonObject.get("timeline").getAsJsonObject().has("closedByFirstname")) {
                    client.setClosedByFirstname(jsonObject.get("timeline").getAsJsonObject()
                            .get("closedByFirstname").getAsString());
                } else if (jsonObject.get("timeline").getAsJsonObject().has("closedOnDate")) {
                    client.setClosingDate(ParseUtil.parseDateFromJsonArray(jsonObject.get("timeline")
                            .getAsJsonObject().get("closedOnDate").getAsJsonArray()));
                }
            } else if (jsonObject.has("gender") && jsonObject.get("gender")
                    .getAsJsonObject().has("name")) {
                client.setGenderName(jsonObject.get("gender").getAsJsonObject()
                        .get("name").getAsString());
            } else if (jsonObject.has("imageId")) {
                client.setImageId(jsonObject.get("imageId").getAsLong());
            } else if (jsonObject.has("imagePresent")) {
                client.setImagePresent(jsonObject.get("imagePresent").getAsBoolean());
            } else if (jsonObject.has("resourceId")) {
                client.setResourceId(jsonObject.get("resourceId").getAsLong());
            } else if (jsonObject.has("savingsAccountId")) {
                client.setSavingsAccountId(jsonObject.get("savingsAccountId").getAsLong());
            } else if (jsonObject.has("savingsId")) {
                client.setSavingsId(jsonObject.get("savingsId").getAsLong());
            } else if (jsonObject.has("staffName")) {
                client.setStaffName(jsonObject.get("staffName").getAsString());
            } else if (jsonObject.has("displayName")) {
                client.setDisplayName(jsonObject.get("displayName").getAsString());
            } else if (jsonObject.has("officeName")) {
                client.setOfficeName(jsonObject.get("officeName").getAsString());
            } else if (jsonObject.has("status")) {
                if (jsonObject.get("status").getAsJsonObject().has("code")) {
                    client.setStatusCode(jsonObject.get("status").getAsJsonObject()
                            .get("code").getAsString());

                } else if (jsonObject.get("status").getAsJsonObject().has("id")) {
                    client.setStatusId(jsonObject.get("status").getAsJsonObject()
                            .get("id").getAsLong());
                } else if (jsonObject.get("status").getAsJsonObject().has("value")) {
                    client.setStatusValue(jsonObject.get("status").getAsJsonObject()
                            .get("value").getAsString());

                }
            }
        } catch (ParseException e) {
            throw new IllegalStateException("There was error while deserializing the server" +
                " response from the clients API endpoint.");
        }

        return client;
    }

}
