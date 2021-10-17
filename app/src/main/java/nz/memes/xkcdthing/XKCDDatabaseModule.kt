package nz.memes.xkcdthing

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class XKCDDatabaseModule {
    @Provides
    fun provideXKCDComicDao(xkcdDatabase: XKCDDatabase): XKCDDao {
        return xkcdDatabase.xkcdDao()
    }

    @Provides
    @Singleton
    fun provideXKCDDatabase(@ApplicationContext appContext: Context): XKCDDatabase {
        return Room.databaseBuilder(
            appContext,
            XKCDDatabase::class.java,
            "xkcd-database"
        ).build()
    }
}