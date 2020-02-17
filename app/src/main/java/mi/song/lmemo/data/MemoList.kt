package mi.song.lmemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemoList(@PrimaryKey(autoGenerate = true)val id:Long, val title:String, val created_at:Long)