package nz.memes.xkcdthing.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nz.memes.xkcdthing.data.XKCDComic

@Database(entities = [XKCDComic::class], version = 1)
@TypeConverters(Converters::class)
abstract class XKCDDatabase : RoomDatabase() {
    abstract fun xkcdDao(): XKCDDao
}
