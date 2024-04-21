package com.example.and101_capstone.ui.notifications;

import android.net.Uri;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

public class AuthGoogleAPI {
    AuthorizationServiceConfiguration serviceConfig =
            new AuthorizationServiceConfiguration(
                    Uri.parse("https://idp.example.com/auth"), // authorization endpoint
                    Uri.parse("https://idp.example.com/token")); // token endpoint

    AuthorizationRequest.Builder authRequestBuilder =
            new AuthorizationRequest.Builder(
                    serviceConfig, // the authorization service configuration
                    MY_CLIENT_ID, // the client ID, typically pre-registered and static
                    ResponseTypeValues.CODE, // the response_type value: we want a code
                    MY_REDIRECT_URI); // the redirect URI to which the auth response is sent

    AuthorizationService authService = new AuthorizationService(this);

    authService.performAuthorizationRequest(
            authRequest,
            PendingIntent.getActivity(this, 0, new Intent(this, MyAuthCompleteActivity.class), 0),
            PendingIntent.getActivity(this, 0, new Intent(this, MyAuthCanceledActivity.class), 0));
}
