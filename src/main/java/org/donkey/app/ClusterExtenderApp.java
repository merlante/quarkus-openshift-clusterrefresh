package org.donkey.app;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@QuarkusMain
public class ClusterExtenderApp implements QuarkusApplication {

    @Inject
    @RestClient
    private OCMTokenClient ocmTokenClient;

    @Override
    public int run(String... args) throws Exception {
        final URI baseUri = URI.create(args[0]);
        final String clusterId = args[1];
        String accessToken = args[2];
        final String refreshToken = args.length > 3 ? args[3] : null;

        if(refreshToken != null) {
            OCMTokenClient.AccessTokenRequest request = new OCMTokenClient.AccessTokenRequest(refreshToken);
            Response tokenResponse = ocmTokenClient.retrieveAccessToken(request);
            accessToken = tokenResponse.readEntity(JsonObject.class).getString("access_token");
        }

        final String authString = "Bearer " + accessToken;

        final ZonedDateTime expirationDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(7);
        final ExpirationTimestampPatch patch = new ExpirationTimestampPatch(expirationDate);

        final ClusterPatchClient clusterPatchClient = RestClientBuilder.newBuilder()
                .baseUri(baseUri)
                .build(ClusterPatchClient.class);

        final Response response = clusterPatchClient.patchCluster(clusterId, patch, authString);

        return 0;
    }
}