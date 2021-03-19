package gr.ppzglou.food.ext

import android.text.Editable
import android.util.Patterns
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import gr.ppzglou.food.R

fun Editable.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Editable.isValidPhone() = "69[0-9]{8}".toRegex().matches(this)

fun Editable.isValidPassword() =
    "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@\$!%*#?&`~])(?!.*[/.,=\\-+_<>|}{:\"';\\[\\]()]).{8,}\$".toRegex()
        .matches(this)

fun Editable.isValidPin() = "[0-9]{4}".toRegex().matches(this)

fun TextInputLayout.isNotEmpty(): Boolean {
    this.editText?.addTextChangedListener {
        this.error = null
    }
    return if (this.editText?.text.toString().isEmpty()) {
        this.error = resources.getString(R.string.error_empty)
        false
    } else
        true
}

fun TextInputLayout.isValidEmail(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        if (!editText?.text?.isValidEmail()!!) {
            error = resources.getString(R.string.error_email)
            isEnabled = true
            return false
        }
        return true
    }
}

fun TextInputLayout.isValidPhone(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        if (!editText?.text?.isValidPhone()!!) {
            error = resources.getString(R.string.error_phone)
            isEnabled = true
            return false
        }
        return true
    }
}

fun TextInputLayout.isValidPassword(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        if (!editText?.text?.isValidPassword()!!) {
            error = resources.getString(R.string.error_password)
            isEnabled = true
            return false
        }
        return true
    }
}



fun TextInputLayout.isValidPin(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        if (!editText?.text?.isValidPin()!!) {
            error = resources.getString(R.string.error_pin)
            isEnabled = true
            return false
        }
        return true
    }
}
