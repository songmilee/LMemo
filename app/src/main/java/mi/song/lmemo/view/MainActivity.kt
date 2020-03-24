package mi.song.lmemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout.VERTICAL
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import mi.song.lmemo.R
import mi.song.lmemo.data.Memo
import mi.song.lmemo.databinding.ActivityMainBinding
import mi.song.lmemo.util.PermissionUtils
import mi.song.lmemo.viewmodel.MemoViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding:ActivityMainBinding
    private var adapter:MemoAdapter? = null
    private val memoVM:MemoViewModel by inject()
    private var memoList:ArrayList<Memo> = ArrayList<Memo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //data binding
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        PermissionUtils(this).checkPermission()
        init()
    }

    private fun init(){
        //set recyclerview adapter
        adapter = MemoAdapter(this)
        mainBinding.memoList.adapter = adapter

        //reverse layout
        val layoutManger = StaggeredGridLayoutManager(2, VERTICAL)
        mainBinding.memoList.layoutManager = layoutManger //LinearLayoutManager(this)

        //set viewModel
        val memoVM : MemoViewModel = get()
        memoVM?.memoList?.observe(this, Observer {memo ->
            showMemoList(memo.size)
            adapter?.setMemoList(memo!!)
        })

        //set btn event
        mainBinding.memoAdd.setOnClickListener { addMemoActivity() }

    }

    private fun showMemoList(size:Int){
        if(size < 1){
            mainBinding.memoList.visibility = View.GONE
            mainBinding.memoEmpty.visibility = View.VISIBLE
        } else {
            mainBinding.memoList.visibility = View.VISIBLE
            mainBinding.memoEmpty.visibility = View.GONE
        }
    }

    //메모 추가를 위한 액티비티 호출
    private fun addMemoActivity(){
        val intent = Intent(this, MemoDetailActivity::class.java)
        startActivity(intent)
    }
}
