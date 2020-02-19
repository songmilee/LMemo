package mi.song.lmemo.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

//이미지 삭제 이벤트 클래스
// adapter의 이미지 리스트와 activity의 이미지 리스트의 값을 맞춰줌
class ImgDeleteEvent : MutableLiveData<ArrayList<String>>() {
    val isImageDelete = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in ArrayList<String>>) {
        super.observe(owner, Observer<ArrayList<String>> {t->
            if(isImageDelete.compareAndSet(true, false)){
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(value: ArrayList<String>?) {
        isImageDelete.set(true)
        super.setValue(value)
    }

}