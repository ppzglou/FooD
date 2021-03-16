package gr.ppzglou.food.ext

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.tapadoo.alerter.Alerter
import gr.ppzglou.food.*


fun Dialog.createPopUpDialog(
    mTitle: String,
    mDescription: String,
    mPosButton: String,
    mNegButton: String,
    l: View.OnClickListener
) {
    this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    this.setCancelable(false)
    this.setContentView(R.layout.fragment_general_dialog)

    val title = this.findViewById(R.id.dialog_title) as TextView
    val description = this.findViewById(R.id.dialog_description) as TextView
    val posButton = this.findViewById(R.id.dialog_pos_button) as Button
    val negButton = this.findViewById(R.id.dialog_neg_button) as Button

    title.text = mTitle
    description.text = mDescription
    posButton.text = mPosButton
    negButton.text = mNegButton

    posButton.setOnClickListener(l)
    negButton.setOnClickListener { this.dismiss() }
    this.show()
}


fun Activity.foodToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}

fun Activity.foodErrorSnackBar(error: Int) {
    val title = error.setTitle()
    val message = error.getErrorMessage()
    val icon = error.setIcon()
    val color = R.color.colorPrimary

    Alerter.create(this)
        .setTitle(getString(title))
        .setText(getString(message))
        .setBackgroundColorRes(color)
        .setIcon(icon)
        .show()
}


fun Int.setIcon(): Int {
    return when (this) {
        ERROR_NO_INTERNET -> R.drawable.alerter_ic_face
        else -> R.drawable.alerter_ic_notifications
    }
}

fun Int.setTitle(): Int {
    return when (this) {
        ERROR_NO_INTERNET -> R.string.ert_INTERNET
        ERROR_GENERAL -> R.string.ert_ERROR
        ERROR_MEMBER_EXIST -> R.string.ert_WARNING
        ERROR_NOT_EMAIL_VALIDATED -> R.string.ert_VER_EMAIL
        ERROR_NOT_MEMBER_EXIST -> R.string.ert_ERROR
        ERROR_WRONG_PASSWORD -> R.string.ert_ERROR
        ERROR_MEMBER_HAS_DISABLED -> R.string.ert_WARNING
        ERROR_MEMBER_HAS_BLOCKED -> R.string.ert_MAX_REQUEST
        ERROR_TOO_MANY_REQUESTS -> R.string.ert_MAX_REQUEST
        ERROR_INVALID_CREDENTIALS -> R.string.ert_ERROR
        ERROR_WRONG_OTP_CODE -> R.string.ert_ERROR
        ERROR_PHONE_IS_LINKED_WITH_OTHER_ACCOUNT -> R.string.ert_ERROR
        ERROR_PIN_OF_USER_IS_WRONG -> R.string.ert_wrong_pin
        else -> R.string.ert_ERROR
    }
}

fun Int.getErrorMessage(): Int {
    return when (this) {
        ERROR_NO_INTERNET -> R.string.ERROR_NO_INTERNET
        ERROR_GENERAL -> R.string.ERROR_GENERAL
        ERROR_MEMBER_EXIST -> R.string.ERROR_MEMBER_EXIST
        ERROR_NOT_EMAIL_VALIDATED -> R.string.ERROR_NOT_EMAIL_VALIDATED
        ERROR_NOT_MEMBER_EXIST -> R.string.ERROR_NOT_MEMBER_EXIST
        ERROR_WRONG_PASSWORD -> R.string.ERROR_WRONG_PASSWORD
        ERROR_MEMBER_HAS_DISABLED -> R.string.ERROR_MEMBER_HAS_DISABLED
        ERROR_MEMBER_HAS_BLOCKED -> R.string.ERROR_MEMBER_HAS_BLOCKED
        ERROR_TOO_MANY_REQUESTS -> R.string.ERROR_TOO_MANY_REQUESTS
        ERROR_INVALID_CREDENTIALS -> R.string.ERROR_INVALID_CREDENTIALS
        ERROR_WRONG_OTP_CODE -> R.string.ERROR_WRONG_OTP_CODE
        ERROR_PHONE_IS_LINKED_WITH_OTHER_ACCOUNT -> R.string.ERROR_PHONE_IS_LINKED_WITH_OTHER_ACCOUNT
        ERROR_PIN_OF_USER_IS_WRONG -> R.string.empty
        else -> {
            R.string.ERROR_GENERAL
        }
    }
}


