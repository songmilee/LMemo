package mi.song.lmemo.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.davemorrissey.labs.subscaleview.ImageSource
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import mi.song.lmemo.R
import mi.song.lmemo.databinding.ActivityZoomBinding
import java.lang.Exception

//zoom-in, zoom-out 할 수 있는 액티비티
class ZoomActivity : AppCompatActivity() {
    lateinit var zoomBinding:ActivityZoomBinding
    var imgInfo:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        zoomBinding = DataBindingUtil.setContentView<ActivityZoomBinding>(this, R.layout.activity_zoom)

        intent.extras?.let {
            imgInfo = it.getString("url")
        }

        setImage()
    }

    //uri로 받아온 이미지 띄우기
    fun setImage(){
        imgInfo?.let {url ->
            Picasso.get()
                .load(url)
                .into(object:Target{
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {               }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        zoomBinding.zoomView.setImage(ImageSource.resource(R.drawable.img_fail_24dp))
                        Toast.makeText(applicationContext, R.string.process_fail, Toast.LENGTH_SHORT).show()
                        e?.printStackTrace()
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        if(bitmap != null)
                            zoomBinding.zoomView.setImage(ImageSource.bitmap(bitmap))
                        else
                            zoomBinding.zoomView.setImage(ImageSource.resource(R.drawable.img_fail_24dp))
                    }
                })

        }
    }
}
