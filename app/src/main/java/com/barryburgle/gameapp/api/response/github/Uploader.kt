package com.barryburgle.gameapp.api.response.github

data class Uploader(
    val gists_url: String = "",
    val repos_url: String = "",
    val user_view_type: String = "",
    val following_url: String = "",
    val starred_url: String = "",
    val login: String = "",
    val followers_url: String = "",
    val type: String = "",
    val url: String = "",
    val subscriptions_url: String = "",
    val received_events_url: String = "",
    val avatar_url: String = "",
    val events_url: String = "",
    val html_url: String = "",
    val site_admin: Boolean = false,
    val id: Int = 0,
    val gravatar_id: String = "",
    val node_id: String = "",
    val organizations_url: String = ""
)