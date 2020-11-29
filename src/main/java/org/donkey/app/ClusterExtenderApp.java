package org.donkey.app;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@QuarkusMain
public class ClusterExtenderApp implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        final URI baseUri = URI.create(args[0]);
        final String clusterId = args[1];
        final String token = args[2];
        final String authString = "Bearer " + token;

        final ZonedDateTime expirationDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(7);
        final ExpirationTimestampPatch patch = new ExpirationTimestampPatch(expirationDate);

        final ClusterPatchClient clusterPatchClient = RestClientBuilder.newBuilder()
                .baseUri(baseUri)
                .build(ClusterPatchClient.class);

        final Response response = clusterPatchClient.patchCluster(clusterId, patch, authString);

        return 0;
    }
}