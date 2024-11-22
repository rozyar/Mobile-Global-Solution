package com.example.solarpanelcalculatorapp.models

data class AnalysisResponse(
    val id: Long,
    val createdAt: String,
    val formattedDate: String,
    val totalConsumption: Double,
    val sunlightHours: Int,
    val appliances: List<Appliance>,
    val result: String
)
