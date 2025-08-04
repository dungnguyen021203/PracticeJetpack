package com.example.videocallapp.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
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
fun SignupScreen(viewModel: AuthViewModel? = hiltViewModel(), navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authResource = viewModel?.signUpFlow?.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AuthHeader()

        Text(
            text = "Become SneakerPuzz Member",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
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

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text(text = "Name")
            },
            leadingIcon = {
                Icon(Icons.Rounded.Person, contentDescription = "Name Leading Icon")
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
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

        Button(
            onClick = {
                viewModel?.signUp(email, name, password)
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
            Text(text = "Sign Up", style = MaterialTheme.typography.titleMedium)
        }

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                        launchSingleTop = true
                    }
                },
            text = "Already have an account?",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        authResource?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    LaunchedEffect(it) {
                        showToast(context = context, message = it.exception.message.toString())
                        viewModel.clearSignUpFlow()
                    }
                }

                is Resource.Loading -> {
                    LoadingCircle()
                }

                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                }
            }
        }
    }

}