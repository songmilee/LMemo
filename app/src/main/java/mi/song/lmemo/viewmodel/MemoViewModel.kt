package mi.song.lmemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import mi.song.lmemo.data.Memo
import mi.song.lmemo.repository.MemoRepository

class MemoViewModel(app:Application) : AndroidViewModel(app){
    val repository = MemoRepository(app.applicationContext)
    val memoList = repository.getMemoList()

    fun insertMemo(memo:Memo){
        repository.insert(memo)
    }

    fun insertMemo(title:String, contentsList:ArrayList<String>){
        val memo = Memo(0, title, contentsList.toString(), System.currentTimeMillis())
        insertMemo(memo)
    }

    fun deleteMemo(memo:Memo){
        repository.delete(memo)
    }

    fun updateMemo(memo:Memo){
        repository.update(memo)
    }

    fun getMemo(id:Long) : LiveData<Memo>{
        return repository.getMemo(id)
    }

}