package nz.memes.xkcdthing

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

class XKCDComicPagingSource(): PagingSource<Int, XKCDResponse>() {

    private val TAG = "ComicPagingSource"

    override val jumpingSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<Int, XKCDResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, XKCDResponse> {
        try {
            Log.v(TAG, "We do a load");
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val responseObj = XKCDService.getInstance().getComic(nextPageNumber)

            Log.v(TAG, "Yay load done!");

            return LoadResult.Page(
                data = listOf(responseObj),
                prevKey = if (nextPageNumber == 1) null else nextPageNumber-1, // Only paging forward.
                nextKey = nextPageNumber+1,
                itemsBefore = 0,
                itemsAfter = 2000
            )
        } catch (e: Exception) {
            Log.v(TAG, "Why we error :(")
            Log.e(TAG, "the error", e)
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            return LoadResult.Error(e)
        }
    }
}