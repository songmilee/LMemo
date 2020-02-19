package mi.song.lmemo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import mi.song.lmemo.data.Memo
import mi.song.lmemo.data.MemoList

@Dao
interface MemoDAO {
    @Insert
    fun insert(memo:Memo) : Long?

    @Update
    fun update(memo:Memo)

    @Delete
    fun delete(memo:Memo)

    @Query("select * from Memo")
    fun getMemoList() : LiveData<List<Memo>>

    @Query("select * from Memo where id=:id")
    fun getMemo(id:Long) : LiveData<Memo>

    @Query("delete from Memo where id=:id")
    fun deleteMemo(id:Long)
}