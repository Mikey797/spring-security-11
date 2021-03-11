package com.example.aouthtwo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/*
here we define separate credentials for client
 */
@Configuration
/*
add auth server functionality

grant type is the precise way that client obtain the token

authorization code - PEKE - proof key
password --> deprecated - user presents client credentials to get a token from auth server
client credentials
refresh token
implicit --> deprecated

 */
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // password grant type
                .withClient("client1").secret("secret1").scopes("read").authorizedGrantTypes("password")
                .and()
                // for simplicity we use implicit grant type here - auth server redirects to the client after getting the token - this is almost authorization code grant type
                // authorization code grant type = here instead of getting token immediately after user authenticates it gives a authorization code, then client does one more step to get the token from the authorization code
                // here the auth server needs  a way to allow user to  enter credentials - spring provides this out of the box - for this just override the configure method of the class which extends WebSecurityConfigurerAdapter
                // you can request a token only once from a authorization_code if you wanna do more tha once you need to re authenticate and get new authorization_code
                .withClient("client2").secret("secret2").scopes("read").redirectUris("http://localhost:9090").authorizedGrantTypes("authorization_code");
    }

    @Override
    /* AuthorizationServerEndpointsConfigurer will use to plug authorization manager to
     * the authorization server.
     * */
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }
}
