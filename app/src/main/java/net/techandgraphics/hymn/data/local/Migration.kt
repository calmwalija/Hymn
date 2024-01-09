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

  val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {

      db.execSQL(
        """
        CREATE TABLE _lyric (
        lyricId INTEGER,
        categoryId INTEGER,
        categoryName TEXT,
        number INTEGER,
        chorus INTEGER,
        content TEXT,
        favorite INTEGER,
        title TEXT,
        lang TEXT,
        timestamp INTEGER,
        PRIMARY KEY (lyricId))
        """.trimIndent()
      )

      db.execSQL(
        """
        INSERT INTO _lyric
        SELECT
        lyricId,
        categoryId,
        categoryName,
        number,
        chorus,
        content,
        favorite,
        title,
        lang,
        timestamp
        FROM lyric
        """.trimIndent()
      )

      db.execSQL("DROP TABLE lyric")

      db.execSQL(
        """
        CREATE TABLE lyric (
        lyricId INTEGER NOT NULL,
        categoryId INTEGER NOT NULL,
        categoryName TEXT NOT NULL,
        number INTEGER NOT NULL,
        chorus INTEGER NOT NULL,
        content TEXT NOT NULL,
        favorite INTEGER NOT NULL,
        title TEXT NOT NULL,
        lang TEXT NOT NULL,
        timestamp INTEGER NOT NULL,
        PRIMARY KEY (lyricId))
        """.trimIndent()
      )

      db.execSQL(
        """
        INSERT INTO lyric
        SELECT
        lyricId,
        categoryId,
        categoryName,
        number,
        chorus,
        content,
        favorite,
        title,
        lang,
        timestamp
        FROM _lyric
        """.trimIndent()
      )

      db.execSQL("DROP TABLE _lyric")

      db.execSQL("CREATE TABLE timestamp (lang TEXT NOT NULL,timestamp INTEGER NOT NULL, number INTEGER NOT NULL, id INTEGER NOT NULL, PRIMARY KEY(id))")
      db.execSQL("CREATE TABLE time_spent (lang TEXT NOT NULL,timeSpent INTEGER NOT NULL, number INTEGER NOT NULL, id INTEGER NOT NULL, PRIMARY KEY(id))")
    }
  }
}
