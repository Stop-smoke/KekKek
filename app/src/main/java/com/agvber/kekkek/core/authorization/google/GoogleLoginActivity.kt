package com.agvber.kekkek.core.authorization.google

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.agvber.kekkek.BuildConfig
import com.agvber.kekkek.core.authorization.model.AuthorizationToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GoogleLoginActivity : ComponentActivity() {

    @Inject
    lateinit var newGoogleAuthorization: NewGoogleAuthorization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { requestGoogleLogin() }
    }

    private suspend fun requestGoogleLogin() = runCatching {
        val googleIdOption = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            .setServerClientId(BuildConfig.FIRBASE_AUTH_SERVCER_CLIENT_KEY)
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(true)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(this)

        // Launch Credential Manager UI
        val result = credentialManager.getCredential(
            context = this,
            request = request
        )

        // Extract credential from the result returned by Credential Manager
        handleSignIn(result.credential)
    }
        .let {
            newGoogleAuthorization.setResult(it)
            finish()
        }

    private fun handleSignIn(credential: Credential): AuthorizationToken {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            return AuthorizationToken(googleIdTokenCredential.idToken, "")
        } else {
            throw IllegalStateException("Invalid credential type")
        }
    }
}
