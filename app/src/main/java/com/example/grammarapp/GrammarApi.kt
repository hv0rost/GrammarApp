package com.example.grammarapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GrammarApi {
    @POST("get-text")
    fun createText(@Body post: PostText) : Call<PostText>

    @GET("get-test")
    fun getTest() : Call<List<Test>>
}