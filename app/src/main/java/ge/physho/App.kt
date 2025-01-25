package ge.physho

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}