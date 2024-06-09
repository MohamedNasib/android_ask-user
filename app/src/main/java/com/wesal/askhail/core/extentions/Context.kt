package com.wesal.askhail.core.extentions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import java.io.Serializable


inline fun <reified T : Activity> Context.sStartActivity(
    vararg params: Pair<String, Any?>
) {
    val intent = Intent(this, T::class.java)
    params.forEach {
        Log.e("STARTAC"," ${it.first}  ${it.second}")
        when (it.second) {
            is String -> intent.putExtra(it.first, it.second as String)
            is Int -> intent.putExtra(it.first, it.second as Int)
            is Boolean -> intent.putExtra(it.first, it.second as Boolean)
            is Parcelable -> intent.putExtra(it.first, it.second as Parcelable)
        }
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Context.sStartActivityWithClear(
    vararg params: Pair<String, Any?>
) {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    params.forEach {
        when (it.second) {
            is String -> intent.putExtra(it.first, it.second as String)
            is Int -> intent.putExtra(it.first, it.second as Int)
            is Parcelable -> intent.putExtra(it.first, it.second as Parcelable)
            is Boolean -> intent.putExtra(it.first, it.second as Boolean)
            is Serializable -> intent.putExtra(it.first, it.second as Serializable)
            else -> throw IllegalStateException("unDefined Type in Intent")
        }
    }
    startActivity(intent)
}
fun Context.toasting(value: String?) {
    value?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}
fun Context.toasting(value: Int) {
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}
fun Context.toasting(value: Int,isSpecial:Boolean) {
    val toast =Toast.makeText(this, value, Toast.LENGTH_LONG)
    val toastView = toast.view
    val toastMessage =toastView?.findViewById<TextView>(android.R.id.message)
    toastMessage?.let {
        it.apply {
           setTextColor(Color.WHITE)
        }
    }
    toastView?.let {
        it.apply {
            setBackgroundColor(Color.parseColor("#034B89"))
        }
    }
    toast.show()
}
fun Context.goToUrl(url:String){
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
}
fun View.closeKeyboard(){
    val imm: InputMethodManager? =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(this.windowToken, 0)
}
fun getLoadingDialog(context: Context): KProgressHUD {
    return KProgressHUD.create(context)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//        .setLabel(context.getText(R.string.please_wait).toString())
        .setCancellable(false)
        .setAnimationSpeed(2)
        .setDimAmount(0.5f)

}

