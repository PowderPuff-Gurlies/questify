package com.example.and101_capstone.ui.notifications

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Identity
import android.security.identity.IdentityCredential
import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.example.and101_capstone.R
import com.example.and101_capstone.ui.task.Task
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.GeneralSecurityException

object CalendarQuickstart {
    /**
     * Application name.
     */
    private const val APPLICATION_NAME = "Google Calendar API Java Quickstart"

    /**
     * Global instance of the JSON factory.
     */
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    /**
     * Directory to store authorization tokens for this application.
     */
    private const val TOKENS_DIRECTORY_PATH = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES = listOf(CalendarScopes.CALENDAR_READONLY)
    private const val CREDENTIALS_FILE_PATH = "app/src/main/res/raw/credentials.json"
    private var CREDENTIALS_FILE_ID = R.raw.credentials


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
//    attempt 1, following the quickstart tutorial
    private fun getCredentials(context: Context, HTTP_TRANSPORT: NetHttpTransport): Credential {
        // Load client secrets.

        val `in`: InputStream = context.resources.openRawResource(CREDENTIALS_FILE_ID)

        val clientSecrets =
            GoogleClientSecrets.load(
                JSON_FACTORY,
                InputStreamReader(`in`)
            )



        // Build flow and trigger user authorization request.
        val tokenFolder = File(context.getExternalFilesDir("")?.absolutePath + TOKENS_DIRECTORY_PATH)
        if (!tokenFolder.exists()) {
            tokenFolder.mkdirs()
        }

        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(tokenFolder))
            .setAccessType("offline")
            .build()

        //val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        //returns an authorized Credential object.

        val ab: AuthorizationCodeInstalledApp =
            object : AuthorizationCodeInstalledApp(flow, LocalServerReceiver()) {
                @Throws(IOException::class)
                override fun onAuthorization(authorizationUrl: AuthorizationCodeRequestUrl) {
                    val url = authorizationUrl.build()
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(browserIntent)
                }
            }
        return ab.authorize("user")
        //return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

//    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//    try {
//        GoogleSignInAccount account = task.getResult(ApiException.class);
//
//        // request a one-time authorization code that your server exchanges for an
//        // access token and sometimes refresh token
//        String authCode = account.getServerAuthCode();
//
//        // Show signed-in UI
//        updateUI(account);
//
//        // TODO(developer): send code to server and exchange for access/refresh/ID tokens
//    } catch (ApiException e) {
//        Log.w(TAG, "Sign-in failed", e);
//        updateUI(null);
//    }

    @Throws(IOException::class, GeneralSecurityException::class)
    @JvmStatic
    fun main(context: Context) {
        // Build a new authorized API client service.
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        val service = Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(context,HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build()

        // List the next 10 events from the primary calendar.
        val now = DateTime(System.currentTimeMillis())
        val events = service.events().list("primary")
            .setMaxResults(10)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute()
        val items = events.items
        if (items.isEmpty()) {
            Log.d("events", "No upcoming events found.")
        } else {
            Log.d("events","Upcoming events")
            for (event in items) {
                var start = event.start.dateTime
                if (start == null) {
                    start = event.start.date
                }
                Log.d("$start", event.summary)
                //System.out.printf("%s (%s)\n", event.summary, start)
            }
        }
    }

    // Attempt 2 :
    suspend fun signIn(context: Context) {
        val inputStream = context.resources.openRawResource(CREDENTIALS_FILE_ID)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line).append("\n")
            line = reader.readLine()
        }
        val jsonContent = stringBuilder.toString()

        // Parse the JSON content to extract the server_client_id
        val jsonObject = JSONObject(jsonContent)
        val serverClientId =
            jsonObject.getString("installed") // "web" is the key for web client credentials
                .let { JSONObject(it) }
                .getString("client_id")

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(serverClientId)
            //.setNonce("Wait a moment, generating Google ID Token")
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)
        coroutineScope {launch {
                try {
//                    val credentialManager = CredentialManager()
                    Log.d("hielel", "toehtui")
                   // Log.d("error place", "$credentialManager.getCredential(context = context, request = request)")
                    val result = credentialManager.getCredential(context, request)
                    Log.d("hielel2", "hi")
                    handleSignIn(result)
                } catch (e: GetCredentialException) {
                    Log.d("credential manager", "error fetching cred: ${e.errorMessage}", e)
                    Log.d("error", "coroutine scope failed")
                    //handleFailure(e)
                }
            }
        }
    }

        fun handleSignIn(result: GetCredentialResponse) {
            // Handle the successfully returned credential.
            Log.d("handlesign in", "in")
            val credential = result.credential

            when (credential) {
                is PublicKeyCredential -> {
                    // Share responseJson such as a GetCredentialResponse on your server to
                    // validate and authenticate
                    Log.d("public key cred", "in")
                    var responseJson = credential.authenticationResponseJson
                }

                is PasswordCredential -> {
                    Log.d("password cred", "in")
                    // Send ID and password to your server to validate and authenticate.
                    val username = credential.id
                    val password = credential.password
                }

                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        Log.d("Custom Crednetial", "in")
                        try {
                            // Use googleIdTokenCredential and extract id to validate and
                            // authenticate on your server.
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)
                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e(TAG, "Received an invalid google id token response", e)
                        }
                    } else {
                        // Catch any unrecognized custom credential type here.
                        Log.e(TAG, "Unexpected type of credential")
                    }
                }

                else -> {
                    // Catch any unrecognized credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
        }


    fun fetchEvents(calendarService: Calendar) {
        try {
            val events = calendarService.events().list("primary").execute()
            val items = events.items
            if (items.isEmpty()) {
                println("No upcoming events found.")
            } else {
                println("Upcoming events:")
                for (event in items) {
                    println("${event.summary} (${event.start.dateTime})")
                }
            }
        } catch (e: IOException) {
            println("Error fetching events: ${e.message}")
        }
    }

    fun addEvents(){}
    }

