package com.example.sneakerpuzzshop.presentation.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.presentation.components.AuthHeader
import com.example.sneakerpuzzshop.presentation.viewmodel.AuthViewModel
import com.example.sneakerpuzzshop.utils.ROUTE_HOME
import com.example.sneakerpuzzshop.utils.ROUTE_LOGIN
import com.example.sneakerpuzzshop.utils.ROUTE_SIGNUP
import com.example.sneakerpuzzshop.R
import com.example.sneakerpuzzshop.presentation.components.showToast
import com.example.sneakerpuzzshop.utils.LoadingCircle
import com.example.sneakerpuzzshop.utils.ROUTE_FORGET_PW

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: AuthViewModel?, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var authResource = viewModel?.loginFlow?.collectAsState()
    val googleLoginState = viewModel?.googleLoginFlow?.collectAsState()

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        AuthHeader()

        Text(text = "Welcome to SneakerPuzz ", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(16.dp))

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
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                    painterResource(id = R.drawable.visible) else
                    painterResource(id = R.drawable.visibility)
                Icon(
                    painter = image,
                    contentDescription = "Password Trailing Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { passwordVisible = !passwordVisible })
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate(ROUTE_FORGET_PW) {
                        popUpTo(ROUTE_LOGIN) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            text = "Forget Password?. Click here",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel?.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 90.dp)
        ) {
            Text(text = "Login", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { viewModel?.loginWithGoogle() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 90.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Google Sign In", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate(ROUTE_SIGNUP) {
                        popUpTo(ROUTE_LOGIN) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            text = "Don't have account?. Click here",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

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
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_LOGIN) { inclusive = true }
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
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}
