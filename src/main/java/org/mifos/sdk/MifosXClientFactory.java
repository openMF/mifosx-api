/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk;

import com.squareup.okhttp.OkHttpClient;
import org.mifos.sdk.internal.RestMifosXClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Utility class to return instances of {@link MifosXClient}
 */
public final class MifosXClientFactory {

    /**
     * Returns a new instance of {@link MifosXClient}
     * @param properties the {@link MifosXProperties} for authentication
     */
    public static MifosXClient get(final MifosXProperties properties) {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(properties.getUrl())
                .build();
        return new RestMifosXClient(properties, restAdapter);
    }

}
