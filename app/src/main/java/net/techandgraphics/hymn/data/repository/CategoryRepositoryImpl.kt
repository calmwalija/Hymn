package net.techandgraphics.hymn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: DataStorePrefs
) : CategoryRepository {

  private val dao = database.categoryDao

  private suspend fun getLang() = prefs.get(prefs.translationKey, Translation.EN.lowercase())

  override suspend fun spotlight(): List<Category> {
    return runBlocking {
      dao.spotlight(getLang()).map { it.asModel() }
    }
  }

  override fun query(query: String): List<Category> {
    return runBlocking {
      dao.query(query, getLang()).map { it.asModel() }
    }
  }

  override fun queryById(id: Int): Flow<List<Category>> {
    return runBlocking {
      dao.queryById(id, getLang()).map { it.map { data -> data.asModel() } }
    }
  }
}
