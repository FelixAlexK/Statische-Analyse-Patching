package de.hhn.dojo.dummy

data class MainScreenUiState(
    val isLoadingFlag: Boolean = false,
    val showFlagDialog: Boolean = false,
    val flagForDialog: String = "",
    val lastError: String? = null
)
