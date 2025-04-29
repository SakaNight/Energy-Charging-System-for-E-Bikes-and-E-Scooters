package com.example.echarging

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _stations = MutableStateFlow<List<Station>>(emptyList())
    val stations: StateFlow<List<Station>> = _stations
    var query by mutableStateOf("")

    init {
        loadStations()
    }

    fun loadStations() {
        viewModelScope.launch {
            db.collection("Stations")
                .get()
                .addOnSuccessListener { result ->
                    val stationList = result.mapNotNull { document ->
                        document.toObject(Station::class.java)
                    }
                    _stations.value = stationList
                }
                .addOnFailureListener { exception ->
                    println("Error loading stations: ${exception.message}")
                }
        }
    }

    fun searchStations() {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadStations()
            } else {
                db.collection("Stations")
                    .whereEqualTo("station_address.city", query.trim())
                    .get()
                    .addOnSuccessListener { result ->
                        val stationList = result.mapNotNull { document ->
                            document.toObject(Station::class.java)
                        }
                        _stations.value = stationList
                        println("Search results: ${stationList.size} stations found")
                    }
                    .addOnFailureListener { exception ->
                        println("Error searching stations: ${exception.message}")
                    }
            }
        }
    }

    fun verifyQRCode(qrCode: String, onSuccess: (Slot) -> Unit, onFailure: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        val pattern = Regex("slot_id:(\\w+),QR_code:(\\w+)")
        val matchResult = pattern.find(qrCode)

        db.collection("Slots")
            .whereEqualTo("QR_code", qrCode)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onFailure()
                } else {
                    for (document in documents) {
                        val slot = document.toObject(Slot::class.java)
                        onSuccess(slot)
                        break
                    }
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }
}
