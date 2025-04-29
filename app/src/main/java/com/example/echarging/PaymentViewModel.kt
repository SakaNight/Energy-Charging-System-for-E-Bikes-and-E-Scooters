package com.example.echarging

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class PaymentViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    val stationId = MutableStateFlow("")
    val slotId = MutableStateFlow("")
    val userId = MutableStateFlow("")

    private val _stationAddress = MutableStateFlow("")
    val stationAddress: StateFlow<String> = _stationAddress

    private val _userBalance = MutableStateFlow(0.0)
    val userBalance: StateFlow<Double> = _userBalance

    private val _duration = MutableStateFlow(2)
    val duration: StateFlow<Int> = _duration

    suspend fun loadPaymentInfo(stationId: String, slotId: String, userId: String) {
        this.stationId.value = stationId
        this.slotId.value = slotId
        this.userId.value = userId

        try {
            val stationDocument = db.collection("Stations").document(stationId).get().await()
            val station = stationDocument.toObject(Station::class.java)
            val stationAddress = station?.station_address?.let { "${it.street}, ${it.city}" } ?: ""
            _stationAddress.value = stationAddress

            val userDocument = db.collection("Users").document(userId).get().await()
            val userBalance = userDocument.getDouble("balance") ?: 0.0
            _userBalance.value = userBalance
        } catch (e: Exception) {
        }
    }

    fun setDuration(duration: Int) {
        _duration.value = duration
    }

    fun updateBalance(newBalance: Double) {
        db.collection("Users").document(userId.value)
            .update("balance", newBalance)
            .addOnSuccessListener {
                _userBalance.value = newBalance
            }
            .addOnFailureListener { e ->
            }
    }

    fun updateChargingStatus(isCharging: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("Users").document(userId)
            .update("is_charging", isCharging)
    }

}

class AccountViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun stopCharging(userId: String, onComplete: (Boolean) -> Unit) {
        db.collection("Users").document(userId)
            .update("is_charging", false)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun checkBalanceAndStopChargingIfNeeded(userId: String, onComplete: (Boolean) -> Unit) {
        db.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val balance = document.getDouble("balance") ?: 0.0
                val isCharging = document.getBoolean("is_charging") ?: false
                if (isCharging && balance <= 0) {
                    stopCharging(userId, onComplete)
                } else {
                    onComplete(false)
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}