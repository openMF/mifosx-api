/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.accounts;

import java.util.List;

/**
 * Holds information related to the timeline of an entity.
 */
public class Timeline {

    private List<Event> events;

    /**
     * Returns the list of events.
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Sets the list of events.
     * @param events the list of events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
