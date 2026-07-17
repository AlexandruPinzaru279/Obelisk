package com.example.idletest.ui.screen

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.idletest.data.local.AuthStorage
import com.example.idletest.data.remote.RetrofitClient
import com.example.idletest.data.remote.auth.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Obelisk",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorMessage = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Username")
            },
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !isLoading
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading &&
                    username.isNotBlank() &&
                    password.isNotBlank(),
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null

                    try {
                        val response = RetrofitClient.authApi.login(
                            LoginRequest(
                                username = username,
                                password = password
                            )
                        )

                        AuthStorage.saveSession(
                            context = context,
                            token = response.token,
                            username = response.username
                        )

                        onLoginSuccess()
                    } catch (exception: HttpException) {
                        errorMessage = when (exception.code()) {
                            400 -> "Completeaza corect toate campurile."
                            401 -> "Username sau parola incorecta."
                            else -> "Eroare de server: ${exception.code()}."
                        }
                    } catch (_: IOException) {
                        errorMessage =
                            "Nu s-a putut realiza conexiunea la server."
                    } catch (_: Exception) {
                        errorMessage = "A aparut o eroare neasteptata."
                    } finally {
                        isLoading = false
                    }
                }
            }
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Login")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            enabled = !isLoading,
            onClick = onRegisterClick
        ) {
            Text(text = "Create account")
        }
    }
}