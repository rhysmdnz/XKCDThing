package nz.memes.xkcdthing

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [XKCDComic::class], version = 1)
abstract class XKCDDatabase: RoomDatabase() {
    abstract fun xkcdDao(): XKCDDao
}