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
        // SCHRITT 1: Teil aus dem Manifest holen (Statischer Teil 1)
        val ai = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        val part1 = ai.metaData.getString("secret_part_1") ?: ""

        // SCHRITT 2: Teil aus den Strings holen (Statischer Teil 2)
        // Hinweis: Die ID R.string.secret_part_2 muss in res/values/strings.xml existieren
        val part2Id = context.resources.getIdentifier("secret_part_2", "string", context.packageName)
        val part2 = if (part2Id != 0) context.getString(part2Id) else ""

        // SCHRITT 3: Hartcodierter Teil (Statischer Teil 3)
        val part3 = "anatomy"

        val correctPassword = part1 + part2 + part3
        val correctUser = "admin" // Kannst du auch verstecken

        if (userIn == correctUser && passIn == correctPassword) {
            // VERIFIZIERUNG: Dieser Log wird vom externen Tool gelesen
            Log.d("ChallengeVerify", "AUTH_SUCCESS_FULL_PIECES")

            // Trigger das ViewModel, um die Flagge anzuzeigen (z.B. über API oder intern)
            viewModel.requestFlag()
        } else {
            Log.w("ChallengeVerify", "AUTH_FAILED: Access Denied.")
            viewModel.sendSnackbarMessage("Invalid Credentials! Check the code anatomy.")
        }

    } catch (e: Exception) {
        Log.e("ChallengeVerify", "Error during decryption: ${e.message}")
    }
}