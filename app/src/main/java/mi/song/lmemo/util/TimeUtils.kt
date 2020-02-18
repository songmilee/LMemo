package mi.song.lmemo.util

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils{
    fun getParsedTime(time:Long) : String{
        val sdf = SimpleDateFormat("yyyy-MM-dd h:mm", Locale.KOREA)
        return sdf.format(time)
    }
}