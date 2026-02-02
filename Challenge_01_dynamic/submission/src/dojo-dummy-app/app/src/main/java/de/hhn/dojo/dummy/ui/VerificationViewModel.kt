package de.hhn.dojo.dummy.ui

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.hhn.dojo.dummy.network.DaddelConnector
import de.hhn.dojo.dummy.network.ServerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

sealed class VerificationUiState {
    object Idle : VerificationUiState()
    object Loading : VerificationUiState()
    data class FlagSuccess(val flag: String) : VerificationUiState()
    data class MessageSuccess(val message: String) : VerificationUiState()
    data class Error(val message: String) : VerificationUiState()
}

class VerificationViewModel : ViewModel() {

    private val _verificationUiState = MutableStateFlow<VerificationUiState>(VerificationUiState.Idle)
    val verificationUiState: StateFlow<VerificationUiState> = _verificationUiState.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>()
    val snackbarMessages = _snackbarMessages.asSharedFlow()

    private fun getSystemProperty(propName: String): String? {
        return try {
            val process = Runtime.getRuntime().exec("getprop $propName")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            bufferedReader.readLine()?.trim().let { if (it.isNullOrEmpty()) null else it }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun executeServerCall(
        call: suspend () -> ServerResult,
        onSuccess: (ServerResult.Success) -> VerificationUiState = { VerificationUiState.MessageSuccess(it.message) }
    ) {
        viewModelScope.launch {
            _verificationUiState.value = VerificationUiState.Loading
            when (val result = withContext(Dispatchers.IO) { call() }) {
                is ServerResult.Success -> _verificationUiState.value = onSuccess(result)
                is ServerResult.Error -> _verificationUiState.value = VerificationUiState.Error(result.message)
            }
        }
    }

    fun submitEmulatorProperties(
        propertiesToSend: Map<String, String>,
        targetRuleName: String? = null
    ) {
        executeServerCall({ DaddelConnector.send(propertiesToSend, targetRuleName) })
    }

    fun sendManualRuleSignal(ruleStepName: String) {
        val commandString = "VERIFY:$ruleStepName"
        executeServerCall({ DaddelConnector.send(command = commandString) })
    }

    fun requestFlag() {
        executeServerCall(
            call = { DaddelConnector.send(command = "getFlag") },
            onSuccess = { result ->
                result.rawFlag?.let { flag ->
                    VerificationUiState.FlagSuccess(flag)
                } ?: VerificationUiState.MessageSuccess(result.message)
            }
        )
    }


    fun triggerDynamicFlagRequestEncrypted() {
        viewModelScope.launch {
            _snackbarMessages.emit("Encrypted flag request triggered (not implemented yet).")
        }
    }



    fun sendSnackbarMessage(message: String) {
        viewModelScope.launch {
            _snackbarMessages.emit(message)
        }
    }

    fun resetState() {
        _verificationUiState.value = VerificationUiState.Idle
    }
}