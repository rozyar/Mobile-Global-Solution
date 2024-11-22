package com.example.solarpanelcalculatorapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.models.AnalysisResponse

class AnalysisAdapter(private val analyses: List<AnalysisResponse>) :
    RecyclerView.Adapter<AnalysisAdapter.AnalysisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_analysis, parent, false)
        return AnalysisViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnalysisViewHolder, position: Int) {
        val analysis = analyses[position]
        holder.bind(analysis)
    }

    override fun getItemCount(): Int = analyses.size

    inner class AnalysisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewTotalConsumption: TextView =
            itemView.findViewById(R.id.textViewTotalConsumption)
        private val textViewSunlightHours: TextView = itemView.findViewById(R.id.textViewSunlightHours)
        private val textViewResult: TextView = itemView.findViewById(R.id.textViewResult)
        private val applianceContainer: LinearLayout = itemView.findViewById(R.id.applianceContainer)

        fun bind(analysis: AnalysisResponse) {
            textViewDate.text = "Data: ${analysis.formattedDate}"
            textViewTotalConsumption.text = "Consumo Total: ${analysis.totalConsumption} kWh"
            textViewSunlightHours.text = "Horas de Luz Solar: ${analysis.sunlightHours}h"
            textViewResult.text = analysis.result

            applianceContainer.removeAllViews()
            for (appliance in analysis.appliances) {
                val applianceView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.item_appliance_summary, applianceContainer, false)

                val textViewApplianceName =
                    applianceView.findViewById<TextView>(R.id.textViewApplianceName)
                val textViewPowerConsumption =
                    applianceView.findViewById<TextView>(R.id.textViewPowerConsumption)

                textViewApplianceName.text = appliance.applianceName
                textViewPowerConsumption.text = "${appliance.powerConsumption} W"

                applianceContainer.addView(applianceView)
            }
        }
    }
}
