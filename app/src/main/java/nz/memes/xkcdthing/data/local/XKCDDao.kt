package nz.memes.xkcdthing.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nz.memes.xkcdthing.data.XKCDComic

@Dao
interface XKCDDao {
    @Query("SELECT * FROM xkcdcomic")
    suspend fun getAll(): List<XKCDComic>

    @Query("SELECT * FROM xkcdcomic WHERE num = :comicId")
    suspend fun getComic(comicId: Int): XKCDComic?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComics(vararg users: XKCDComic)
}
