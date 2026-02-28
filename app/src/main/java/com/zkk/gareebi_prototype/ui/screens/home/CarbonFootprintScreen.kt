package com.zkk.gareebi_prototype.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zkk.gareebi_prototype.viewmodel.HomeViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarbonFootprintScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Logic for Red vs Green
    val isUsingMore = uiState.currentMonthKwh > uiState.lastMonthKwh
    val difference = abs(uiState.currentMonthKwh - uiState.lastMonthKwh)
    val statusColor = if (isUsingMore) Color(0xFFD32F2F) else Color(0xFF2E7D32) // Red vs Green
    val statusText = if (isUsingMore) "Higher than last month" else "Lower than last month"
    val sign = if (isUsingMore) "+" else "-"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Footprint Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Big Hero Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your Total Footprint", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = String.format("%.1f kg CO₂", uiState.carbonFootprintKg),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = statusColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Red/Green Indicator Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = statusColor.copy(alpha = 0.1f),
                    contentColor = statusColor
                ) {
                    Text(
                        text = "$sign${String.format("%.1f", difference)} kWh ($statusText)",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider()

            // Usage Breakdown
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("This Month", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text("${uiState.currentMonthKwh} kWh", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Last Month", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text("${uiState.lastMonthKwh} kWh", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider()

            // The Formula Section
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("How is this calculated?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your carbon footprint is based on your total energy consumption multiplied by the Grid Emission Factor (the average CO₂ emitted to generate 1 kWh of electricity in your region).",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // The Standard Math Formula
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Footprint = kWh Consumed × 0.71 kg/kWh",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}