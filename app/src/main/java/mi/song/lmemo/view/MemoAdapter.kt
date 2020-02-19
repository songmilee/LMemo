package mi.song.lmemo.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mi.song.lmemo.R
import mi.song.lmemo.data.Memo
import mi.song.lmemo.databinding.MemoListItemBinding
import mi.song.lmemo.util.TimeUtils

class MemoAdapter(val context:Context) : RecyclerView.Adapter<MemoAdapter.MemoVH>() {
    var memo:List<Memo>? = null

    fun setMemoList(memoList:List<Memo>){
        memo = memoList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoVH {
        return MemoVH(LayoutInflater.from(context).inflate( R.layout.memo_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        if(memo == null)
            return 0
        else
            return memo?.size!!
    }

    override fun onBindViewHolder(holder: MemoVH, position: Int) {
        holder.bindData(memo?.get(position)!!)

    }

    class MemoVH(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val memoListItem: MemoListItemBinding? = DataBindingUtil.bind(itemView)
        fun bindData(memo:Memo){
            memoListItem?.memoListTitle?.setText(memo.title)
            memoListItem?.memoListDate?.setText(TimeUtils().getParsedTime(memo.created_at))

            if(memo.imgContents.length > 0){
                memoListItem?.memoListImg?.visibility = View.VISIBLE

                val imgList = memo.imgContents.substring(1, memo.imgContents.length - 1).split(",")
                Glide.with(itemView).load(imgList[0])
                    .placeholder(R.drawable.img_fail_24dp)
                    .centerCrop()
                    .into(memoListItem?.memoListImg!!)
            } else{
                memoListItem?.memoListImg?.visibility = View.GONE
            }
            itemView.setOnClickListener(itemClickEvent(memo.id))
        }

        //아이템 클릭 이벤트
        fun itemClickEvent(id:Long) = object:View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(itemView.context, MemoDetailActivity::class.java)
                intent.putExtra("id", id)
                itemView.context.startActivity(intent)
            }
        }
    }
}