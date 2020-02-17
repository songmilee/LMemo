package mi.song.lmemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import mi.song.lmemo.R
import mi.song.lmemo.data.Memo
import mi.song.lmemo.databinding.ActivityMainBinding
import mi.song.lmemo.viewmodel.MemoViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding:ActivityMainBinding
    private var adapter:MemoAdapter? = null
    private var memoVM:MemoViewModel? = null
    private var memoList:ArrayList<Memo> = ArrayList<Memo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //data binding
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init(){
        //set recyclerview adapter
        adapter = MemoAdapter(this)
        mainBinding.memoList.adapter = adapter
        mainBinding.memoList.layoutManager = LinearLayoutManager(this)

        memoList.add(Memo(0, "test", "2020-02-18", System.currentTimeMillis()))
        adapter?.setMemoList(memoList)

        if(adapter?.itemCount!! < 1){
            mainBinding.memoList.visibility = View.GONE
            mainBinding.memoEmpty.visibility = View.VISIBLE
        } else {
            mainBinding.memoList.visibility = View.VISIBLE
            mainBinding.memoEmpty.visibility = View.GONE
        }

        //set viewModel
        memoVM = MemoViewModel(application)
        memoVM?.memoList?.observe(this, Observer {memo ->
            Log.d("main", "$memo")
        })
        //set btn event
        mainBinding.memoAdd.setOnClickListener { addMemoActivity() }

    }

    //메모 추가를 위한 액티비티 호출
    private fun addMemoActivity(){
        val intent = Intent(this, AddMemoActivity::class.java)
        startActivity(intent)
    }
}
