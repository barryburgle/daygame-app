package com.barryburgle.gameapp.api

import com.barryburgle.gameapp.api.service.github.GithubService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val githubApiService: GithubService by lazy {
        Retrofit.Builder().baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(GithubService::class.java)
    }
}