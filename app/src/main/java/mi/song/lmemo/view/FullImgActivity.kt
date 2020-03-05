package mi.song.lmemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import mi.song.lmemo.R
import mi.song.lmemo.databinding.ActivityFullImgBinding

class FullImgActivity : AppCompatActivity() {
    private lateinit var fullImageBinding:ActivityFullImgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullImageBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_img)

        val imgUrl = intent.extras?.getString("url")
        android.util.Log.d("FullImgActivity", "img url : $imgUrl")

        Glide.with(fullImageBinding.memoFullImgView)
            .load(imgUrl)
            .error(R.drawable.img_fail_24dp)
            .into(fullImageBinding.memoFullImgView)
    }
}
