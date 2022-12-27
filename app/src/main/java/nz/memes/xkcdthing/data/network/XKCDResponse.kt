package nz.memes.xkcdthing.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import nz.memes.xkcdthing.data.XKCDComic

@JsonClass(generateAdapter = true)
data class XKCDResponse(
    val month: Int,
    val year: Int,
    val day: Int,
    val num: Int,
    val img: String,
    val alt: String,
    val title: String,
    @Json(name = "safe_title") val safeTitle: String,
    val transcript: String
)

fun XKCDResponse.toXKCDComic(): XKCDComic {
    val date = LocalDate.of(year, month, day)
    return XKCDComic(
        num = num,
        releaseDate = date,
        title = title,
        safeTitle = safeTitle,
        alt = alt,
        img = img,
        transcript = transcript
    )
}
