package nz.memes.xkcdthing

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class XKCDComic(
    @PrimaryKey val num: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "img") val img: String,
    @ColumnInfo(name = "alt") val alt: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "safe_title") val safeTitle: String
)
