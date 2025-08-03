package com.example.videocallapp.repository_impl

import com.example.videocallapp.common.Resource
import com.example.videocallapp.helper.await
import com.example.videocallapp.model.UserModel
import com.example.videocallapp.repository.AuthRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signUp(
        email: String,
        name: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Resource<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user ?: return Resource.Failure(Exception("User Null"))
            Resource.Success(user)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }

    override suspend fun resetPassword(email: String): Resource<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserInformation(): Resource<UserModel> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Failure(Exception("No authenticated user"))

            val snapshot = firebaseFirestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = snapshot.toObject(UserModel::class.java)
                ?: return Resource.Failure(Exception("User data not found"))

            Resource.Success(user)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Resource<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            val email = firebaseAuth.currentUser?.email
            if (email.isNullOrEmpty()) {
                return Resource.Failure(Exception("Email người dùng không tồn tại"))
            }
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            user?.reauthenticate(credential)?.await()
            user?.updatePassword(newPassword)?.await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateUser(
        uid: String,
        updatedUser: UserModel
    ): Resource<Unit> {
        return try {
            firebaseFirestore.collection("users").document(uid).set(updatedUser).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteAccount(uid: String): Resource<Unit> {
        return try {
            firebaseFirestore.collection("users").document(uid).delete().await()
            firebaseAuth.currentUser?.delete()?.await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}