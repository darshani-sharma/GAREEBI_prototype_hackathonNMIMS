package com.zkk.gareebi_prototype.ui.screens.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zkk.gareebi_prototype.data.model.Vendor
import com.zkk.gareebi_prototype.viewmodel.VendorViewModel

@Composable
fun VendorListScreen(
    navController: NavController,
    viewModel: VendorViewModel = viewModel() // Injecting the ViewModel here
) {
    // Observing the StateFlow. The UI will redraw if the list changes!
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(top = 48.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Button(onClick = { navController.popBackStack() }) { Text("Back") }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Select Electricity Vendor", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Drawing the vendors from the ViewModel's state
            items(uiState.vendors) { vendor ->
                VendorCard(vendor) { /* TODO: Trigger buy action */ }
            }
        }
    }
}

@Composable
private fun VendorCard(vendor: Vendor, onBuyClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = vendor.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = vendor.capacity, style = MaterialTheme.typography.bodyMedium)
                Text(text = vendor.experience, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = vendor.pricePerUnit, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onBuyClick, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                    Text("Buy")
                }
            }
        }
    }
}