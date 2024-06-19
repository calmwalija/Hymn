package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Other
import net.techandgraphics.hymn.domain.repository.OtherRepository
import javax.inject.Inject

class OtherRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: DataStorePrefs
) : OtherRepository {

  private val dao = database.otherDao

  private suspend fun getLang() = prefs.get(prefs.translationKey, Lang.EN.lowercase())

  override suspend fun upsert(data: List<Other>) {
    dao.upsert(data.map { it.asEntity() })
  }

  override suspend fun query(): List<Other> {
    return dao.query(getLang()).map { it.asModel() }
  }

  override suspend fun clearAll() {
    dao.clearAll()
  }
}
