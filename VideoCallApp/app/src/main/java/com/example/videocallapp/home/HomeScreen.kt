package com.example.videocallapp.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.videocallapp.MainActivity
import com.example.videocallapp.appID
import com.example.videocallapp.appSign
import com.example.videocallapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

@SuppressLint("ContextCastToActivity")
@Composable
fun HomeScreen(authViewModel: AuthViewModel = hiltViewModel(), navController: NavHostController) {
    val context = LocalContext.current as MainActivity

    LaunchedEffect(key1 = Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            context.initZegoInviteService(appID, appSign, it.email!!, it.email!!)
        }
    }


    var targetUser by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "UserID: ${FirebaseAuth.getInstance().currentUser?.email}")

        OutlinedTextField(
            value = targetUser,
            onValueChange = { targetUser = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Invite user")
            }
        )

        Row {
            CallButton(isVideoCall = false) { button ->
                if (targetUser.isNotEmpty()) {
                    button.setInvitees(
                        mutableListOf(
                            ZegoUIKitUser(
                                targetUser, targetUser
                            )
                        )
                    )
                }
            }
            CallButton(isVideoCall = true) { button ->
                if (targetUser.isNotEmpty()) {
                    button.setInvitees(
                        mutableListOf(
                            ZegoUIKitUser(
                                targetUser, targetUser
                            )
                        )
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextButton(
                onClick = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Text(text = "Sign Out", fontSize = 16.sp, color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun CallButton(isVideoCall: Boolean, onClick: (ZegoSendCallInvitationButton) -> Unit) {
    AndroidView(
        factory = { context ->
            val button = ZegoSendCallInvitationButton(context)
            button.setIsVideoCall(isVideoCall)
            button.resourceID = ("zego_data")
            button
        },
        modifier = Modifier.size(50.dp)
    ) { zegoCallButton ->
        zegoCallButton.setOnClickListener { _ -> onClick(zegoCallButton) }
    }
}