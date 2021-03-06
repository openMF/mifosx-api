/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import retrofit.client.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class to parse the server response in case an ambiguous error occurs.
 */
public class ServerResponseUtil {

    /**
     * Parses the server response to extract the useful message.
     * @param response the server response
     * @return the error message
     */
    public static String parseResponse(final Response response) {
        Preconditions.checkNotNull(response);

        try {
            final InputStream stream = response.getBody().in();
            final InputStreamReader streamReader = new InputStreamReader(stream);
            final JsonObject responseJSON = new Gson().fromJson(streamReader, JsonObject.class);
            final JsonObject message = responseJSON.get("errors").getAsJsonArray()
                .get(0).getAsJsonObject();
            return message.get("developerMessage").getAsString();
        } catch(IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

}
