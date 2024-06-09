package com.wesal.askhail.core.extentions

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.wesal.askhail.R
import com.wesal.askhail.features.browsingType.BrowsingTypeActivity
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.askhail.features.visitorLogin.VisitorLoginActivity
import com.wesal.domain.useCases.UseCaseImpl


fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}
fun View.invisible(){
    this.visibility = View.INVISIBLE
}
inline fun requiredLoginArea(view: View?, isRedirectToPage:Boolean,code: () -> Unit) {
    val isVisitor = UseCaseImpl().isInVisitorMode()
    if (!isVisitor) {
        code()
    } else {
        showLoginSnackBar(view,isRedirectToPage)

    }
}
fun showLoginSnackBar(view: View?,isRedirectToPage:Boolean) {
    view?.let {
        if (isRedirectToPage){
            view.context.sStartActivity<VisitorLoginActivity>()
        }else{
            val snack = Snackbar.make(view, R.string.unAuthorize, Snackbar.LENGTH_LONG)
            snack.setActionTextColor(Color.parseColor("#39CDEE"))
            snack.setAction(R.string.login) {
                view.context.sStartActivityWithClear<BrowsingTypeActivity>()
            }
            snack.show()
        }

    }

}

fun View.openKeyboard(activity:Activity){
    val inputMethodManager =
        activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager!!.toggleSoftInputFromWindow(
        this.applicationWindowToken,
        InputMethodManager.SHOW_FORCED,
        0
    )
}
fun hideKeyboardFrom(context: Context, view: View) {
    val imm =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
//inline fun requiredLoginArea(view: View?, code: () -> Unit) {
//    val isVisitor = AuthorizationUseCases().isVisitor()
//    if (!isVisitor) {
//        code()
//    } else {
//        showLoginSnackBar(view)
//    }
//}
//fun showLoginSnackBar(view: View?) {
//    view?.let {
//        val snack = Snackbar.make(view, R.string.unAuthorize, Snackbar.LENGTH_LONG)
//        snack.setAction(R.string.login) {
//            view.context.sStartActivityWithClear<LoginActivity>()
//        }
//        snack.show()
//    }
//
//}
