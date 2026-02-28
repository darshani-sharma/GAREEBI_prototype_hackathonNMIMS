package com.zkk.gareebi_prototype.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Imports for all our screens
import com.zkk.gareebi_prototype.ui.screens.splash.SplashScreen
import com.zkk.gareebi_prototype.ui.screens.home.HomeScreen
import com.zkk.gareebi_prototype.ui.screens.home.CarbonFootprintScreen
import com.zkk.gareebi_prototype.ui.screens.home.UserProfileScreen
import com.zkk.gareebi_prototype.ui.screens.vendor.VendorListScreen
import com.zkk.gareebi_prototype.ui.screens.vendor.DummyPage
import com.zkk.gareebi_prototype.ui.screens.seller.SellerSetupScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToCarbonFootprint = { navController.navigate("carbon_footprint_details") },
                onNavigateToUserProfile = { navController.navigate("user_profile") }, // ADDED: Routing for User Profile
                onNavigateToElectricityBuy = { navController.navigate("electricity_vendors") },
                onNavigateToBioGasBuy = { navController.navigate("bio_gas") },
                onNavigateToSellForm = { resourceType ->
                    navController.navigate("seller_setup/$resourceType")
                }
            )
        }

        composable("carbon_footprint_details") {
            CarbonFootprintScreen(navController)
        }

        // ADDED: The new User Profile Screen route
        composable("user_profile") {
            UserProfileScreen(navController)
        }

        composable("electricity_vendors") {
            VendorListScreen(navController)
        }

        composable("bio_gas") {
            DummyPage("Bio Gas: To be implemented", navController)
        }

        composable("seller_setup/{resourceType}") { backStackEntry ->
            val resourceType = backStackEntry.arguments?.getString("resourceType") ?: "Resource"
            SellerSetupScreen(navController, resourceType)
        }
    }
}