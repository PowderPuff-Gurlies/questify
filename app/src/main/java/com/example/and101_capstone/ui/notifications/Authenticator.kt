package com.example.and101_capstone.ui.notifications

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import android.util.Log
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.NoClientAuthentication
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.BrowserMatcher
import net.openid.appauth.browser.VersionedBrowserMatcher
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Authenticator(private val configuration: OAuthConfiguration, private val applicationContext: Context) : AuthenticatorVars {


    private var metadata: AuthorizationServiceConfiguration? = null
    private var loginAuthService: AuthorizationService? = null
    private var logoutAuthService: AuthorizationService? = null
    private val concurrencyHandler = ConcurrentActionHandler()
    private val tokenStorage = TokenStorage(this.applicationContext)

    /*
     * One time initialization on application startup
     */
    override suspend fun initialize() {

        // Load OpenID Connect metadata
        this.getMetadata()

        // Load tokens from storage
        this.tokenStorage.loadTokens()
    }

    /*
     * Try to get an access token, which most commonly involves returning the current one
     */
    override suspend fun getAccessToken(): String? {

        // See if there is a token in storage
        val accessToken = this.tokenStorage.getTokens()?.accessToken
        if (!accessToken.isNullOrBlank()) {
            return accessToken
        }

        // Indicate no access token
        return null
    }

    /*
     * Try to refresh an access token
     */
    override suspend fun synchronizedRefreshAccessToken(): String {

        val refreshToken = this.tokenStorage.getTokens()?.refreshToken
        if (!refreshToken.isNullOrBlank()) {

            // Perform token refresh and manage concurrency
            this.concurrencyHandler.execute(this::performRefreshTokenGrant)

            // Return the token on success
            val accessToken = this.tokenStorage.getTokens()?.accessToken
            if (!accessToken.isNullOrBlank()) {
                return accessToken
            }
        }

        // Otherwise abort the API call via a known exception
        Log.d("No Token", "There isn't a token probably")
        throw Error("bad token")
        //throw ErrorFactory().fromLoginRequired()
    }

    /*
     * Do the work to perform an authorization redirect
     */
    override fun startLogin(launchAction: (i: Intent) -> Unit) {

        try {

            val authService = AuthorizationService(this.applicationContext, this.getBrowserConfiguration())
            this.loginAuthService = authService

            // Create the AppAuth request object and use Authorization Code Flow (PKCE)
            // If required, call builder.setAdditionalParameters to supply details such as acr_values
            val builder = AuthorizationRequest.Builder(
                this.metadata!!,
                this.configuration.clientId,
                ResponseTypeValues.CODE,
                Uri.parse(this.getLoginRedirectUri())
            )
                .setScope(this.configuration.scope)
            val request = builder.build()

            // Do the AppAuth redirect
            val authIntent = authService.getAuthorizationRequestIntent(request)
            launchAction(authIntent)

        } catch (ex: Throwable) {
            Log.d("Login Error", "${ex.message}")
            //throw ErrorFactory().fromLoginOperationError(ex, ErrorCodes.loginRequestFailed)
        }
    }

    /*
     * When a login redirect completes, process the login response here
     */
    override suspend fun finishLogin(intent: Intent) {

        // Get the response details
        val authorizationResponse = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)

        // Free custom tab resources after a login
        // https://github.com/openid/AppAuth-Android/issues/91
        this.loginAuthService?.dispose()
        this.loginAuthService = null

        when {
            ex != null -> {

                // Handle the case where the user closes the Chrome Custom Tab rather than logging in
                if (ex.type == AuthorizationException.TYPE_GENERAL_ERROR &&
                    ex.code == AuthorizationException.GeneralErrors.USER_CANCELED_AUTH_FLOW.code
                ) {

                    //throw ErrorFactory().fromRedirectCancelled()
                    throw Error("Redirect cancelled")
                }

                // Translate AppAuth errors to the display format
                //throw ErrorFactory().fromLoginOperationError(ex, ErrorCodes.loginResponseFailed)
                throw Error("Error: {$ex}", ex)
            }
            authorizationResponse != null -> {

                // Swap the authorization code for tokens and update state
                this.exchangeAuthorizationCode(authorizationResponse)
            }
        }
    }

    private suspend fun exchangeAuthorizationCode(authResponse: AuthorizationResponse) {

        // Wrap the request in a coroutine
        return suspendCoroutine { continuation ->

            // Define a callback to handle the result of the authorization code grant
            val callback =
                AuthorizationService.TokenResponseCallback { tokenResponse, ex ->

                    when {
                        // Translate AppAuth errors to the display format
                        ex != null -> {
                            //val error = ErrorFactory().fromTokenError(ex, ErrorCodes.authorizationCodeGrantFailed)
                            val error = Error("auth code grand failed")
                            continuation.resumeWithException(error)
                        }

                        // Sanity check
                        tokenResponse == null -> {
                            val empty = RuntimeException("Authorization code grant returned an empty response")
                            continuation.resumeWithException(empty)
                        }

                        // Process the response by saving tokens to secure storage
                        else -> {
                            this.saveTokens(tokenResponse)
                            continuation.resume(Unit)
                        }
                    }
                }

            // Create the authorization code grant request
            val tokenRequest = authResponse.createTokenExchangeRequest()

            // Trigger the request
            val authService = AuthorizationService(this.applicationContext)
            authService.performTokenRequest(tokenRequest, NoClientAuthentication.INSTANCE, callback)
        }
    }

    /*
 * Control the browser to use for login and logout redirects
 */
    private fun getBrowserConfiguration(): AppAuthConfiguration {

        return AppAuthConfiguration.Builder()
            .setBrowserMatcher(BrowserAllowList(this.getBrowser()))
            .build()
    }

    /*
    * Android emulators on levels 31 and 32 do not reliably return to the app after custom tab redirects
    * Work around this bug using the Chrome system browser until these emulator issues stabilize
    */
    private fun getBrowser(): BrowserMatcher {

        if (this.isEmulator() && (Build.VERSION.SDK_INT == 31 || Build.VERSION.SDK_INT == 32)) {
            return VersionedBrowserMatcher.CHROME_BROWSER
        }

        return VersionedBrowserMatcher.CHROME_CUSTOM_TAB
    }

    /*
  * Basic detection on whether the app is running on an emulator
  */
    private fun isEmulator(): Boolean {

        val model = Build.MODEL.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()
        return model.contains("emulator") ||
                (model.startsWith("sdk_gphone") && manufacturer.contains("google"))

    }



    override fun startLogout(launchAction: (i: Intent) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun finishLogout() {
        TODO("Not yet implemented")
    }

    override fun clearLoginState() {
        TODO("Not yet implemented")
    }

    override fun expireAccessToken() {
        TODO("Not yet implemented")
    }

    override fun expireRefreshToken() {
        TODO("Not yet implemented")
    }
}