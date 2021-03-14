package gr.ppzglou.food.ext

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.radiobutton.MaterialRadioButton


fun Activity?.hideKeyboard() =
    (this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)
        ?.hideSoftInputFromWindow(this?.currentFocus?.windowToken, 0)

fun Activity.openKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT)
}

fun ImageView.setPic(drawable: Int) {
    Glide.with(context)
        .load(ContextCompat.getDrawable(this.context, drawable))
        .into(this)
}

val Resources.screenHeight: Int
    get() = displayMetrics.heightPixels

val Resources.screenWidth: Int
    get() = displayMetrics.widthPixels

fun Activity.vibratePhone() {
    val vibrator = this?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(50)
    }
}

fun MaterialRadioButton.changeButtonEnable(radios: MutableList<MaterialRadioButton>) {
    for (r in radios) {
        if (r != this) {
            r.isChecked = false
        }
    }
}