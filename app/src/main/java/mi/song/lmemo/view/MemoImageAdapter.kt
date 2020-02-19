package mi.song.lmemo.view

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import mi.song.lmemo.R
import mi.song.lmemo.viewmodel.MemoViewModel

class MemoImageAdapter(val context: Context) : RecyclerView.Adapter<MemoImageAdapter.MemoImgVH>() {
    var imgList:ArrayList<String>? = null
    var memoVM:MemoViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoImgVH {
        return MemoImgVH(LayoutInflater.from(context).inflate(R.layout.memo_img_item, parent, false))
    }

    override fun getItemCount(): Int {
        if(imgList == null) return 0

        return imgList?.size!!
    }

    override fun onBindViewHolder(holder: MemoImgVH, position: Int) {
        if(imgList != null){
            Log.d("memo img adapter", "position : $position url : ${imgList!![position]}")
            val url = imgList!![position].trim()
            holder.bind(url, position)
        }
    }

    fun updateImgList(imgList:ArrayList<String>){
        this.imgList = imgList
        notifyDataSetChanged()
    }

    inner class MemoImgVH(itemView:View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ImageView>(R.id.detail_item_img)
        val clear = itemView.findViewById<ImageView>(R.id.detail_item_clear)

        fun bind(url:String, position: Int){
            Glide.with(img)
                .load(url)
                .placeholder(R.drawable.img_fail_24dp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(object: CustomViewTarget<ImageView, Drawable>(img){
                    override fun onResourceCleared(placeholder: Drawable?) {               }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        Toast.makeText(itemView.context, R.string.process_fail, Toast.LENGTH_SHORT).show()
                        img.setImageDrawable(errorDrawable)
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        img.setImageDrawable(resource)
                    }
                })

            clear.setOnClickListener(clearClickEvent(position))
        }

        private fun clearClickEvent(position: Int) = View.OnClickListener {
            imgList!!.removeAt(position)
            memoVM?.imgListDeleteEvent?.setValue(imgList!!)
            notifyDataSetChanged()
        }
    }

}