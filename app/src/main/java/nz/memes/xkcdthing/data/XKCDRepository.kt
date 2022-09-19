package nz.memes.xkcdthing.data

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import nz.memes.xkcdthing.XKCDService
import nz.memes.xkcdthing.data.local.XKCDDao
import nz.memes.xkcdthing.data.network.toXKCDComic
import javax.inject.Inject

class XKCDRepository @Inject constructor(
    private val xkcdDao: XKCDDao,
    private val xkcdService: XKCDService
) {
    private suspend fun updateComicNetwork(comicId: Int): XKCDComic {
        val comic = xkcdService.getComic(comicId = comicId)
        val xkcdComic = comic.toXKCDComic()
        xkcdDao.insertComics(xkcdComic)
        return xkcdComic
    }

    // TODO Return status info and failed states nicely
    suspend fun getComic(comicId: Int): XKCDComic {
        val comic = xkcdDao.getComic(comicId = comicId)
        if (comic != null) {
            return comic
        }
        return updateComicNetwork(comicId = comicId)
    }

    suspend fun getLatestComic(): XKCDComic {
        val comic = xkcdService.getLatestComic()
        val xkcdComic = comic.toXKCDComic()
        xkcdDao.insertComics(xkcdComic)
        return xkcdComic
    }

    suspend fun downloadAll() {
        val latestComic = getLatestComic()
        coroutineScope {
            val deferred: List<Deferred<XKCDComic>> =
                ((1..403) + (405..latestComic.num)).map { id -> async { getComic(id) } }
            deferred.awaitAll()
        }
    }
}
