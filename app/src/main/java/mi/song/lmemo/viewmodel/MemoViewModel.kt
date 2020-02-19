package mi.song.lmemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import mi.song.lmemo.data.Memo
import mi.song.lmemo.event.ImgDeleteEvent
import mi.song.lmemo.repository.MemoRepository

class MemoViewModel(app:Application) : AndroidViewModel(app){
    val repository = MemoRepository(app.applicationContext)
    val memoList = repository.getMemoList()
    val imgListDeleteEvent = ImgDeleteEvent()

    fun insertMemo(memo:Memo){
        repository.insert(memo)
    }

    fun insertMemo(title:String, contentsList:ArrayList<String>, imgList:ArrayList<String>?){
        val imgVal = if(imgList.isNullOrEmpty()) "" else imgList.toString()
        val memo = Memo(0, title, contentsList.toString(), imgVal, System.currentTimeMillis())
        insertMemo(memo)
    }

    fun deleteMemo(memo:Memo){
        repository.delete(memo)
    }

    fun deleteMemo(id:Long){
        repository.delete(id)
    }

    fun updateMemo(memo:Memo){
        repository.update(memo)
    }

    fun updateMemo(id:Long, title:String, contentsList:ArrayList<String>, imgList:ArrayList<String>?){
        val imgVal = if(imgList.isNullOrEmpty()) "" else imgList.toString()
        val memo = Memo(id, title, contentsList.toString(), imgVal, System.currentTimeMillis())
        updateMemo(memo)
    }

    fun getMemo(id:Long) : LiveData<Memo>{
        return repository.getMemo(id)
    }

}