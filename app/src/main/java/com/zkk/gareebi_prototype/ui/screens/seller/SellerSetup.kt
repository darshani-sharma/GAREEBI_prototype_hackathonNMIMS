package com.zkk.gareebi_prototype.ui.screens.seller

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SellerSetupScreen(navController: NavController, resourceType: String) {
    var ivrfNumber by remember { mutableStateOf("") }
    var subsidyId by remember { mutableStateOf("") }
    var expectedOutput by remember { mutableStateOf("") }

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