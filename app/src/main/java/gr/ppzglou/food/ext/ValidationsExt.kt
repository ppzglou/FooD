package gr.ppzglou.food.ext

import android.text.Editable
import android.util.Patterns
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import gr.ppzglou.food.R
import java.util.*

fun Editable.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Editable.isValidPhone() = "69[0-9]{8}".toRegex().matches(this)

fun Editable.isValidPassword() =
    "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@\$!%*#?&`~])(?!.*[/.,=\\-+_<>|}{:\"';\\[\\]()]).{8,}\$".toRegex()
        .matches(this)

fun Editable.isValidPostCode() = "[0-9]{5}".toRegex().matches(this)

fun Editable.isValidAFM() = "[0-9]{9}".toRegex().matches(this)

fun Editable.isValidAMKA() = "[0-9]{11}".toRegex().matches(this)

fun Editable.isValidPin() = "[0-9]{4}".toRegex().matches(this)

fun Editable.isValidYear() = "[0-9]{4}".toRegex().matches(this)


fun CheckBox.isValid(): Boolean {
    this.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) this.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }
    return if (!this.isChecked) {
        this.setTextColor(ContextCompat.getColor(context, R.color.colorErrorPink))
        false
    } else
        true
}

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

fun TextInputLayout.isValidPostCode(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        if (!editText?.text?.isValidPostCode()!!) {
            error = resources.getString(R.string.error_post_code)
            isEnabled = true
            return false
        }
        return true
    }
}

fun TextView.isAnyChecked(list: MutableList<SwitchMaterial>): Boolean {
    for (switch in list) {
        if (switch.isChecked) {
            this.setTextColor(ContextCompat.getColor(this.context, R.color.colorBlack))
            return true
        }
    }
    this.setTextColor(ContextCompat.getColor(this.context, R.color.colorErrorPink))
    return false
}

fun TextInputLayout.isValidAFM(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        if (!editText?.text?.isValidAFM()!!) {
            error = resources.getString(R.string.error_afm_digits)
            isEnabled = true
            return false
        }
        try {
            val integers = arrayListOf<Int>()
            for (i in this.editText?.text.toString().indices) {
                integers.add(this.editText?.text.toString().substring(i, i + 1).toInt())
            }
            var multiplier = 512
            val sum = integers.dropLast(1).fold(0) { acc, c ->
                multiplier /= 2
                acc + multiplier * c
            }
            if (sum.rem(11).rem(10) != integers.last()) {
                error = resources.getString(R.string.error_afm)
                isEnabled = true
                return false
            }
            return true
        } catch (e: Exception) {
            error = resources.getString(R.string.error_afm)
            isEnabled = true
            return false
        }
    }
}

fun TextInputLayout.isValidAMKA(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        if (!editText?.text?.isValidAMKA()!!) {
            error = resources.getString(R.string.error_amka)
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

fun TextInputLayout.isValidYear(): Boolean {
    return if (!isNotEmpty()) {
        false
    } else {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        if (!editText?.text?.isValidYear()!! || editText?.text.toString().toInt() !in 1870..year) {
            error = resources.getString(R.string.error_year)
            isEnabled = true
            return false
        }
        return true
    }
}