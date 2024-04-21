package com.example.and101_capstone.ui.notifications;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import com.google.android.gms.auth.TokenData;

class TokenStorage {
    private final SharedPreferences sharedPrefs;
    private TokenData tokenData = null;
    private String applicationName = "com.authsamples.basicmobileapp";
    private final String key = "AUTH_STATE";

    public TokenStorage(Context context) {
        sharedPrefs = context.getSharedPreferences(applicationName, MODE_PRIVATE);
    }

    /*
     * Load token data from storage on application startup
     */
    public void loadTokens() {
        try {
            // Try the load
            String data = sharedPrefs.getString(key, "");
            if (!data.isEmpty() && !data.trim().isEmpty()) {
                // Try to deserialize
                Gson gson = new Gson();
                tokenData = gson.fromJson(data, TokenData.class);
            }
        } catch (Throwable ex) {
            // Require a new login if there are problems loading tokens
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }
    /*
     * Get tokens if the user has logged in or they have been loaded from storage
     */
    public TokenData getTokens() {
        return this.tokenData;
    }

    /*
     * Save tokens to stored preferences
     */
    public void saveTokens(TokenData newTokenData) {
        this.tokenData = newTokenData;
        this.saveTokenData();
    }

    /*
     * Remove tokens from storage
     */
    public void removeTokens() {
        this.tokenData = null;
        this.sharedPrefs.edit().remove(this.key).apply();
    }

    /*
     * A hacky method for testing, to update token storage to make the access token act like it is expired
     */
    public void expireAccessToken() {
        if (this.tokenData != null) {
            this.tokenData.accessToken = this.tokenData.accessToken + "x";
            this.saveTokenData();
        }
    }
    /*
     * A hacky method for testing, to update token storage to make the refresh token act like it is expired
     */
    public void expireRefreshToken() {
        if (this.tokenData != null) {
            this.tokenData.accessToken = this.tokenData.accessToken + "x";
            this.tokenData.refreshToken = this.tokenData.refreshToken + "x";
            this.saveTokenData();
        }
    }

    /*
     * Save tokens to stored preferences
     */
    private void saveTokenData() {
        Gson gson = new Gson();
        String data = gson.toJson(this.tokenData);
        this.sharedPrefs.edit().putString(this.key, data).apply();
    }
}

