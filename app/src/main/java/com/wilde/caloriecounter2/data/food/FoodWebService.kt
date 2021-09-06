package com.wilde.caloriecounter2.data.food

import com.wilde.caloriecounter2.data.food.response.FoodResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodWebService {

    @GET("cgi/search.pl?action=process&json=true")
    suspend fun search(@Query("search_terms") searchTerm: String): FoodResponse
}