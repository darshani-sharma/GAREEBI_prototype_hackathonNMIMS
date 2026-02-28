package com.zkk.gareebi_prototype.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zkk.gareebi_prototype.ui.components.QuickActionButton
import com.zkk.gareebi_prototype.viewmodel.HomeUiState
import com.zkk.gareebi_prototype.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToCarbonFootprint: () -> Unit,
    onNavigateToUserProfile: () -> Unit, // ADDED: New route for User Profile
    onNavigateToElectricityBuy: () -> Unit,
    onNavigateToBioGasBuy: () -> Unit,
    onNavigateToSellForm: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Energy Market", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Pass both click handlers down!
            TopSection(uiState, onNavigateToCarbonFootprint, onNavigateToUserProfile)

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            BuySection(onNavigateToElectricityBuy, onNavigateToBioGasBuy)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SellSection(onNavigateToSellForm)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TopSection(
    uiState: HomeUiState,
    onFootprintClick: () -> Unit,
    onProfileClick: () -> Unit // ADDED: Handler for the right card
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Card: Carbon Footprint
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .clickable { onFootprintClick() },
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Carbon Footprint",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = String.format("%.1f kg", uiState.carbonFootprintKg),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // Right Card: User Profile
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .clickable { onProfileClick() }, // ADDED: Make it clickable!
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = uiState.userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                // UPDATED: Show "Used This Month" instead of IVRF and Location
                Text(
                    text = "Used This Month:",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${uiState.currentMonthKwh} kWh",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BuySection(onNavigateToElectricity: () -> Unit, onNavigateToBioGas: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Quick Top-Up", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionButton("Buy Electricity", Icons.Default.Add, onNavigateToElectricity)
            QuickActionButton("Buy Bio Gas", Icons.Default.Settings, onNavigateToBioGas)
        }
    }
}

@Composable
private fun SellSection(onNavigateToSellForm: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Marketplace Sellers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionButton("Sell Electricity", Icons.Default.Add, { onNavigateToSellForm("Electricity") })
            QuickActionButton("Sell Gas", Icons.Default.Settings, { onNavigateToSellForm("Gas") })
        }
    }
}