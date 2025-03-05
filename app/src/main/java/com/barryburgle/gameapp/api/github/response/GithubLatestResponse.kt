package com.barryburgle.gameapp.api.github.response

data class GithubLatestResponse(
    val assets: List<AssetsItem>?,
    val tag_name: String = "",
    val html_url: String = "",
    val published_at: String = "",
    val body: String? = ""
)