package mi.song.lmemo.util

import java.util.concurrent.Executors

class IOUtils{
    fun ioThread(f:() -> Unit){
        try {
            Executors.newSingleThreadExecutor().execute(f)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}