package de.hhn.dojo.dummy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FlagDialog(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    bodyText: String? = null,
    flagToDisplay: String? = null,
    confirmButtonText: String = "OK",
    showCopyFlagButton: Boolean = flagToDisplay != null && flagToDisplay.isNotBlank(),
    onFlagCopied: (() -> Unit)? = null
) {
    val clipboard = LocalClipboardManager.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = icon?.let {
            { Icon(imageVector = it, contentDescription = "$title icon") }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                bodyText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = if (flagToDisplay != null) 12.dp else 0.dp)
                    )
                }
                flagToDisplay?.takeIf { it.isNotBlank() }?.let { flag ->
                    Text(
                        text = "Flag:", // Or "Code:", "Value:", etc.
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = if (bodyText != null) 8.dp else 0.dp, bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = flag,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(confirmButtonText)
            }
        },
        dismissButton = if (showCopyFlagButton && flagToDisplay != null) {
            {
                TextButton(onClick = {
                    clipboard.setText(AnnotatedString(flagToDisplay))
                    onFlagCopied?.invoke()
                }) {
                    Text("Copy")
                }
            }
        } else {
            null
        }
    )
}


// --- Previews ---

@Preview(showBackground = true, name = "Dialog with Flag and Body")
@Composable
fun FlexibleInfoDialogPreview_FlagAndBody() {
    MaterialTheme {
        FlagDialog(
            title = "Success!",
            icon = Icons.Filled.EmojiEvents,
            bodyText = "You have successfully completed the task. Here is your reward:",
            flagToDisplay = "FLAG{D1al0g_W1th_Styl3!}",
            onDismiss = {},
            confirmButtonText = "Awesome!"
        )
    }
}

@Preview(showBackground = true, name = "Dialog with Flag Only")
@Composable
fun FlexibleInfoDialogPreview_FlagOnly() {
    MaterialTheme {
        FlagDialog(
            title = "Flag Retrieved!",
            icon = Icons.Filled.Flag,
            flagToDisplay = "FLAG{Just_A_Flag}",
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, name = "Dialog with Body Only")
@Composable
fun FlexibleInfoDialogPreview_BodyOnly() {
    MaterialTheme {
        FlagDialog(
            title = "Important Information",
            bodyText = "This is a general information message displayed in the dialog. There is no flag to show here.",
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, name = "Dialog with Title Only")
@Composable
fun FlexibleInfoDialogPreview_TitleOnly() {
    MaterialTheme {
        FlagDialog(
            title = "Process Complete",
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, name = "Dialog with no Copy Button")
@Composable
fun FlexibleInfoDialogPreview_NoCopy() {
    MaterialTheme {
        FlagDialog(
            title = "Flag (No Copy)",
            flagToDisplay = "FLAG{No_Copy_Option}",
            showCopyFlagButton = false, // Explizit deaktiviert
            onDismiss = {}
        )
    }
}