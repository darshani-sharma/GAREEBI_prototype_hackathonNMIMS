package com.zkk.gareebi_prototype.viewmodel

import androidx.lifecycle.ViewModel
import com.zkk.gareebi_prototype.data.model.Vendor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VendorUiState(
    val vendors: List<Vendor> = emptyList(),
    val isLoading: Boolean = false
)

class VendorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VendorUiState())
    val uiState: StateFlow<VendorUiState> = _uiState.asStateFlow()

    init {
        loadMockVendors()
    }

    private fun loadMockVendors() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        // This simulates fetching from your backend/database
        val dummyData = listOf(
            Vendor("Ramesh Solar Co.", "Produces: 150 kWh/day", "Selling since 2021", "₹6.50/Unit"),
            Vendor("Sharma Grid Traders", "Produces: 300 kWh/day", "Selling since 2019", "₹6.20/Unit"),
            Vendor("Priya EcoPower", "Produces: 50 kWh/day", "Selling since 2023", "₹6.80/Unit"),
            Vendor("Verma Microgrid", "Produces: 80 kWh/day", "Selling since 2022", "₹6.40/Unit")
        )

        _uiState.value = _uiState.value.copy(
            vendors = dummyData,
            isLoading = false
        )
    }
}