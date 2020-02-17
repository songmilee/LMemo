package mi.song.lmemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mi.song.lmemo.data.dao.MemoDAO
import mi.song.lmemo.data.Memo

@Database(entities = [Memo::class], version = 1, exportSchema = false)
abstract class MemoDB : RoomDatabase(){
    abstract fun MemoDao():MemoDAO

    companion object{
        private var INSTANCE:MemoDB? = null

        @Synchronized
        fun getInstance(context:Context) : MemoDB{

            if(INSTANCE == null) {
                val dbName = "${context.packageName}.db"
                INSTANCE =
                    Room.databaseBuilder(context.applicationContext, MemoDB::class.java, dbName)
                        .build()
            }

            return INSTANCE!!
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}