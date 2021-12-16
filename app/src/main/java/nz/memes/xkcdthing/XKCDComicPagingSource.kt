package nz.memes.xkcdthing

import androidx.paging.PagingSource
import androidx.paging.PagingState
import timber.log.Timber

class XKCDComicPagingSource(private val xkcdService: XKCDService) :
    PagingSource<Int, XKCDResponse>() {

    override val jumpingSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<Int, XKCDResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, XKCDResponse> {
        try {
            Timber.v("We do a load")
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val responseObj = xkcdService.getComic(nextPageNumber)

            Timber.v("Yay load done!")

            return LoadResult.Page(
                data = listOf(responseObj),
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1, // Only paging forward.
                nextKey = nextPageNumber + 1,
                itemsBefore = 0,
                itemsAfter = 2000
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
