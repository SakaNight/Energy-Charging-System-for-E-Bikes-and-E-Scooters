import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.echarging.R

class StationAdapter(private val context: Context, private val dataSource:List<Station>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: inflater.inflate(R.layout.list_item_station, parent, false)

        val station = getItem(position) as Station

        val stationNameTextView = view.findViewById<TextView>(R.id.station_name)
        val stationIdTextView = view.findViewById<TextView>(R.id.station_id)
        val distanceTextView = view.findViewById<TextView>(R.id.distance)

        stationNameTextView.text = station.station_address.street
        stationIdTextView.text = station.station_address.city
        distanceTextView.text = station.distance

        return view
    }
}