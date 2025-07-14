package com.example.note

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("Api/Market/Gold_Currency.php?key=Freeb4kFJyIjKGneMaRtkAE457lV9iON")
    fun getData(): Call<DataClass>
}