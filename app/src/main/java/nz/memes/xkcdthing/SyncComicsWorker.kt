package nz.memes.xkcdthing

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import nz.memes.xkcdthing.data.XKCDRepository

@HiltWorker
class SyncComicsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val xkcdRepository: XKCDRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        xkcdRepository.downloadAll()
        return Result.success()
    }
}
