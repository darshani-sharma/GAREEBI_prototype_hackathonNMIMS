
package com.zkk.gareebi_prototype.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// A simple data class to hold our past months' data
data class MonthUsage(val month: String, val kwh: Double)

data class HomeUiState(
    val currentMonthKwh: Double = 145.5,
    val lastMonthKwh: Double = 160.0,
    val userName: String = "Jay Choukikar",
    val ivrfNumber: String = "N1234567786",
    val phone: String = "+91 9595959595",
    // 3-Line Address Setup
    val addressLine1: String = "Plot No. 42, Solar Enclave",
    val addressLine2: String = "Vijay Nagar Sector A",
    val addressLine3: String = "Indore, MP 452010",
    // Mock data for the historical blocks
    val pastUsage: List<MonthUsage> = listOf(
        MonthUsage("February 2026", 160.0),
        MonthUsage("January 2026", 155.5),
        MonthUsage("December 2025", 142.0),
        MonthUsage("November 2025", 138.5)
    )
) {
    val carbonFootprintKg: Double
        get() = currentMonthKwh * 0.71
}

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun updateEnergyConsumption(newCurrentKwh: Double) {
        _uiState.value = _uiState.value.copy(currentMonthKwh = newCurrentKwh)
    }
}