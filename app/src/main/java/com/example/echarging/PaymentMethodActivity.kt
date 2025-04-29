package com.example.echarging

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

class PaymentMethodActivity : ComponentActivity() {
    private val viewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EChargingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PaymentMethodScreen(viewModel)
                }
            }
        }

        val slotId = intent.getStringExtra("slot_id") ?: ""
        val stationId = intent.getStringExtra("station_id") ?: ""
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val duration = intent.getIntExtra("duration", 2)

        lifecycleScope.launch {
            viewModel.loadPaymentInfo(stationId, slotId, userId)
        }
    }
}

@Composable
fun PaymentMethodScreen(viewModel: PaymentViewModel) {
    val context = LocalContext.current
    val userBalance by viewModel.userBalance.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val cost = duration * 3 // 3 dollars per hr

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Please choose payment method", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (userBalance >= cost) {
                    viewModel.updateBalance(userBalance - cost)
                    viewModel.updateChargingStatus(true)  // 添加这行
                    Toast.makeText(context, "Start charging", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, AccountActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Insufficient balance, please top up", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Text("Balance")
        }
        Button(
            onClick = { Toast.makeText(context, "Google Pay", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Text("Google Pay")
        }
        Button(
            onClick = { Toast.makeText(context, "Credit Card", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Text("Credit Card")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodScreenPreview() {
    EChargingTheme {
        PaymentMethodScreen(viewModel = PaymentViewModel())
    }
}
