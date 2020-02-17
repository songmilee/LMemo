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

    @Query("select id, title, created_at from Memo")
    fun getMemoList() : LiveData<List<MemoList>>

    @Query("select * from Memo where id=:id")
    fun getMemo(id:Long) : LiveData<Memo>
}