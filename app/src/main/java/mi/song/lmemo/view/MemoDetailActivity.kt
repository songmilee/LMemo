package mi.song.lmemo.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import mi.song.lmemo.R
import mi.song.lmemo.databinding.ActivityDetailMemoBinding
import mi.song.lmemo.util.FileUtils
import mi.song.lmemo.util.GlobalVariable
import mi.song.lmemo.viewmodel.MemoViewModel
import java.io.*
import java.lang.Exception
import kotlin.collections.ArrayList

class MemoDetailActivity : AppCompatActivity() {
    private lateinit var addMemoBinding:ActivityDetailMemoBinding
    private var memoVM:MemoViewModel? = null
    private var id:Long? = null
    private var imgList:ArrayList<String> = ArrayList<String>()
    private var imgAdapter:MemoDetailAdapter? = null

    private var originTitle:String? = null
    private var originContents:String? = null
    private var isImgUpdate:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_memo)

        //intent id 값을 받았는지 여부 확인
        intent.extras?.let {bundle->
            id = bundle.getLong("id")
        }

        init()
    }

    private fun init(){
        memoVM = MemoViewModel(application)
        memoVM?.imgListDeleteEvent?.observe(this, Observer { result ->
            imgList = result
            updateImageList()
        })

        setBtnEvents()
        setData()
        setImageUI()
    }

    private fun setImageUI(){
        imgAdapter = MemoDetailAdapter(applicationContext)
        imgAdapter?.memoVM = memoVM

        addMemoBinding.addMemoImg.adapter = imgAdapter
        addMemoBinding.addMemoImg.layoutManager = GridLayoutManager(this, 3)
        imgAdapter?.updateImgList(imgList)
    }

    //데이터 셋팅
    private fun setData(){
        addMemoBinding.addMemoImg.visibility = View.GONE
        id?.let {identical ->
            memoVM?.getMemo(identical)?.observe(this, Observer { memo ->
                if (memo != null) {
                    originTitle = memo.title
                    originContents = memo.contents.substring(1, memo.contents.length - 1)

                    addMemoBinding.addMemoTitle.setText(originTitle)
                    addMemoBinding.addMemoContents.setText(originContents)

                    if(!memo.imgContents.isNullOrEmpty()) {
                        addMemoBinding.addMemoImg.visibility = View.VISIBLE

                        val tempMemoList =
                            memo.imgContents.substring(1, memo.imgContents.length - 1).split(",")
                        Log.d("img list", "temp Memo List : $tempMemoList")
                        for (i in tempMemoList)
                            imgList.add(i)
                        updateImageList()
                        isImgUpdate = false
                    } else {
                        addMemoBinding.addMemoImg.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun setBtnEvents(){
        addMemoBinding.memoToolbarPhoto.setOnClickListener { photoDialog() }
        addMemoBinding.memoToolbarDelete.setOnClickListener { deleteData() }
        addMemoBinding.memoToolbarBack.setOnClickListener {  addUpdateEvent() }
    }

    //데이터 삭제
    private fun deleteData(){
        if(id != null) {
            memoVM?.deleteMemo(id!!)
            Toast.makeText(this, R.string.memo_delete_event, Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    //id 값에 따른 데이터 추가 또는 업데이트
    private fun addUpdateEvent(){
        var titleValue = addMemoBinding.addMemoTitle.text.toString()
        var contentsValue = addMemoBinding.addMemoContents.text.toString()

        if(id == null){
            if(!titleValue.equals("") && !contentsValue.equals("")){
                addData(titleValue, contentsValue)
            }
        } else {
            if ((originTitle != null && !originTitle.equals(titleValue)) || (originContents != null && !originContents.equals(
                    contentsValue
                )) || isImgUpdate
            ) {
                updateData(titleValue, contentsValue)
            }
        }

        finish()
    }

    //데이터 추가
    private fun addData(titleValue:String, contentsValue:String){
        val contentsList = ArrayList<String>()
        contentsList.add(contentsValue)
        memoVM?.insertMemo(titleValue, contentsList, imgList)
        Toast.makeText(this, getText(R.string.memo_add_event), Toast.LENGTH_SHORT).show()
    }

    //데이터 업데이트
    private fun updateData(titleValue:String, contentsValue:String){
        val contentsList = ArrayList<String>()
        contentsList.add(contentsValue)
        memoVM?.updateMemo(id!!, titleValue, contentsList, imgList)
        Toast.makeText(this, getText(R.string.memo_update_event), Toast.LENGTH_SHORT).show()
    }

    //사진 버튼 눌렀을 때 뜨는 다이얼로그
    private fun photoDialog(){
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.photo_dialog_title)
            .setItems(R.array.photo_way, DialogInterface.OnClickListener { dialog, which ->
                when(which) {
                    0 -> {    getImageByPhoto()      }
                    1 -> {    getImageByCamera()     }
                    2 -> {    getImageByURL()        }
                }
            }).create()

        builder.show()
    }

    // 사진을 눌렀을 때 동작
    private fun getImageByPhoto(){
        val intent = Intent()
        intent.apply {
            setType("image/*")
            setAction(Intent.ACTION_OPEN_DOCUMENT)
        }

        startActivityForResult(intent, GlobalVariable.REQ_PHOTO_CODE)
    }

    // 카메라를 눌렀을 때 동작
    private fun getImageByCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePicture ->
            startActivityForResult(takePicture, GlobalVariable.REQ_IMAGE_CAPTURE)
        }
    }

    private fun getImageByURL(){
        val v = layoutInflater.inflate(R.layout.url_dialog, null)
        val url = v.findViewById<EditText>(R.id.memo_img_url)
        val builder = AlertDialog.Builder(this)
            .setView(v)
            .setTitle(R.string.enter_url)
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which ->
                url.text.clear()
                dialog.dismiss()
            })
            .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                val urlData = url.text.toString()
                if(checkUrl(urlData)) {
                    imgList.add(urlData)
                    updateImageList()
                } else {
                    Toast.makeText(this, R.string.wrong_url, Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            })
            .create()
        builder.show()
    }

    //이미지 url 체크
    private fun checkUrl(url:String) : Boolean{
//        val regex = "(http|https)://[.*]+.(jpg|jpeg|gif|png|bmp)".toRegex()
//        return url.matches(regex)

        return (url.startsWith("http") || url.startsWith("https"))
    }

    //이미지 업데이트
    private fun updateImageList(){
        Log.d("img list", "list : $imgList")
        addMemoBinding.addMemoImg.visibility = View.VISIBLE
        imgAdapter?.updateImgList(imgList)
        isImgUpdate = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            GlobalVariable.REQ_PHOTO_CODE -> {
                if(resultCode == RESULT_OK){
                    val uri = data?.data!!

                    imgList.add(uri.toString()!!)
                    updateImageList()
                }
            }

            GlobalVariable.REQ_IMAGE_CAPTURE -> {
                Log.d("camera cde", "result code : $resultCode")
                if(resultCode == RESULT_OK){
                    data?.extras?.get("data")?.let{ bm ->
                        val bitmap = bm as Bitmap
                        val uri = saveCameraFile(bitmap)

                        imgList.add(uri.toString())
                        updateImageList()
                    }

                }
            }
        }
    }

    @Throws(IOException::class)
    fun saveCameraFile(bitmap:Bitmap) : Uri{
        val file = FileUtils(applicationContext).createImageFile()
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        } catch (e:Exception) { e.printStackTrace() }

        return FileProvider.getUriForFile(applicationContext, packageName, file)
    }
}
