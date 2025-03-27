package com.barryburgle.gameapp.api.response.github

data class AssetsItem(
    val created_at: String = "",
    val browser_download_url: String = "",
    val label: String = "",
    val url: String = "",
    val download_count: Int = 0,
    val content_type: String = "",
    val size: Int = 0,
    val updated_at: String = "",
    val uploader: Uploader,
    val name: String = "",
    val id: Int = 0,
    val state: String = "",
    val node_id: String = ""
)