package mi.song.lmemo.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import mi.song.lmemo.R
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.Exception

class MemoImageAdapter(val context: Context) : BaseAdapter() {
    var imgList:ArrayList<String>? = null

    fun updateImgList(imgList:ArrayList<String>){
        this.imgList = imgList
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View? = null
        if(convertView == null)
            view = LayoutInflater.from(context).inflate(R.layout.memo_img_item, null)
        else
            view = convertView

        if(imgList != null) {
            val img = view?.findViewById<ImageView>(R.id.memo_img)
            Log.d("memo img adapter", "${imgList!![position]}")

            Picasso.get().load(imgList!![position]).into(img)
        }

        return view!!
    }

    override fun getItem(position: Int): Any {
        if(imgList == null) return -1
        return imgList?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        if(imgList == null) return 0

        return imgList?.size!!
    }

}