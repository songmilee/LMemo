package mi.song.lmemo.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import mi.song.lmemo.R
import mi.song.lmemo.databinding.ActivityAddMemoBinding
import mi.song.lmemo.util.FileUtils
import mi.song.lmemo.util.GlobalVariable
import mi.song.lmemo.viewmodel.MemoViewModel
import java.io.File
import java.io.IOException
import java.lang.Exception
import kotlin.collections.ArrayList

class AddMemoActivity : AppCompatActivity() {
    private lateinit var addMemoBinding:ActivityAddMemoBinding
    private var memoVM:MemoViewModel? = null
    private var id:Long? = null
    private var imgList:ArrayList<String> = ArrayList<String>()
    private var imgAdapter:MemoImageAdapter? = null

    private var originTitle:String? = null
    private var originContents:String? = null

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
        setImageUI()
    }

    private fun setImageUI(){
        imgAdapter = MemoImageAdapter(applicationContext)
        addMemoBinding.addMemoImg.adapter = imgAdapter
        imgAdapter?.updateImgList(imgList)
    }

    //데이터 셋팅
    private fun setData(){
        id?.let {identical ->
            memoVM?.getMemo(identical)?.observe(this, Observer { memo ->
                if (memo != null) {
                    originTitle = memo.title
                    originContents = memo.contents.substring(1, memo.contents.length - 1)

                    addMemoBinding.addMemoTitle.setText(originTitle)
                    addMemoBinding.addMemoContents.setText(originContents)
                }
            })
        }
    }

    private fun setBtnEvents(){
        if(id != null)
            addMemoBinding.memoToolbarPhoto.visibility = View.GONE
        else{
            addMemoBinding.memoToolbarPhoto.setOnClickListener { photoDialog() }
        }

        addMemoBinding.memoToolbarDelete.setOnClickListener { deleteData() }
        addMemoBinding.memoToolbarBack.setOnClickListener {  addUpdateEvent() }
    }

    //데이터 삭제
    private fun deleteData(){
        if(id != null) {
            memoVM?.deleteMemo(id!!)
        }
        finish()
    }

    //id 값에 따른 데이터 추가 또는 업데이트
    private fun addUpdateEvent(){
        val titleValue = addMemoBinding.addMemoTitle.text.toString()
        val contentsValue = addMemoBinding.addMemoContents.text.toString()

        if(!titleValue.equals("") && !contentsValue.equals("") && id == null){
            addData(titleValue, contentsValue)
        } else if((originTitle != null && !originTitle.equals(titleValue) )|| (originContents != null && !originContents.equals(contentsValue))) {
            updateData(titleValue, contentsValue)
        }

        finish()
    }

    //데이터 추가
    private fun addData(titleValue:String, contentsValue:String){
        val contentsList = ArrayList<String>()
        contentsList.add(contentsValue)
        memoVM?.insertMemo(titleValue, contentsList, null)
        Toast.makeText(this, getText(R.string.memo_add_event), Toast.LENGTH_SHORT).show()
    }

    //데이터 업데이트
    private fun updateData(titleValue:String, contentsValue:String){
        val contentsList = ArrayList<String>()
        contentsList.add(contentsValue)
        memoVM?.updateMemo(id!!, titleValue, contentsList, null)
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
            setAction(Intent.ACTION_GET_CONTENT)
        }

        startActivityForResult(intent, GlobalVariable.REQ_PHOTO_CODE)
    }

    // 카메라를 눌렀을 때 동작
    private fun getImageByCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePicture ->
            takePicture.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    FileUtils(this).createImageFile()
                } catch (e : IOException) {
                    e.printStackTrace()
                    null
                }

                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(this, packageName, it)
                    Log.d("add memo uri", "$photoURI")
                    takePicture.putExtra("content_uri", photoURI)
                    startActivityForResult(takePicture, GlobalVariable.REQ_IMAGE_CAPTURE)
                }
            }
        }

    }

    private fun getImageByURL(){
        val v = layoutInflater.inflate(R.layout.url_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(v)
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                val url = v.findViewById<EditText>(R.id.memo_img_url)
                requestURLImg(url.text.toString())
                dialog.dismiss()
            })
            .create()
        builder.show()
    }

    private fun requestURLImg(url:String){
        Log.d("add memo", "load url : $url")
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.img_fail_24dp)
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {              }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Toast.makeText(applicationContext, R.string.process_fail, Toast.LENGTH_SHORT).show()
                    e?.printStackTrace()
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    Log.d("request url ", "on resource ready")
                    bitmap?.let {bm ->
                        val uri = FileUtils(applicationContext).saveBitmap(bm)
                        Log.d("request url ", "get uri : $uri")
                        imgList.add(uri.toString())
                        updateImageList()
                    }
                }
            })
    }

    //grid view의 이미지 업데이트
    private fun updateImageList(){
        Log.d("img list", "list : $imgList")
        imgAdapter?.updateImgList(imgList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            GlobalVariable.REQ_PHOTO_CODE -> {
                if(resultCode == RESULT_OK){
                    imgList.add(data?.data.toString())
                    updateImageList()
                }
            }

            GlobalVariable.REQ_IMAGE_CAPTURE -> {
                if(resultCode == RESULT_OK){
                    Log.d("img data", "$data")
                    if(data?.data != null){
                        imgList.add(data?.data.toString())
                        updateImageList()
                    }
                }
            }

            GlobalVariable.REQ_URL_IMAGE -> {

            }
        }
    }
}
