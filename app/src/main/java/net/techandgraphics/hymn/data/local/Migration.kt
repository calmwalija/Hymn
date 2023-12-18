package net.techandgraphics.hymn.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {

  val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
      db.execSQL("ALTER TABLE Search ADD `lang` TEXT NOT NULL DEFAULT 'en'")
      db.execSQL("ALTER TABLE Other ADD `lang` TEXT NOT NULL DEFAULT 'en'")
    }
  }

  val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
      db.execSQL("ALTER TABLE lyric ADD `ftsSuggestion` INTEGER NOT NULL DEFAULT 0")
      db.execSQL("ALTER TABLE lyric ADD `forTheService` INTEGER NOT NULL DEFAULT 0")
      db.execSQL("ALTER TABLE lyric ADD `justAdded` INTEGER NOT NULL DEFAULT 0")
      db.execSQL("ALTER TABLE lyric ADD `millsAdded` INTEGER NOT NULL DEFAULT 0")
    }
  }
}
