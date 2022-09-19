package nz.memes.xkcdthing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import nz.memes.xkcdthing.data.XKCDComic
import nz.memes.xkcdthing.data.XKCDRepository
import javax.inject.Inject

@HiltViewModel
class XKCDViewModel @Inject constructor(
    private val xkcdRepository: XKCDRepository
) : ViewModel() {
    val comics: Flow<PagingData<XKCDComic>> =
        Pager(PagingConfig(pageSize = 20, enablePlaceholders = true)) {
            XKCDComicPagingSource(xkcdRepository)
        }.flow.cachedIn(viewModelScope)

    fun getLatestComicId() = liveData {
        emit(xkcdRepository.getLatestComic().num)
    }
}
