package mi.song.lmemo.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomViewTarget
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import mi.song.lmemo.R
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.Exception

class MemoImageAdapter(val context: Context) : RecyclerView.Adapter<MemoImageAdapter.MemoImgVH>() {
    var imgList:ArrayList<String>? = null

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
            holder.bind(url)
//            Picasso.get()
//                .load(url).placeholder(R.drawable.img_fail_24dp)
//                .into(object:Target{
//                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                    placeHolderDrawable?.let {ph->
//                        holder.preloadBind(ph)
//                    }
//                }
//
//                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                    Toast.makeText(context, R.string.process_fail, Toast.LENGTH_SHORT).show()
//                    e?.printStackTrace()
//                }
//
//                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                    holder.bind(bitmap, url)
//                }
//            })
        }
    }

    fun updateImgList(imgList:ArrayList<String>){
        this.imgList = imgList
        notifyDataSetChanged()
    }

    class MemoImgVH(itemView:View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ImageView>(R.id.memo_img)

        fun preloadBind(preloadDrawable:Drawable){
            img.setImageDrawable(preloadDrawable)
        }

        fun bind(url:String){
            Glide.with(img)
                .load(url)
                .placeholder(R.drawable.img_fail_24dp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(img)

            url?.let{
                img.setOnClickListener(imgClickEvent(it))
            }
        }

        fun bind(bm:Bitmap?, url:String?){
            if(bm == null)
                img.setImageResource(R.drawable.img_fail_24dp)
            else
                img.setImageBitmap(bm)

            url?.let {
                img.setOnClickListener(imgClickEvent(it))
            }
        }

        private fun imgClickEvent(url:String) = View.OnClickListener {
            val intent = Intent(itemView.context, ZoomActivity::class.java)
            intent.putExtra("url", url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            itemView.context.startActivity(intent)
        }
    }

}