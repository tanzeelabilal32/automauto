package com.sema.data.api

import com.sema.data.model.CarSearchItem
import retrofit2.http.*

interface ApiService {

    @GET("/")
    suspend fun getCars(): List<CarSearchItem>
}
