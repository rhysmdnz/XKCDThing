package nz.memes.xkcdthing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class XKCDViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val xkcdRepository: XKCDRepository,
) : ViewModel() {
    val comics: Flow<PagingData<XKCDResponse>> = Pager(PagingConfig(pageSize = 20, enablePlaceholders = true)) {
        XKCDComicPagingSource()
    }.flow.cachedIn(viewModelScope)
}

