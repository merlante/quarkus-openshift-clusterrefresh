package org.donkey.app;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.Form;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient
public interface OCMTokenClient {

    @POST
    @Path("/auth/realms/redhat-external/protocol/openid-connect/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    Response retrieveAccessToken(@Form AccessTokenRequest form);

    class AccessTokenRequest {
        @FormParam("grant_type") final String grantType = "refresh_token";
        @FormParam("client_id") final String clientId = "cloud-services";
        @FormParam("refresh_token") final String refreshToken;

        public AccessTokenRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getGrantType() {
            return grantType;
        }

        public String getClientId() {
            return clientId;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }

}
