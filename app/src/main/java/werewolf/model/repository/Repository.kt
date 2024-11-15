package werewolf.model.repository

import android.content.ContentValues
import android.content.Context
import werewolf.model.entities.Profile
import werewolf.model.repository.local.FeedReaderContract.FeedEntry
import werewolf.model.repository.local.ProfileLocalStorageImpl

interface Repository{
    fun getProfiles(): List<Profile>
    fun getProfile(): Profile?
    fun createProfile(name: String)
    fun deleteProfile(name: String)
    fun addVillagerWin(name: String)
    fun addWerewolfWin(name: String)
    fun addJesterWin(name: String)
}

class RepositoryImpl(val context: Context):Repository{

    private val cursorToProfileMapper: CursorToProfileMapper = CursorToProfileMapperImpl()
    private val dbWerewolf = ProfileLocalStorageImpl(context)

    override fun getProfiles(): List<Profile> {
        val db = dbWerewolf.readableDatabase

        val projection = arrayOf(FeedEntry.COLUMN_NAME_NAME, FeedEntry.COLUMN_NAME_JESTER_WINS, FeedEntry.COLUMN_NAME_WEREWOLF_WINS,FeedEntry.COLUMN_NAME_VILLAGER_WINS)
        val selection = "*"
        val selectionArgs = emptyArray<String>()

        val cursor = db.query(
            FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val profiles = mutableListOf<Profile>()
        if (cursor.moveToFirst()) {
            do {
                profiles.add(cursorToProfileMapper.getProfile(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return profiles
    }

    override fun getProfile(): Profile? {
        TODO("Not yet implemented")
    }

    override fun createProfile(name: String) {
        // Gets the data repository in write mode
        val db = dbWerewolf.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(FeedEntry.COLUMN_NAME_NAME, name)
            put(FeedEntry.COLUMN_NAME_JESTER_WINS, 0)
            put(FeedEntry.COLUMN_NAME_WEREWOLF_WINS, 0)
            put(FeedEntry.COLUMN_NAME_VILLAGER_WINS, 0)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(FeedEntry.TABLE_NAME, null, values)

    }

    override fun deleteProfile(name: String) {
        TODO("Not yet implemented")
    }

    override fun addVillagerWin(name: String) {
        TODO("Not yet implemented")
    }

    override fun addWerewolfWin(name: String) {
        TODO("Not yet implemented")
    }

    override fun addJesterWin(name: String) {
        TODO("Not yet implemented")
    }
}