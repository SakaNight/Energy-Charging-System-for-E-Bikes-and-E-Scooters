package com.example.echarging

data class Station(
    val distance: String = "",
    val slot_id: String = "",
    val station_address: StationAddress = StationAddress(),
    val station_status: String = ""
) {
    val station_id: String = ""
}

data class StationAddress(
    val city: String = "",
    val street: String = ""
)
