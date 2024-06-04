package com.stopsmoke.kekkek.authorization.google

import android.app.Activity
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.BuildConfig

class GoogleAuthorization(
    private val fragment: Fragment,
) {
    private var callback: GoogleAuthorizationCallbackListener? = null

    private lateinit var googleSighInClient: GoogleSignInClient

    private val googleLoginResult =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                    e.printStackTrace()
                    callback?.onFailure(e)
                } catch (e: Exception) {
                    callback?.onFailure(e)
                }
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth = Firebase.auth
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(fragment.requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    callback?.onSuccess(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(
                        /* tag = */ TAG,
                        /* msg = */ "signInWithCredential:failure",
                        /* tr = */ task.exception
                    )
                }
            }
            .addOnFailureListener {
                callback?.onFailure(it)
                it.printStackTrace()
            }
    }

    fun launchGoogleAuthActivity() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIRBASE_AUTH_SERVCER_CLIENT_KEY)
            .requestId()
            .requestEmail()
            .requestProfile()
            .build()

        googleSighInClient = GoogleSignIn.getClient(fragment.requireContext(), options)
        googleLoginResult.launch(googleSighInClient.signInIntent)
    }

    fun registerCallbackListener(callback: GoogleAuthorizationCallbackListener) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }

    companion object {
        private const val TAG = "GoogleAuthorization"
    }
}