package my.com.toru.gogotimer.util

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager

object Util {
    fun taskForVersion(preOTask:()->Unit, oTask:()->Unit){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            oTask()
        }
        else{
            preOTask()
        }
    }

    fun hideSoftKeyboard(view: View){
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}