package com.example.solarpanelcalculatorapp.api

import com.example.solarpanelcalculatorapp.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/users/authenticate")
    suspend fun authenticate(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>

    @GET("api/appliances")
    suspend fun getAppliances(@Header("Authorization") token: String): Response<List<Appliance>>

    @POST("api/appliances")
    suspend fun addAppliance(
        @Header("Authorization") token: String,
        @Body appliance: ApplianceRequest
    ): Response<Void>

    @PUT("api/appliances/{id}")
    suspend fun updateAppliance(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body appliance: ApplianceRequest
    ): Response<Void>

    @DELETE("api/appliances/{id}")
    suspend fun deleteAppliance(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Void>

    @POST("api/analyses")
    suspend fun createAnalysis(
        @Header("Authorization") token: String,
        @Body analysisRequest: AnalysisRequest
    ): Response<AnalysisResponse>

    @GET("api/analyses")
    suspend fun getAnalyses(@Header("Authorization") token: String): Response<List<AnalysisResponse>>
}
