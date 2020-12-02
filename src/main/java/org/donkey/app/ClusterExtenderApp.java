package org.donkey.app;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import picocli.CommandLine;

import javax.inject.Inject;
import javax.json.JsonObject;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@QuarkusMain
@CommandLine.Command(name = "extend", mixinStandardHelpOptions = true)
public class ClusterExtenderApp implements Runnable, QuarkusApplication {

    @Inject
    CommandLine.IFactory factory;

    @Inject
    @RestClient
    OCMTokenClient ocmTokenClient;

    @CommandLine.Option(names = {"-u", "--url"}, required = true, description = "OCM environment baseUrl")
    String baseUri;

    @CommandLine.Option(names = {"-c", "--cluster"}, required = true, description = "OCM environment baseUrl")
    String clusterId;

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    Exclusive tokens;

    static class Exclusive {
        @CommandLine.Option(names = {"-a", "--accessToken"}, description = "OCM access token (retrieved with refresh token if not specified)")
        String accessToken;

        @CommandLine.Option(names = {"-r", "--refreshToken"}, description = "OCM refresh token (retrieve new access token where none is specified)")
        String refreshToken;
    }

    @Override
    public void run() {
        var accessToken = Optional.ofNullable(tokens.accessToken).orElseGet(() -> getNewAccessToken(tokens.refreshToken));

        var authString = "Bearer " + accessToken;

        var expirationDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(7);
        var patch = new ExpirationTimestampPatch(expirationDate);

        var clusterPatchClient = RestClientBuilder.newBuilder()
                .baseUri(URI.create(baseUri))
                .build(ClusterPatchClient.class);

        clusterPatchClient.patchCluster(clusterId, patch, authString);
    }

    private String getNewAccessToken(String refreshToken) {
        var request = new OCMTokenClient.AccessTokenRequest(tokens.refreshToken);
        var tokenResponse = ocmTokenClient.retrieveAccessToken(request);

        return tokenResponse.readEntity(JsonObject.class).getString("access_token");
    }

    @Override
    public int run(String... args) throws Exception {
        return new CommandLine(this, factory).execute(args);
    }
}