package net.techandgraphics.hymn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.prefs.AppPrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: AppPrefs
) : CategoryRepository {

  private val dao = database.categoryDao

  override suspend fun spotlight(): List<Category> {
    return runBlocking {
      dao.spotlight(prefs.getLang()).map { it.asModel() }
    }
  }

  override fun query(query: String): Flow<List<Category>> {
    return runBlocking {
      dao.query(query, prefs.getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override fun queryById(id: Int): Flow<List<Category>> {
    return runBlocking {
      dao.queryById(id, prefs.getLang()).map { it.map { data -> data.asModel() } }
    }
  }
}
