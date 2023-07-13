package net.techandgraphics.hymn.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {

  val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
      database.execSQL("ALTER TABLE Search ADD `lang` TEXT NOT NULL DEFAULT 'en'")
      database.execSQL("ALTER TABLE Other ADD `lang` TEXT NOT NULL DEFAULT 'en'")
    }
  }
}
