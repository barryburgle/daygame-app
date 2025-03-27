package com.barryburgle.gameapp.api.response.github

data class GithubLatestResponse(
    val author: Author,
    val tag_name: String = "",
    val created_at: String = "",
    val body: String = "",
    val url: String = "",
    val assets_url: String = "",
    val assets: List<AssetsItem>?,
    val prerelease: Boolean = false,
    val html_url: String = "",
    val target_commitish: String = "",
    val draft: Boolean = false,
    val zipball_url: String = "",
    val name: String = "",
    val upload_url: String = "",
    val id: Int = 0,
    val published_at: String = "",
    val tarball_url: String = "",
    val node_id: String = ""
)