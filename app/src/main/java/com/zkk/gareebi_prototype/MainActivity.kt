package com.zkk.gareebi_prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zkk.gareebi_prototype.ui.theme.GAREEBI_prototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GAREEBI_prototypeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                onNavigateToElectricityBuy = { navController.navigate("electricity_vendors") },
                                onNavigateToBioGasBuy = { navController.navigate("bio_gas") },
                                onNavigateToSellForm = { resourceType ->
                                    navController.navigate("seller_setup/$resourceType")
                                }
                            )
                        }

                        composable("electricity_vendors") { VendorListScreen(navController) }
                        composable("bio_gas") { DummyPage("Bio Gas: To be implemented", navController) }

                        // New dynamic route for selling different resources
                        composable("seller_setup/{resourceType}") { backStackEntry ->
                            val resourceType = backStackEntry.arguments?.getString("resourceType") ?: "Resource"
                            SellerSetupScreen(navController, resourceType)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "IVRF Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLoginSuccess) {
            Text(text = "Mock Login (Verify IVRF)")
        }
    }
}

@Composable
fun HomeScreen(
    onNavigateToElectricityBuy: () -> Unit,
    onNavigateToBioGasBuy: () -> Unit,
    onNavigateToSellForm: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TopSection()
        HorizontalDivider()
        BuySection(onNavigateToElectricityBuy, onNavigateToBioGasBuy)
        HorizontalDivider()
        SellSection(onNavigateToSellForm)
    }
}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(modifier = Modifier.weight(0.5f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Carbon Commission", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+125 Credits",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(modifier = Modifier.weight(0.5f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Jay Choukikar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "IVRF: N1234567786", style = MaterialTheme.typography.bodySmall)
                Text(text = "Ph: +91 9595959595", style = MaterialTheme.typography.bodySmall)
                Text(text = "Loc: Indore, MP", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun BuySection(onNavigateToElectricity: () -> Unit, onNavigateToBioGas: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Quick Top-Up", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
fun SellSection(onNavigateToSellForm: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Sellers", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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

@Composable
fun QuickActionButton(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    }
}

// --- NEW SELLER SETUP SCREEN ---

@Composable
fun SellerSetupScreen(navController: NavController, resourceType: String) {
    var ivrfNumber by remember { mutableStateOf("") }
    var subsidyId by remember { mutableStateOf("") }
    var expectedOutput by remember { mutableStateOf("") }

    // Dropdown State
    var expanded by remember { mutableStateOf(false) }
    var selectedSource by remember { mutableStateOf("Select Source Type") }
    val sourceOptions = if (resourceType == "Electricity") {
        listOf("Solar Panels", "Wind Turbine", "Micro-Hydro")
    } else {
        listOf("Domestic Digester", "Community Plant", "Agricultural Waste")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { navController.popBackStack() }) { Text("Back") }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Sell $resourceType", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Form Fields
        OutlinedTextField(
            value = ivrfNumber,
            onValueChange = { ivrfNumber = it },
            label = { Text("IVRF Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = subsidyId,
            onValueChange = { subsidyId = it },
            label = { Text("Government Subsidy ID (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown Menu
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedSource,
                onValueChange = {},
                readOnly = true,
                label = { Text("Energy Source") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, "dropdown", Modifier.clickable { expanded = true })
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                sourceOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedSource = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = expectedOutput,
            onValueChange = { expectedOutput = it },
            label = { Text("Expected Daily Output (Units)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* TODO: Submit form to backend */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register as Seller")
        }
    }
}

data class Vendor(val name: String, val capacity: String, val experience: String, val pricePerUnit: String)

@Composable
fun VendorListScreen(navController: NavController) {
    val vendors = listOf(
        Vendor("Ramesh Solar Co.", "Produces: 150 kWh/day", "Selling since 2021", "₹6.50/Unit"),
        Vendor("Sharma Grid Traders", "Produces: 300 kWh/day", "Selling since 2019", "₹6.20/Unit"),
        Vendor("Priya EcoPower", "Produces: 50 kWh/day", "Selling since 2023", "₹6.80/Unit"),
        Vendor("Verma Microgrid", "Produces: 80 kWh/day", "Selling since 2022", "₹6.40/Unit")
    )

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
            items(vendors) { vendor ->
                VendorCard(vendor) { }
            }
        }
    }
}

@Composable
fun VendorCard(vendor: Vendor, onBuyClick: () -> Unit) {
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

@Composable
fun DummyPage(title: String, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}