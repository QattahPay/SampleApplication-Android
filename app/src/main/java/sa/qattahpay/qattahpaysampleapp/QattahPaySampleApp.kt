package sa.qattahpay.qattahpaysampleapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class QattahPaySampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModule = module {
            single { MainViewModel(get(), get() as MainRepository) }
            single { MainRepository() }
        }

        startKoin {
            androidContext(this@QattahPaySampleApp)
            modules(listOf(myModule))
        }

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}