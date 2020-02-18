package mi.song.lmemo.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtils(val context:Context){
    private val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    @Throws(IOException::class)
    fun createImageFile() :File{
        val time = System.currentTimeMillis()

        return File.createTempFile(time.toString(), ".jpeg", storageDir)
    }

    @Throws(IOException::class)
    fun saveBitmap(bitmap:Bitmap) : Uri {
        if(!storageDir?.exists()!!){
            storageDir?.mkdir()
        }

        val newBitmapFile = File.createTempFile(System.currentTimeMillis().toString(), ".jpeg", storageDir)
        val fos = FileOutputStream(newBitmapFile)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()

        return FileProvider.getUriForFile(context, context.packageName, newBitmapFile)
    }

    private fun addGallery(){

    }
}