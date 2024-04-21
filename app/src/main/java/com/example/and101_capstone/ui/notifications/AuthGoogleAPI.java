package com.example.and101_capstone.ui.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

public class AuthGoogleAPI {
    private final Context context;

    public AuthGoogleAPI(Context context) {
        this.context = context;
    }

    public void performAuthorization() {
        AuthorizationServiceConfiguration serviceConfig =
                new AuthorizationServiceConfiguration(
                        Uri.parse("https://idp.example.com/auth"), // authorization endpoint
                        Uri.parse("https://idp.example.com/token")); // token endpoint

        AuthorizationRequest authRequest =
                new AuthorizationRequest.Builder(
                        serviceConfig, // the authorization service configuration
                        MY_CLIENT_ID, // the client ID, typically pre-registered and static
                        ResponseTypeValues.CODE, // the response_type value: we want a code
                        MY_REDIRECT_URI) // the redirect URI to which the auth response is sent
                        .build();

        AuthorizationService authService = new AuthorizationService(context);

        PendingIntent authorizationIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MyAuthCompleteActivity.class), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent cancellationIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MyAuthCanceledActivity.class), PendingIntent.FLAG_IMMUTABLE);

        authService.performAuthorizationRequest(authRequest, authorizationIntent, cancellationIntent);
    }
}
