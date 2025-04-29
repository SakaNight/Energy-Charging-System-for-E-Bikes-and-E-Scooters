package com.example.echarging

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.echarging.ui.theme.DarkBlue
import com.example.echarging.ui.theme.EChargingTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PaymentActivity : ComponentActivity() {
    private val viewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EChargingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PaymentScreen(viewModel)
                }
            }
        }

        val slotId = intent.getStringExtra("slot_id") ?: ""
        val stationId = intent.getStringExtra("station_id") ?: ""
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        lifecycleScope.launch {
            viewModel.loadPaymentInfo(stationId, slotId, userId)
        }
    }
}

@Composable
fun PaymentScreen(viewModel: PaymentViewModel) {
    val context = LocalContext.current
    val stationAddress by viewModel.stationAddress.collectAsState()
    val slotId by viewModel.slotId.collectAsState()
    val userBalance by viewModel.userBalance.collectAsState()
    val selectedDuration = remember { mutableStateOf(2) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Station Address: $stationAddress", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Slot ID: $slotId", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Balance: $userBalance", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Please chose charging duration", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            DurationButton(duration = 2, selectedDuration = selectedDuration)
            DurationButton(duration = 4, selectedDuration = selectedDuration)
            DurationButton(duration = 8, selectedDuration = selectedDuration)
            DurationButton(duration = 12, selectedDuration = selectedDuration)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val intent = Intent(context, PaymentMethodActivity::class.java).apply {
                    putExtra("slot_id", slotId)
                    putExtra("station_id", viewModel.stationId.value)
                    putExtra("user_id", viewModel.userId.value)
                    putExtra("duration", selectedDuration.value)
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)

        ) {
            Text("GO!")
        }
    }
}

@Composable
fun DurationButton(duration: Int, selectedDuration: MutableState<Int>) {
    Button(
        onClick = { selectedDuration.value = duration },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedDuration.value == duration) DarkBlue else MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        Text("$duration Hrs")
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    EChargingTheme {
        PaymentScreen(viewModel = PaymentViewModel())
    }
}
