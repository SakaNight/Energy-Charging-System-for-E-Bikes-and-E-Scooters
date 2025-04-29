package com.example.echarging

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echarging.ui.theme.DarkBlue
import com.example.echarging.ui.theme.EChargingTheme

class StationActivity : ComponentActivity() {
    private val viewModel: StationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EChargingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val stationId = intent.getStringExtra("station_id") ?: ""
                    StationScreen(viewModel, stationId)
                }
            }
        }
    }
}

@Composable
fun StationScreen(viewModel: StationViewModel, stationId: String) {
    val context = LocalContext.current
    val slots by viewModel.slots.collectAsState(initial = emptyList())

    LaunchedEffect(stationId) {
        viewModel.loadSlots(stationId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Slots for Station $stationId",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(slots) { slot ->
                SlotItem(slot = slot, onClick = {
                    val intent = Intent(context, PaymentActivity::class.java).apply {
                        putExtra("slot_id", slot.slot_id)
                        putExtra("station_id", slot.station_id)
                        putExtra("qr_code", slot.QR_code)
                        putExtra("slot_status", slot.slot_status)
                        putExtra("adapter_id", slot.adapter_id)
                    }
                    context.startActivity(intent)
                })
            }
        }
    }
}

@Composable
fun SlotItem(slot: Slot, onClick: () -> Unit) {
    val statusColor = if (slot.slot_status == "available") androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(DarkBlue)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = "Slot ID: ${slot.slot_id}", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
        Text(text = "Status: ${slot.slot_status}", color = statusColor, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun StationScreenPreview() {
    EChargingTheme {
        StationScreen(viewModel = StationViewModel(), stationId = "station_001")
    }
}
