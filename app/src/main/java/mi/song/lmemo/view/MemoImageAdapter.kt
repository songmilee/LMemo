package mi.song.lmemo.view

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import mi.song.lmemo.R

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
            img?.setImageURI(Uri.parse(imgList!![position]))
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