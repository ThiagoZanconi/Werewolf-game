package werewolf.model.repository.local

import android.provider.BaseColumns

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "profile"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_JESTER_WINS = "jester_wins"
        const val COLUMN_NAME_WEREWOLF_WINS = "werewolf_wins"
        const val COLUMN_NAME_VILLAGER_WINS = "villager_wins"
    }

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${FeedEntry.COLUMN_NAME_NAME} TEXT PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_JESTER_WINS} INTEGER," +
                "${FeedEntry.COLUMN_NAME_WEREWOLF_WINS} INTEGER," +
                "${FeedEntry.COLUMN_NAME_VILLAGER_WINS} INTEGER)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"
}