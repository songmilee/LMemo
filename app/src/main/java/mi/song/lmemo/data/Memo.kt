package mi.song.lmemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(@PrimaryKey(autoGenerate = true)val id:Long, val title:String, val contents:String, val created_at:Long)