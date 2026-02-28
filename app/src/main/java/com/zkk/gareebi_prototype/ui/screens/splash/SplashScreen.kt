package com.zkk.gareebi_prototype.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
// import com.zkk.gareebi_prototype.R // Uncomment this when you add your infographic to the res/drawable folder

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    // LaunchedEffect runs a coroutine tied to this composable's lifecycle.
    // key1 = true means it only fires once when the screen loads.
    LaunchedEffect(key1 = true) {
        delay(2000L) // Wait for exactly 2 seconds
        onSplashComplete() // Tell the NavController to move to Home
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Drop your infographic into app/src/main/res/drawable as an image file (e.g., 'splash_infographic.png' or '.jpg')
        // Then uncomment the Image block below:

        /*
        Image(
            painter = painterResource(id = R.drawable.splash_infographic),
            contentDescription = "P2P Energy Market Infographic",
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        )
        */

        // Fallback text just in case the image isn't loaded yet
        Text(text = "GAREEBI Energy Market", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Loading P2P Network...", style = MaterialTheme.typography.bodyMedium)
    }
}