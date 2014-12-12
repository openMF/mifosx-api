/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

import com.google.gson.*;
import com.squareup.okhttp.OkHttpClient;
import org.mifos.sdk.internal.RestMifosXClient;
import org.mifos.sdk.internal.serializers.OfficeSerializer;
import org.mifos.sdk.internal.serializers.StaffSerializer;
import org.mifos.sdk.office.domain.Office;
import org.mifos.sdk.staff.domain.Staff;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Utility class to return instances of {@link MifosXClient}
 */
public final class MifosXClientFactory {

    /**
     * Returns a new instance of {@link MifosXClient}
     * @param properties the {@link MifosXProperties} for authentication
     */
    public static MifosXClient get(final MifosXProperties properties) {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Office.class, new OfficeSerializer())
                .registerTypeAdapter(Staff.class, new StaffSerializer())
                .create();
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(properties.getUrl())
                .setConverter(new GsonConverter(gson))
                .build();
        return new RestMifosXClient(properties, restAdapter);
    }

}
