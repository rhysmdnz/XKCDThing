package nz.memes.xkcdthing

import androidx.room.Dao
import androidx.room.Query

@Dao
interface XKCDDao {
    @Query("SELECT * FROM xkcdcomic")
    fun getAll(): List<XKCDComic>

    @Query("SELECT * FROM xkcdcomic WHERE num = :comicId")
    fun getById(comicId: Int): XKCDComic
}