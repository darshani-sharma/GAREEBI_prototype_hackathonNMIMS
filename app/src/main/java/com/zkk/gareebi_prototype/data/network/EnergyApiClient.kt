package com.zkk.gareebi_prototype.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class EnergyApiClient {

    // A standard OkHttpClient with reasonable timeouts
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    // TODO: CHANGE THIS TO YOUR LAPTOP'S ACTUAL IPv4 ADDRESS ON THE HOTSPOT
    private val pythonServerUrl = "http://192.168.1.5:5000/api/purchase"

    /**
     * Sends the purchase command to the Python server.
     * We use a suspend function so it runs in the background and doesn't freeze the UI.
     */
    suspend fun executeP2PTrade(unitsToBuy: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Build the JSON payload
            val json = JSONObject().apply {
                put("action", "p2p_trade")
                put("units", unitsToBuy)
            }

            val body = json.toString().toRequestBody("application/json".toMediaType())

            // 2. Build the HTTP POST Request
            val request = Request.Builder()
                .url(pythonServerUrl)
                .post(body)
                .build()

            // 3. Execute the request
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                println("Grid Response: ${response.body?.string()}")
                return@withContext true
            } else {
                println("Grid Error: ${response.code}")
                return@withContext false
            }
        } catch (e: IOException) {
            println("Failed to connect to microgrid: ${e.message}")
            return@withContext false
        }
    }
}