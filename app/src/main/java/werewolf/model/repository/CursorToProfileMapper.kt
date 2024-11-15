package werewolf.model.repository

import android.database.Cursor
import werewolf.model.entities.Profile

interface CursorToProfileMapper{
    fun getProfile(cursor: Cursor): Profile
}

class CursorToProfileMapperImpl(
): CursorToProfileMapper{
    override fun getProfile(cursor: Cursor): Profile {
        return Profile(
            cursor.getString(0),
            cursor.getInt(1),
            cursor.getInt(2),
            cursor.getInt(3)
        )
    }
}