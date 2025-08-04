package com.example.videocallapp.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.videocallapp.common.Resource
import com.example.videocallapp.helper.AuthHeader
import com.example.videocallapp.helper.LoadingCircle
import com.example.videocallapp.helper.showToast
import com.example.videocallapp.viewmodel.AuthViewModel

@Composable
fun ForgetPassword(viewModel: AuthViewModel = hiltViewModel(), navController: NavHostController) {
    var email by remember { mutableStateOf("") }

    val passwordResetState = viewModel.resetPasswordFlow.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AuthHeader()

        Text(text = "Welcome to SneakerPuzz", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        Text(
            text = "Enter your email. We got you!!!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email")
            },
            leadingIcon = {
                Icon(Icons.Rounded.Email, contentDescription = "Account Leading Icon")
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            )
        )

        Button(
            onClick = {
                viewModel.resetPassword(email)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A50FF),
                contentColor = Color.White
            )
        ) {
            Text(text = "Reset Password", style = MaterialTheme.typography.titleMedium)
        }

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate("login") {
                        popUpTo("forget") { inclusive = true }
                        launchSingleTop = true
                    }
                },
            text = "Back to Login",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }

    passwordResetState.value?.let {
        when (it) {
            is Resource.Failure -> {
                LaunchedEffect(it) {
                    showToast(context = context, message = it.exception.message.toString())
                    viewModel.clearResetPasswordFlow()
                }
            }

            Resource.Loading -> LoadingCircle()
            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    showToast(context = context, message = "Kiểm tra email của bạn")
                    navController.navigate("login") {
                        popUpTo("forget") { inclusive = true }
                    }
                    viewModel.clearResetPasswordFlow()
                }
            }
        }

    }
}