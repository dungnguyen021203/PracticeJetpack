package com.example.videocallapp.helper

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.videocallapp.common.Resource
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class GoogleSignInHelper @Inject constructor(
    private val context: Context,
) {
    private val credentialManager = CredentialManager.Companion.create(context)

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                        "105601437924-k2rlkns6sok75up5j1gp5isa4dn7o63d.apps.googleusercontent.com"
                    )
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()

        return credentialManager.getCredential(
            request = request, context = context
        )
    }

    suspend fun getIdTokenFromGoogle(): Resource<String> {
        return try {
            val result = buildCredentialRequest()
            val credential = result.credential
            if(
                credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val tokenCredential = GoogleIdTokenCredential.Companion.createFrom(credential.data)
                val idToken = tokenCredential.idToken
                Resource.Success(idToken)
            } else {
                Resource.Failure(Exception("Id token ko hợp lệ"))
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun clearGoogleCredentialState() {
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
    }
}