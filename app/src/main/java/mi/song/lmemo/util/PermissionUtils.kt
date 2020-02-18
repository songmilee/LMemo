package mi.song.lmemo.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import mi.song.lmemo.R

class PermissionUtils(val activity:Activity) : Activity() {
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun checkPermission(){
        for(i in permissions){
            if(ContextCompat.checkSelfPermission(activity.applicationContext, i) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, permissions, GlobalVariable.REQ_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            GlobalVariable.REQ_CODE -> {
                if(grantResults.isNotEmpty()){
                    for(i in grantResults){
                        if(i != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(activity.applicationContext, getString(R.string.permission_warn), Toast.LENGTH_SHORT).show()
                            break
                        }
                    }
                }
            }
        }
    }
}