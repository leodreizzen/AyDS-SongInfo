package ayds.songinfo.utils
import android.util.Log
interface ErrorLogger {
    fun logError(tag: String, msg: String?)
}

internal class ErrorLoggerImpl : ErrorLogger {
    override fun logError(tag: String, msg: String?) {
        Log.e(tag, msg?: "")
    }
}