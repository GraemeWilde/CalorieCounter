package com.wilde.caloriecounter2.data.food

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodWebService {

    @GET("cgi/search.pl?action=process&json=true")
    suspend fun search(@Query("search_terms") searchTerm: String): FoodResponse
}