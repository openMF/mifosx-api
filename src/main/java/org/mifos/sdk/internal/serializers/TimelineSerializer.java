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
import org.mifos.sdk.internal.ParseUtil;
import org.mifos.sdk.internal.accounts.Event;
import org.mifos.sdk.internal.accounts.Timeline;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON deserializer for Timeline.
 */
public class TimelineSerializer implements JsonDeserializer<Timeline> {

    @Override
    public Timeline deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonObject jsonObject = json.getAsJsonObject();
        final Timeline timeline = new Timeline();
        Event submittedEvent = null;
        Event activatedEvent = null;
        Event approvedEvent = null;
        Event withdrawnEvent = null;
        Event closedEvent = null;
        Event rejectedEvent = null;
        Event writeOffEvent = null;
        Event disbursedEvent = null;
        final List<Event> events = new ArrayList<>();

        try {
            // Submitted Event
            if (jsonObject.has("submittedOnDate")) {
                if (submittedEvent == null) {
                    submittedEvent = new Event();
                    submittedEvent.setType(Event.Type.SUBMITTED);
                }
                submittedEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("submittedOnDate").getAsJsonArray()));
                if (!events.contains(submittedEvent)) {
                    events.add(submittedEvent);
                }
            }
            if (jsonObject.has("submittedByUsername")) {
                if (submittedEvent == null) {
                    submittedEvent = new Event();
                    submittedEvent.setType(Event.Type.SUBMITTED);
                }
                submittedEvent.setUsername(jsonObject.get("submittedByUsername").getAsString());
                if (!events.contains(submittedEvent)) {
                    events.add(submittedEvent);
                }
            }
            if (jsonObject.has("submittedByFirstname")) {
                if (submittedEvent == null) {
                    submittedEvent = new Event();
                    submittedEvent.setType(Event.Type.SUBMITTED);
                }
                submittedEvent.setFirstname(jsonObject.get("submittedByFirstname").getAsString());
                if (!events.contains(submittedEvent)) {
                    events.add(submittedEvent);
                }
            }
            if (jsonObject.has("submittedByLastname")) {
                if (submittedEvent == null) {
                    submittedEvent = new Event();
                    submittedEvent.setType(Event.Type.SUBMITTED);
                }
                submittedEvent.setLastname(jsonObject.get("submittedByLastname").getAsString());
                if (!events.contains(submittedEvent)) {
                    events.add(submittedEvent);
                }
            }

            // Activated Event
            if (jsonObject.has("activatedOnDate")) {
                if (activatedEvent == null) {
                    activatedEvent = new Event();
                    activatedEvent.setType(Event.Type.ACTIVATED);
                }
                activatedEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("activatedOnDate").getAsJsonArray()));
                if (!events.contains(activatedEvent)) {
                    events.add(activatedEvent);
                }
            }
            if (jsonObject.has("activatedByUsername")) {
                if (activatedEvent == null) {
                    activatedEvent = new Event();
                    activatedEvent.setType(Event.Type.ACTIVATED);
                }
                activatedEvent.setUsername(jsonObject.get("activatedByUsername").getAsString());
                if (!events.contains(activatedEvent)) {
                    events.add(activatedEvent);
                }
            }
            if (jsonObject.has("activatedByFirstname")) {
                if (activatedEvent == null) {
                    activatedEvent = new Event();
                    activatedEvent.setType(Event.Type.ACTIVATED);
                }
                activatedEvent.setFirstname(jsonObject.get("activatedByFirstname").getAsString());
                if (!events.contains(activatedEvent)) {
                    events.add(activatedEvent);
                }
            }
            if (jsonObject.has("activatedByLastname")) {
                if (activatedEvent == null) {
                    activatedEvent = new Event();
                    activatedEvent.setType(Event.Type.ACTIVATED);
                }
                activatedEvent.setLastname(jsonObject.get("activatedByLastname").getAsString());
                if (!events.contains(activatedEvent)) {
                    events.add(activatedEvent);
                }
            }

            // Approved Event
            if (jsonObject.has("approvedOnDate")) {
                if (approvedEvent == null) {
                    approvedEvent = new Event();
                    approvedEvent.setType(Event.Type.APPROVED);
                }
                approvedEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("approvedOnDate").getAsJsonArray()));
                if (!events.contains(approvedEvent)) {
                    events.add(approvedEvent);
                }
            }
            if (jsonObject.has("approvedByUsername")) {
                if (approvedEvent == null) {
                    approvedEvent = new Event();
                    approvedEvent.setType(Event.Type.APPROVED);
                }
                approvedEvent.setUsername(jsonObject.get("approvedByUsername").getAsString());
                if (!events.contains(approvedEvent)) {
                    events.add(approvedEvent);
                }
            }
            if (jsonObject.has("approvedByFirstname")) {
                if (approvedEvent == null) {
                    approvedEvent = new Event();
                    approvedEvent.setType(Event.Type.APPROVED);
                }
                approvedEvent.setFirstname(jsonObject.get("approvedByFirstname").getAsString());
                if (!events.contains(approvedEvent)) {
                    events.add(approvedEvent);
                }
            }
            if (jsonObject.has("approvedByLastname")) {
                if (approvedEvent == null) {
                    approvedEvent = new Event();
                    approvedEvent.setType(Event.Type.APPROVED);
                }
                approvedEvent.setLastname(jsonObject.get("approvedByLastname").getAsString());
                if (!events.contains(approvedEvent)) {
                    events.add(approvedEvent);
                }
            }

            // Withdrawn Event
            if (jsonObject.has("withdrawnOnDate")) {
                if (withdrawnEvent == null) {
                    withdrawnEvent = new Event();
                    withdrawnEvent.setType(Event.Type.WITHDRAWN);
                }
                withdrawnEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("withdrawnOnDate").getAsJsonArray()));
                if (!events.contains(withdrawnEvent)) {
                    events.add(withdrawnEvent);
                }
            }
            if (jsonObject.has("withdrawnByUsername")) {
                if (withdrawnEvent == null) {
                    withdrawnEvent = new Event();
                    withdrawnEvent.setType(Event.Type.WITHDRAWN);
                }
                withdrawnEvent.setUsername(jsonObject.get("withdrawnByUsername").getAsString());
                if (!events.contains(withdrawnEvent)) {
                    events.add(withdrawnEvent);
                }
            }
            if (jsonObject.has("withdrawnByFirstname")) {
                if (withdrawnEvent == null) {
                    withdrawnEvent = new Event();
                    withdrawnEvent.setType(Event.Type.WITHDRAWN);
                }
                withdrawnEvent.setFirstname(jsonObject.get("withdrawnByFirstname").getAsString());
                if (!events.contains(withdrawnEvent)) {
                    events.add(withdrawnEvent);
                }
            }
            if (jsonObject.has("withdrawnByLastname")) {
                if (withdrawnEvent == null) {
                    withdrawnEvent = new Event();
                    withdrawnEvent.setType(Event.Type.WITHDRAWN);
                }
                withdrawnEvent.setLastname(jsonObject.get("withdrawnByLastname").getAsString());
                if (!events.contains(withdrawnEvent)) {
                    events.add(withdrawnEvent);
                }
            }

            // Closed Event
            if (jsonObject.has("closedOnDate")) {
                if (closedEvent == null) {
                    closedEvent = new Event();
                    closedEvent.setType(Event.Type.CLOSED);
                }
                closedEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("closedOnDate").getAsJsonArray()));
                if (!events.contains(closedEvent)) {
                    events.add(closedEvent);
                }
            }
            if (jsonObject.has("closedByUsername")) {
                if (closedEvent == null) {
                    closedEvent = new Event();
                    closedEvent.setType(Event.Type.CLOSED);
                }
                closedEvent.setUsername(jsonObject.get("closedByUsername").getAsString());
                if (!events.contains(closedEvent)) {
                    events.add(closedEvent);
                }
            }
            if (jsonObject.has("closedByFirstname")) {
                if (closedEvent == null) {
                    closedEvent = new Event();
                    closedEvent.setType(Event.Type.CLOSED);
                }
                closedEvent.setFirstname(jsonObject.get("closedByFirstname").getAsString());
                if (!events.contains(closedEvent)) {
                    events.add(closedEvent);
                }
            }
            if (jsonObject.has("closedByLastname")) {
                if (closedEvent == null) {
                    closedEvent = new Event();
                    closedEvent.setType(Event.Type.CLOSED);
                }
                closedEvent.setLastname(jsonObject.get("closedByLastname").getAsString());
                if (!events.contains(closedEvent)) {
                    events.add(closedEvent);
                }
            }

            // Rejected Event
            if (jsonObject.has("rejectedOnDate")) {
                if (rejectedEvent == null) {
                    rejectedEvent = new Event();
                    rejectedEvent.setType(Event.Type.REJECTED);
                }
                rejectedEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("rejectedOnDate").getAsJsonArray()));
                if (!events.contains(rejectedEvent)) {
                    events.add(rejectedEvent);
                }
            }
            if (jsonObject.has("rejectedByUsername")) {
                if (rejectedEvent == null) {
                    rejectedEvent = new Event();
                    rejectedEvent.setType(Event.Type.REJECTED);
                }
                rejectedEvent.setUsername(jsonObject.get("rejectedByUsername").getAsString());
                if (!events.contains(rejectedEvent)) {
                    events.add(rejectedEvent);
                }
            }
            if (jsonObject.has("rejectedByFirstname")) {
                if (rejectedEvent == null) {
                    rejectedEvent = new Event();
                    rejectedEvent.setType(Event.Type.REJECTED);
                }
                rejectedEvent.setFirstname(jsonObject.get("rejectedByFirstname").getAsString());
                if (!events.contains(rejectedEvent)) {
                    events.add(rejectedEvent);
                }
            }
            if (jsonObject.has("rejectedByLastname")) {
                if (rejectedEvent == null) {
                    rejectedEvent = new Event();
                    rejectedEvent.setType(Event.Type.REJECTED);
                }
                rejectedEvent.setLastname(jsonObject.get("rejectedByLastname").getAsString());
                if (!events.contains(rejectedEvent)) {
                    events.add(rejectedEvent);
                }
            }

            // Write Off Event
            if (jsonObject.has("writeOffOnDate")) {
                if (writeOffEvent == null) {
                    writeOffEvent = new Event();
                    writeOffEvent.setType(Event.Type.WRITEOFF);
                }
                writeOffEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("writeOffOnDate").getAsJsonArray()));
                if (!events.contains(writeOffEvent)) {
                    events.add(writeOffEvent);
                }
            }
            if (jsonObject.has("writeOffByUsername")) {
                if (writeOffEvent == null) {
                    writeOffEvent = new Event();
                    writeOffEvent.setType(Event.Type.WRITEOFF);
                }
                writeOffEvent.setUsername(jsonObject.get("writeOffByUsername").getAsString());
                if (!events.contains(writeOffEvent)) {
                    events.add(writeOffEvent);
                }
            }
            if (jsonObject.has("writeOffByFirstname")) {
                if (writeOffEvent == null) {
                    writeOffEvent = new Event();
                    writeOffEvent.setType(Event.Type.WRITEOFF);
                }
                writeOffEvent.setFirstname(jsonObject.get("writeOffByFirstname").getAsString());
                if (!events.contains(writeOffEvent)) {
                    events.add(writeOffEvent);
                }
            }
            if (jsonObject.has("writeOffByLastname")) {
                if (writeOffEvent == null) {
                    writeOffEvent = new Event();
                    writeOffEvent.setType(Event.Type.WRITEOFF);
                }
                writeOffEvent.setLastname(jsonObject.get("writeOffByLastname").getAsString());
                if (!events.contains(writeOffEvent)) {
                    events.add(writeOffEvent);
                }
            }

            // Disbursed Event
            if (jsonObject.has("actualDisbursementDate")) {
                if (disbursedEvent == null) {
                    disbursedEvent = new Event();
                    disbursedEvent.setType(Event.Type.DISBURSED);
                }
                disbursedEvent.setDate(ParseUtil.parseDateFromJsonArray(jsonObject
                    .get("actualDisbursementDate").getAsJsonArray()));
                if (!events.contains(disbursedEvent)) {
                    events.add(disbursedEvent);
                }
            }
            if (jsonObject.has("disbursedByUsername")) {
                if (disbursedEvent == null) {
                    disbursedEvent = new Event();
                    disbursedEvent.setType(Event.Type.DISBURSED);
                }
                disbursedEvent.setUsername(jsonObject.get("disbursedByUsername").getAsString());
                if (!events.contains(disbursedEvent)) {
                    events.add(disbursedEvent);
                }
            }
            if (jsonObject.has("disbursedByFirstname")) {
                if (disbursedEvent == null) {
                    disbursedEvent = new Event();
                    disbursedEvent.setType(Event.Type.DISBURSED);
                }
                disbursedEvent.setFirstname(jsonObject.get("disbursedByFirstname").getAsString());
                if (!events.contains(disbursedEvent)) {
                    events.add(disbursedEvent);
                }
            }
            if (jsonObject.has("disbursedByLastname")) {
                if (disbursedEvent == null) {
                    disbursedEvent = new Event();
                    disbursedEvent.setType(Event.Type.DISBURSED);
                }
                disbursedEvent.setLastname(jsonObject.get("disbursedByLastname").getAsString());
                if (!events.contains(disbursedEvent)) {
                    events.add(disbursedEvent);
                }
            }
        } catch (ParseException e) {
            throw new IllegalStateException("There was error while deserializing. " + e.getMessage());
        }

        timeline.setEvents(events);

        return timeline;
    }

}
