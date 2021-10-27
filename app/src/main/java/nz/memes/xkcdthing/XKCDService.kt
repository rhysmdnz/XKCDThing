package nz.memes.xkcdthing

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface XKCDService {

    @GET("{comicId}/info.0.json")
    suspend fun getComic(@Path("comicId") comicId: Int) : XKCDResponse

    companion object {
        var retrofitService: XKCDService? = null
        fun getInstance() : XKCDService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://xkcd.com/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(XKCDService::class.java)
            }
            return retrofitService!!
        }

    }
}