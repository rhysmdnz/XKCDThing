package nz.memes.xkcdthing

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class XKCDResponse(
    val month: Int,
    val year: Int,
    val day: Int,
    val num: Int,
    val img: String,
    val alt: String,
    val title: String,
    @Json(name = "safe_title") val safeTitle: String
)
