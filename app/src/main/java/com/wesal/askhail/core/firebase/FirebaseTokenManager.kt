package com.wesal.askhail.core.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.wesal.domain.useCases.UseCaseImpl
import kotlinx.coroutines.tasks.await

class FirebaseTokenManager {
    suspend fun isValidToken(): Boolean {
        val token = Firebase.messaging.token.await()
        // Check whether the retrieved token matches the one on your server for this user's device
        val tokenStored: String = UseCaseImpl().getFirebaseToken()
        if (tokenStored == "" || tokenStored != token) {
            UseCaseImpl().saveFireBaseToken(token)
            return false
            //send it to server
        }
        return true
    }
}