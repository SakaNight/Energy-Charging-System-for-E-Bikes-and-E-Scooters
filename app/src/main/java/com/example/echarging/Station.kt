data class Station(
    val station_id: String = "",
    val distance: String = "",
    val station_address: StationAddress = StationAddress(),
    val station_status: String = ""
)

data class StationAddress(
    val city: String = "",
    val street: String = "",
)

data class Slot(
    val slot_id: String = "",
    val station_id: String = "",
    val QR_code: String = "",
    val slot_status: String = "",
    val adapter_id: String = ""
)
