package nz.memes.xkcdthing.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class XKCDComic(
    @PrimaryKey val num: Int,
    @ColumnInfo(name = "release_date") val releaseDate: LocalDate,
    @ColumnInfo(name = "img") val img: String,
    @ColumnInfo(name = "alt") val alt: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "safe_title") val safeTitle: String,
    @ColumnInfo(name = "transcript") val transcript: String
)
