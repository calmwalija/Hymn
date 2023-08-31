package net.techandgraphics.hymn.worker

import androidx.work.WorkerFactory
import net.techandgraphics.hymn.data.repository.Repository
import javax.inject.Inject

class HymnWorkerFactory @Inject constructor(
  private val repository: Repository
) : WorkerFactory() {
  override fun createWorker(
    appContext: android.content.Context,
    workerClassName: String,
    workerParameters: androidx.work.WorkerParameters
  ) = HymnWorker(appContext, workerParameters, repository)
}
