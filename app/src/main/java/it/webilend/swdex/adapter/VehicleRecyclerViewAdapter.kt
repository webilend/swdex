package it.webilend.swdex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import it.webilend.swdex.*
import it.webilend.swdex.model.Film
import it.webilend.swdex.model.Vehicle

class VehicleRecyclerViewAdapter(private val parentActivity: FragmentActivity,
                                 private val values: List<Vehicle>) :
    RecyclerView.Adapter<VehicleRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.txtVehicleView.text = item.name
        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtVehicleView: TextView = view.findViewById(R.id.txt_vehicle)
    }
}