package mi.song.lmemo.app

import android.app.Application
import mi.song.lmemo.viewmodel.MemoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class LMemoApplication : Application() {
    lateinit var moduleList:Module
    override fun onCreate() {
        super.onCreate()

        moduleList = module {
            single { MemoViewModel(this@LMemoApplication) }
        }

        //app 레벨에서 koin 시작
        startKoin {
            androidContext(this@LMemoApplication)
            modules(moduleList)
        }
    }
}