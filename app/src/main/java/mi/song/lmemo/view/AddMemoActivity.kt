package mi.song.lmemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import mi.song.lmemo.R
import mi.song.lmemo.databinding.ActivityAddMemoBinding

class AddMemoActivity : AppCompatActivity() {
    lateinit var addMemoBinding:ActivityAddMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_memo)
        addMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_memo)
        init()
    }

    private fun init(){
        setBtnEvents()
    }

    private fun setBtnEvents(){
        addMemoBinding.memoToolbarDelete.setOnClickListener { finish() }
        addMemoBinding.memoToolbarBack.setOnClickListener {  addDataEvents() }
    }

    private fun addDataEvents(){
        Toast.makeText(this, getText(R.string.memo_add_event), Toast.LENGTH_SHORT).show()
        finish()
    }
}
