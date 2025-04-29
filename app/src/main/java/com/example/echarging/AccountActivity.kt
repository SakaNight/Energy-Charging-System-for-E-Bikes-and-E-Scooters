package com.example.echarging

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.echarging.ui.theme.EChargingTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.activity.viewModels
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.echarging.ui.theme.DarkBlue

class AccountActivity : ComponentActivity() {
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EChargingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AccountScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun AccountScreen(viewModel: AccountViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf(0.0) }
    var isCharging by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection("Users").document(userId)
        userDocument.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    userName = document.getString("user_name") ?: "N/A"
                    userEmail = document.getString("user_email") ?: "N/A"
                    balance = document.getDouble("balance") ?: 0.0
                    isCharging = document.getBoolean("is_charging") ?: false

                    // 检查余额并在需要时停止充电
                    viewModel.checkBalanceAndStopChargingIfNeeded(userId) { stopped ->
                        if (stopped) {
                            isCharging = false
                            Toast.makeText(context, "Charging stopped due to insufficient balance", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    errorMessage = "User not found"
                }
            }
            .addOnFailureListener { exception ->
                errorMessage = "Error getting user data: $exception"
            }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(text = "User Name: $userName", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "User Email: $userEmail", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Balance: $balance", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Charging Status: ${if (isCharging) "Charging" else "Not Charging"}", style = MaterialTheme.typography.bodyLarge)

            if (isCharging) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.stopCharging(userId) { success ->
                            if (success) {
                                isCharging = false
                                Toast.makeText(context, "Charging stopped", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to stop charging", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
                    ) {
                    Text("Stop Charging")
                }
            }
        }

        if (isCharging) {
            Spacer(modifier = Modifier.height(32.dp))
            ChargingAnimation()
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun AccountScreenPreview() {
//    EChargingTheme {
//        AccountScreen()
//    }
//}
