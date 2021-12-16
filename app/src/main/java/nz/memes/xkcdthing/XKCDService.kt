package nz.memes.xkcdthing

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface XKCDService {

    @GET("{comicId}/info.0.json")
    suspend fun getComic(@Path("comicId") comicId: Int): XKCDResponse
}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://xkcd.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideXKCDService(retrofit: Retrofit): XKCDService =
        retrofit.create(XKCDService::class.java)
}
