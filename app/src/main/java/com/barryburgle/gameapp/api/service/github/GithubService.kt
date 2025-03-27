package com.barryburgle.gameapp.api.service.github

import com.barryburgle.gameapp.api.response.github.GithubLatestResponse
import retrofit2.Response
import retrofit2.http.GET

interface GithubService {

    @GET("/repos/barryburgle/daygame-app/releases/latest")
    suspend fun getLatestVersion(): Response<GithubLatestResponse>
}