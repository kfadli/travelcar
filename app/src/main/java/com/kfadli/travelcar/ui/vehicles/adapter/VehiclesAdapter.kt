package com.kfadli.travelcar.ui.vehicles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kfadli.core.models.Vehicle
import com.kfadli.travelcar.R


class VehiclesAdapter : RecyclerView.Adapter<VehiclesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    private var dataSet: MutableList<Vehicle> = mutableListOf()

    fun updateVehicles(dataSet: List<Vehicle>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val year: TextView
        val thumbnail: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.name)
            year = view.findViewById(R.id.year)
            thumbnail = view.findViewById(R.id.thumbnail)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_vehicle_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item = dataSet[position]

        viewHolder.name.text = "${item.brand} ${item.model}"
        viewHolder.year.text = item.year.toString()
        Glide.with(viewHolder.thumbnail.context)
            .load(item.thumbnail)
            .into(viewHolder.thumbnail)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
