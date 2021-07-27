package com.kfadli.travelcar.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kfadli.core.network.responses.VehicleResponse
import com.kfadli.travelcar.R

class VehiclesAdapter : RecyclerView.Adapter<VehiclesAdapter.ViewHolder>() {

  /**
   * Provide a reference to the type of views that you are using
   * (custom ViewHolder).
   */

  private var dataSet: MutableList<VehicleResponse> = mutableListOf()

  fun updateVehicles(dataSet: List<VehicleResponse>) {
    this.dataSet.clear()
    this.dataSet.addAll(dataSet)
    notifyDataSetChanged()
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView

    init {
      // Define click listener for the ViewHolder's View.
      textView = view.findViewById(R.id.textView)
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
    viewHolder.textView.text = dataSet[position].model
  }

  // Return the size of your dataset (invoked by the layout manager)
  override fun getItemCount() = dataSet.size

}
