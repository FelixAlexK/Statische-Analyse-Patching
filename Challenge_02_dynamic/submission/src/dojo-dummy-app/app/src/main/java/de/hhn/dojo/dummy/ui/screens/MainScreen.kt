package de.hhn.dojo.dummy.ui.screens

import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.hhn.dojo.dummy.LocalSnackbarHostState
import de.hhn.dojo.dummy.ui.VerificationUiState
import de.hhn.dojo.dummy.ui.VerificationViewModel
import de.hhn.dojo.dummy.ui.components.FlagDialog
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    verificationViewModel: VerificationViewModel = viewModel()
) {
    val verificationState by verificationViewModel.verificationUiState.collectAsState()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current

    // State für die Eingabefelder
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(key1 = verificationViewModel.snackbarMessages, key2 = snackbarHostState) {
        verificationViewModel.snackbarMessages.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    // Flag-Dialog Handling (bleibt gleich)
    when (val state = verificationState) {
        is VerificationUiState.FlagSuccess -> {
            FlagDialog(
                onDismiss = { verificationViewModel.resetState() },
                title = "Flag Retrieved!",
                icon = Icons.Filled.Flag,
                bodyText = "You have successfully patched the logic.",
                flagToDisplay = state.flag,
                onFlagCopied = {
                    verificationViewModel.sendSnackbarMessage("Flag copied to clipboard!")
                }
            )
        }
        else -> { /* Error/Message handling logic here if needed */ }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (verificationState is VerificationUiState.Loading) 1f else 0f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Code Anatomy Login",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Username Feld
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(0.8f).padding(bottom = 16.dp)
        )

        // Password Feld
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.8f).padding(bottom = 24.dp)
        )

        // Login Button
        Button(
            onClick = {
                checkLogin(username, password, context, verificationViewModel)
            },
            modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
        ) {
            Text("Unlock System")
        }
    }
}

private fun verifyPassword(input: String): Boolean {
    if (input.length != 16) return false

    var acc = 0
    for (c in input) {
        acc = (acc * 31) xor c.code
    }

    return acc == 0x5f3759df  // praktisch nicht erratbar
}

private fun isCorrectUser(u: String): Boolean {
    return u.lowercase().reversed() == "nimda"
}

/**
 * Die Logik für die Code-Anatomy Challenge
 */
private fun checkLogin(
    userIn: String,
    passIn: String,
    context: android.content.Context,
    viewModel: VerificationViewModel
) {
    try {
        if (isCorrectUser(userIn) && verifyPassword(passIn)) {
            Log.d("LicenseManager", "BYPASS_DETECTED_SUCCESS")
            viewModel.requestFlag()
        } else {
            Log.w(
                "Hint",
                "Crypto looks strong. But what decides success or failure?"
            )
            viewModel.sendSnackbarMessage("Access denied.")
        }

    } catch (e: Exception) {
        Log.e("ChallengeVerify", "Error during decryption: ${e.message}")
    }
}