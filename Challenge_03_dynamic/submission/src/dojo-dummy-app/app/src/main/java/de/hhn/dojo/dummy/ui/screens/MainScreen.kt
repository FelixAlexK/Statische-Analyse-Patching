package de.hhn.dojo.dummy.ui.screens

import android.os.Build
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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

    LaunchedEffect(key1 = verificationViewModel.snackbarMessages, key2 = snackbarHostState) {
        verificationViewModel.snackbarMessages.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    when (val state = verificationState) {
        is VerificationUiState.FlagSuccess -> {
            FlagDialog(
                onDismiss = { verificationViewModel.resetState() },
                title = "Flag Retrieved!",
                icon = Icons.Filled.Flag,
                bodyText = "You have successfully completed the task.",
                flagToDisplay = state.flag,
                onFlagCopied = {
                    verificationViewModel.sendSnackbarMessage("Flag copied to clipboard!")
                }
            )
        }

        is VerificationUiState.MessageSuccess -> {
            LaunchedEffect(state) {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                verificationViewModel.resetState()
            }
        }

        is VerificationUiState.Error -> {
            LaunchedEffect(state) {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Long
                )
                verificationViewModel.resetState()
            }
        }

        else -> {
            /* Idle or Loading */
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (verificationState is VerificationUiState.Loading) 1f else 0f)
        )
        MainActions(
            onGetDynamicFlagPlain = { verificationViewModel.requestFlag() },
            onGetDynamicFlagEncrypted = { verificationViewModel.triggerDynamicFlagRequestEncrypted() },
            onVerifyEmulatorProperties = {
                val propertiesToSend = mapOf(
                    "ro.product.model" to Build.MODEL,
                    "ro.product.manufacturer" to Build.MANUFACTURER,
                    "ro.build.version.sdk" to Build.VERSION.SDK_INT.toString()
                )
                verificationViewModel.submitEmulatorProperties(
                    propertiesToSend = propertiesToSend,
                    targetRuleName = "emulator_properties_check"
                )
            },
            onVerifyLogMessage = {
                Log.d("onVerifyLogMessage", "Log Example!")
            },
            onVerifyManual = {
                Log.d("onVerifyManual", "Log Example 2!")
                verificationViewModel.sendManualRuleSignal("manual_example")
            }
        )
    }
}

@Composable
fun MainActions(
    onGetDynamicFlagPlain: () -> Unit,
    onGetDynamicFlagEncrypted: () -> Unit,
    onVerifyEmulatorProperties: () -> Unit,
    onVerifyLogMessage: () -> Unit,
    onVerifyManual: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        ActionItem(
            title = "Get Flag",
            description = "Demo for unencrypted flag",
            icon = Icons.Outlined.LockOpen,
            onClick = onGetDynamicFlagPlain,
        )
        HorizontalDivider(Modifier.padding(16.dp))
        ActionItem(
            title = "Rule: Emulator Properties",
            description = "Trigger emulator rule verification event",
            icon = Icons.Outlined.Devices,
            onClick = onVerifyEmulatorProperties,
        )
        ActionItem(
            title = "Rule: Logcat",
            description = "Trigger logcat rule verification event",
            icon = Icons.AutoMirrored.Outlined.Message,
            onClick = onVerifyLogMessage,
        )
        ActionItem(
            title = "Rule: Manual",
            description = "Trigger manual rule verification event",
            icon = Icons.AutoMirrored.Outlined.Message,
            onClick = onVerifyManual,
        )
    }
}

@Composable
fun ActionItem(
    title: String,
    description: String?,
    icon: ImageVector?,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(bottom = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ListItem(
            modifier = Modifier
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    }
                ),
            headlineContent = { Text(text = title) },
            supportingContent = description?.let { { Text(text = it) } },
            leadingContent = icon?.let {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = title
                    )
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}