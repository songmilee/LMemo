package mi.song.lmemo.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtils(val context:Activity){
    private var storageDir = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES)//context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    @Throws(IOException::class)
    fun createImageFile() :File{
        val time = System.currentTimeMillis()

        return File.createTempFile(time.toString(), ".jpeg", storageDir)
    }

    @Throws(IOException::class)
    fun getCameraTempValueUri() : Uri? {
        //new Bitmap

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val cv = ContentValues().apply {
                put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            return  context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)

        } else {
            if (!storageDir?.exists()!!) {
                storageDir?.mkdir()
            }

            val file = createImageFile()

            return FileProvider.getUriForFile(context, context.packageName, file)
        }

        return null
    }

}