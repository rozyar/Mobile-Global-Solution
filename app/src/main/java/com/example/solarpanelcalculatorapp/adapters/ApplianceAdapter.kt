package com.example.solarpanelcalculatorapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.models.Appliance

class ApplianceAdapter(
    private val appliances: MutableList<Appliance>,
    private val onEditClick: (Appliance) -> Unit,
    private val onDeleteClick: (Appliance) -> Unit
) : RecyclerView.Adapter<ApplianceAdapter.ApplianceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplianceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appliance, parent, false)
        return ApplianceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplianceViewHolder, position: Int) {
        val appliance = appliances[position]
        holder.bind(appliance)
    }

    override fun getItemCount(): Int = appliances.size

    fun updateList(newAppliances: List<Appliance>) {
        val diffCallback = ApplianceDiffCallback(appliances, newAppliances)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        appliances.clear()
        appliances.addAll(newAppliances)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ApplianceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewApplianceName)
        private val textViewPower: TextView = itemView.findViewById(R.id.textViewPowerConsumption)
        private val buttonEdit: ImageButton = itemView.findViewById(R.id.buttonEdit)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(appliance: Appliance) {
            textViewName.text = appliance.applianceName
            textViewPower.text = "${appliance.powerConsumption} W"

            buttonEdit.setOnClickListener {
                onEditClick(appliance)
            }

            buttonDelete.setOnClickListener {
                onDeleteClick(appliance)
            }
        }
    }

    class ApplianceDiffCallback(
        private val oldList: List<Appliance>,
        private val newList: List<Appliance>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}
