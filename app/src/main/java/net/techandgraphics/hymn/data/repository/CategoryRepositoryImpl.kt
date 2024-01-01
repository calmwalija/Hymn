package net.techandgraphics.hymn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.prefs.SharedPrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(database: Database, prefs: SharedPrefs) :
  CategoryRepository {

  private val dao = database.categoryDao
  private val lang = prefs.lang

  override suspend fun spotlight(): List<Category> {
    return dao.spotlight(lang).map { it.asModel() }
  }

  override fun query(): Flow<List<Category>> {
    return dao.query(lang).map { it.map { it.asModel() } }
  }

  override fun queryById(id: Int): Flow<List<Category>> {
    return dao.queryById(id, lang).map { it.map { it.asModel() } }
  }
}
