package nz.memes.xkcdthing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class XKCDViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val xkcdRepository: XKCDRepository,
) : ViewModel() {
    var currentImageUrl by mutableStateOf("https://imgs.xkcd.com/comics/mullers_ratchet_2x.png")
    var nextId by mutableStateOf(0)
    var prevId by mutableStateOf(0)
    var comicId by mutableStateOf(0)

    fun changeComic(newComicId: Int) {
        currentImageUrl = ""
        nextId = comicId + 1
        prevId = comicId - 1
        comicId = newComicId
        val url =
            if (comicId == 0) "https://xkcd.com/info.0.json" else "https://xkcd.com/${comicId}/info.0.json"
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(url)
                .build()
            val response: Response = client.newCall(request).execute()
            val moshi: Moshi = Moshi.Builder().build()
            val xkcdAdapter = moshi.adapter(XKCDResponse::class.java)
            val respString = response.body?.string()
            val responseObj = xkcdAdapter.fromJson(respString)
            currentImageUrl = responseObj?.img ?: ""
            nextId = (responseObj?.num ?: 0) + 1
            prevId = (responseObj?.num ?: 0) - 1
            comicId = responseObj?.num ?: 0
        }
    }
}

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