package com.example.productviewerapp.network

import com.example.productviewerapp.model.Product
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

}