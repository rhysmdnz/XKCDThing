package nz.memes.xkcdthing.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import nz.memes.xkcdthing.data.XKCDComic
import nz.memes.xkcdthing.data.XKCDRepository
import timber.log.Timber

class XKCDComicPagingSource(private val xkcdRepository: XKCDRepository) :
    PagingSource<Int, XKCDComic>() {

    override val jumpingSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<Int, XKCDComic>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, XKCDComic> {
        try {
            Timber.v("We do a load")
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val responseObj = xkcdRepository.getComic(nextPageNumber)

            Timber.v("Yay load done!")

            return LoadResult.Page(
                data = listOf(responseObj),
                // Only paging forward.
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = nextPageNumber + 1,
                itemsBefore = 0,
                itemsAfter = 3000
            )
        } catch (e: Exception) {
            Timber.v("Why we error :(")
            Timber.e(e, "the error")
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            return LoadResult.Error(e)
        }
    }
}
