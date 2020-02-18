package mi.song.lmemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import mi.song.lmemo.R
import mi.song.lmemo.databinding.ActivityAddMemoBinding
import mi.song.lmemo.viewmodel.MemoViewModel

class AddMemoActivity : AppCompatActivity() {
    private lateinit var addMemoBinding:ActivityAddMemoBinding
    private var memoVM:MemoViewModel? = null
    private var id:Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_memo)

        //intent id 값을 받았는지 여부 확인
        intent.extras?.let {bundle->
            id = bundle.getLong("id")
        }

        init()
    }

    private fun init(){
        memoVM = MemoViewModel(application)
        setBtnEvents()
        setData()

    }

    private fun setData(){
        id?.let {identical ->
            memoVM?.getMemo(identical)?.observe(this, Observer { memo ->
                if (memo != null) {
                    addMemoBinding.addMemoTitle.setText(memo.title)
                    addMemoBinding.addMemoContents.setText(memo.contents)
                }
            })
        }
    }

    private fun setBtnEvents(){
        if(id != null)
            addMemoBinding.memoToolbarPhoto.visibility = View.GONE

        addMemoBinding.memoToolbarDelete.setOnClickListener { deleteData() }
        addMemoBinding.memoToolbarBack.setOnClickListener {  addData() }
    }

    //데이터 삭제
    private fun deleteData(){
        if(id != null) {
            memoVM?.deleteMemo(id!!)
        }
        finish()
    }

    //데이터 추가
    private fun addData(){
        val titleValue = addMemoBinding.addMemoTitle.text.toString()
        val contentsValue = addMemoBinding.addMemoContents.text.toString()


        if(!titleValue.equals("") && !contentsValue.equals("") && id == null){
            val contentsList = ArrayList<String>()
            contentsList.add(contentsValue)
            memoVM?.insertMemo(titleValue, contentsList)
            Toast.makeText(this, getText(R.string.memo_add_event), Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}
