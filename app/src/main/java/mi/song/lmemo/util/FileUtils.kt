package mi.song.lmemo.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException

class FileUtils(val context:Context){

    @Throws(IOException::class)
    fun createImageFile() :File{
        val time = System.currentTimeMillis()
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(time.toString(), ".jpeg", storageDir)
    }
}