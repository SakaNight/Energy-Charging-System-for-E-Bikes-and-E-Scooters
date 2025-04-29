package com.example.echarging

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echarging.ui.theme.EChargingTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.example.echarging.ui.theme.DarkBlue


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            val content = result.contents
            val parts = content.split(",")
            val slotId = parts.find { it.startsWith("slot_id:") }?.split(":")?.get(1)?.trim() ?: ""
            val qrCode = parts.find { it.startsWith("QR_code:") }?.split(":")?.get(1)?.trim() ?: ""

            if (slotId.isNotEmpty() && qrCode.isNotEmpty()) {
                viewModel.verifyQRCode(
                    qrCode = qrCode,
                    onSuccess = { slot ->
                        val intent = Intent(this, PaymentActivity::class.java).apply {
                            putExtra("slot_id", slotId)
                            putExtra("station_id", slot.station_id)
                            putExtra("qr_code", qrCode)
                            putExtra("slot_status", slot.slot_status)
                            putExtra("adapter_id", slot.adapter_id)
                        }
                        startActivity(intent)
                    },
                    onFailure = {
                        Toast.makeText(this, "QR code not found", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EChargingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel, ::startQRCodeScan)
                }
            }
        }
    }

    private fun startQRCodeScan() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a QR code")
        options.setCameraId(0)
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, onQRCodeScan: () -> Unit) {
    val context = LocalContext.current
    val stations by viewModel.stations.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "eCharging Stations",
                style = MaterialTheme.typography.titleLarge.copy(color = DarkBlue),
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = onQRCodeScan,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
            ) {
                Text("Scan QR Code", color = Color.White)
            }
        }

        TextField(
            value = viewModel.query,
            onValueChange = { viewModel.query = it },
            label = { Text("Search by city") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkBlue,
                unfocusedContainerColor = DarkBlue,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Button(
            onClick = { viewModel.searchStations() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Text("Search", color = Color.White)
        }

        LazyColumn {
            items(stations) { station ->
                StationItem(station = station, onClick = {
                    val intent = Intent(context, StationActivity::class.java).apply {
                        putExtra("station_id", station.station_id)
                    }
                    context.startActivity(intent)
                })
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.End),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, AccountActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#FFC107")))
            ) {
                Text("Account", color = Color.White)
            }
        }
    }
}

@Composable
fun StationItem(station: Station, onClick: () -> Unit) {
    val distance = station.distance
    val availableSlots = station.station_status == "available"
    val statusColor = if (availableSlots) Color.Green else Color.Red

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(DarkBlue)
            .clickable(onClick = onClick)
    ) {
        Text(text = station.station_address.street, style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
        Text(text = station.station_address.city, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
        Text(text = "Distance: $distance", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
        Text(
            text = if (availableSlots) "Available" else "Unavailable",
            color = statusColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EChargingTheme {
        MainScreen(viewModel = MainViewModel(), onQRCodeScan = {})
    }
}
