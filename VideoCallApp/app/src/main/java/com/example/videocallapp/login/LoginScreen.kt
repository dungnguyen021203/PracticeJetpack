package com.example.videocallapp.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.videocallapp.R
import com.example.videocallapp.common.Resource
import com.example.videocallapp.helper.AuthHeader
import com.example.videocallapp.helper.LoadingCircle
import com.example.videocallapp.helper.showToast
import com.example.videocallapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel? = hiltViewModel(), navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var authResource = viewModel?.loginFlow?.collectAsState()
    val googleLoginState = viewModel?.googleLoginFlow?.collectAsState()

    val context = LocalContext.current
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        AuthHeader()

        Text(text = "Welcome to SneakerPuzz ", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(5.dp))

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
                imeAction = ImeAction.Next,
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            )
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = "Password Leading Icon")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Default.Visibility else
                    Icons.Default.VisibilityOff
                Icon(
                    imageVector = image,
                    contentDescription = "Password Trailing Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { passwordVisible = !passwordVisible })
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
                Text(
                    text = "Remember me", fontSize = 14.sp
                )
            }

            Text(
                modifier = Modifier
                    .clickable {
                        navController.navigate("forget") {
                            popUpTo("login") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                text = "Forget Password?",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }

        Button(
            onClick = {
                viewModel?.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A50FF),
                contentColor = Color.White
            )
        ) {
            Text(text = "Login", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = {
                navController.navigate("signup") {
                    popUpTo("login") { inclusive = false }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(text = "Create Account", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )
            Text(text = "Or sign in with", color = Color.Gray)
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(0.5.dp, Color.Gray, RoundedCornerShape(30.dp))
                .clickable {
                    viewModel?.loginWithGoogle()
                },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp),

                )
        }

        authResource?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    LaunchedEffect(it) {
                        showToast(context = context, message = it.exception.message.toString())
                        viewModel?.clearLoginFlow()
                    }
                }

                is Resource.Loading -> {
                    LoadingCircle()
                }

                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }

        googleLoginState?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    LaunchedEffect(it) {
                        showToast(context = context, message = it.exception.message.toString())
                        viewModel.clearGoogleSignUpFLow()
                    }
                }

                is Resource.Loading -> {
                    LoadingCircle()
                }

                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}