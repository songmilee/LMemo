package mi.song.lmemo.repository

import android.content.Context
import androidx.lifecycle.LiveData
import mi.song.lmemo.data.Memo
import mi.song.lmemo.data.MemoList
import mi.song.lmemo.db.MemoDB
import mi.song.lmemo.util.IOUtils

class MemoRepository(val context: Context) {
    val dao = MemoDB.getInstance(context).MemoDao()

    fun getMemoList() : LiveData<List<Memo>>{
        return dao.getMemoList()
    }

    fun getMemo(id:Long):LiveData<Memo>{
        return dao.getMemo(id)
    }

    fun insert(memo:Memo) = IOUtils().ioThread {
        dao.insert(memo)
    }

    fun delete(memo:Memo) = IOUtils().ioThread {
        dao.delete(memo)
    }

    fun delete(id:Long) = IOUtils().ioThread {
        dao.deleteMemo(id)
    }

    fun update(memo:Memo) = IOUtils().ioThread {
        dao.update(memo)
    }
}