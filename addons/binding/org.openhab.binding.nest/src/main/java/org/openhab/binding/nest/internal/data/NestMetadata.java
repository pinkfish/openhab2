package org.openhab.binding.nest.internal.data;

import com.google.gson.annotations.SerializedName;

/**
 * The meta data in the data downloades from nest.
 *
 * @author David Bennett - Initial Contribution
 */
public class NestMetadata {
    public String getAccess_token() {
        return access_token;
    }

    public String getClient_version() {
        return client_version;
    }

    @SerializedName("access_token")
    private String access_token;
    @SerializedName("client_version")
    private String client_version;
}
