package com.bit45.movietrack.model.json

import com.squareup.moshi.Json

data class ProviderResponse(
    val results: Map<String, ProviderLists>?
)

data class ProviderLists(
    @Json(name = "link") val link: String,
    @Json(name = "flatrate") val streaming: List<Provider>?,
    @Json(name = "rent") val rent: List<Provider>?,
    @Json(name = "buy") val buy: List<Provider>?
)

data class Provider(
    @Json(name = "provider_id") val id: Int,
    @Json(name = "display_priority") val priority: Int,
    @Json(name = "provider_name") val name: String,
    @Json(name = "logo_path") val logo: String?,

    @Transient var purchaseType: Int = 0,
){
    companion object {
        const val TYPE_STREAM = 1
        const val TYPE_RENT = 2
        const val TYPE_BUY = 3
    }
}
