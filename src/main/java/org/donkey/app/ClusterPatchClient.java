package org.donkey.app;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient
public interface ClusterPatchClient {

    @PATCH
    @Path("/api/clusters_mgmt/v1/clusters/{clusterId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response patchCluster(@PathParam("clusterId") String clusterId,
                          ExpirationTimestampPatch jsonPatch,
                          @HeaderParam("Authorization") String token);
}
