package de.hhn.dojo.dummy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.hhn.dojo.dummy.ui.components.AppBar
import de.hhn.dojo.dummy.ui.screens.MainScreen
import de.hhn.dojo.dummy.ui.theme.DojoDummyTheme

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided. Did you forget to provide it via CompositionLocalProvider?")
}


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                DojoDummyTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = { AppBar() },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        content = { innerPadding ->
                            Surface(modifier = Modifier.padding(innerPadding)) {
                                MainScreen()
                            }
                        }
                    )
                }
            }
        }
    }
}