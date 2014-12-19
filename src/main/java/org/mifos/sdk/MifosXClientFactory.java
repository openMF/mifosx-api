/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.ClientIdentifier;
import org.mifos.sdk.client.domain.commands.ActivateClientCommand;
import org.mifos.sdk.client.domain.commands.CloseClientCommand;
import org.mifos.sdk.internal.RestMifosXClient;
import org.mifos.sdk.internal.serializers.ClientIdentifierSerializer;
import org.mifos.sdk.internal.serializers.ClientSerializer;
import org.mifos.sdk.internal.serializers.OfficeSerializer;
import org.mifos.sdk.internal.serializers.StaffSerializer;
import org.mifos.sdk.internal.serializers.commands.Client.ActivateClientSerializer;
import org.mifos.sdk.internal.serializers.commands.Client.CloseClientSerializer;
import org.mifos.sdk.office.domain.Office;
import org.mifos.sdk.staff.domain.Staff;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

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
                .serializeNulls()
                // serializers
                .registerTypeAdapter(Office.class, new OfficeSerializer())
                .registerTypeAdapter(Staff.class, new StaffSerializer())
                .registerTypeAdapter(Client.class, new ClientSerializer())
                // commands serializers
                .registerTypeAdapter(ActivateClientCommand.class, new ActivateClientSerializer())
                .registerTypeAdapter(CloseClientCommand.class, new CloseClientSerializer())
                // identifier serializers
                .registerTypeAdapter(ClientIdentifier.class, new ClientIdentifierSerializer())
                .create();
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(properties.getUrl())
                .setConverter(new GsonConverter(gson))
                .build();

        return new RestMifosXClient(properties, restAdapter);
    }

}
