package mi.song.lmemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import mi.song.lmemo.R
import mi.song.lmemo.data.Memo
import mi.song.lmemo.databinding.ActivityAddMemoBinding
import mi.song.lmemo.viewmodel.MemoViewModel

class AddMemoActivity : AppCompatActivity() {
    private lateinit var addMemoBinding:ActivityAddMemoBinding
    private var memoVM:MemoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_memo)
        addMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_memo)
        init()
    }

    private fun init(){
        memoVM = MemoViewModel(application)
        setBtnEvents()
    }

    private fun setBtnEvents(){
        addMemoBinding.memoToolbarDelete.setOnClickListener { finish() }
        addMemoBinding.memoToolbarBack.setOnClickListener {  addDataEvents() }
    }

    private fun addDataEvents(){
        val titleValue = addMemoBinding.addMemoTitle.text.toString()
        val contentsValue = addMemoBinding.addMemoContents.text.toString()

        if(!titleValue.equals("") && !contentsValue.equals("")){
            val contentsList = ArrayList<String>()
            contentsList.add(contentsValue)
            memoVM?.insertMemo(titleValue, contentsList)
            Toast.makeText(this, getText(R.string.memo_add_event), Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}
