package ge.physho.extensions

import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"

fun TextInputLayout.validateEmail(email: String, text: String): Boolean {
    return if (!TextUtils.isEmpty(email)) {
        return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.error = null
            true
        } else {
            this.error = text
            false
        }
    } else {
        this.error = text
        false
    }
}

fun TextInputLayout.validatePassword(password: String, text: String): Boolean {
    return if (password.length < 8 && !Pattern.compile(PASSWORD_PATTERN)
            .matcher(password)
            .matches()
    ) {
        this.error = text
        false
    } else {
        this.error = null
        true
    }
}

fun TextInputLayout.validateRepeatPassword(
    password: String,
    repeatedPassword: String,
    text: String
): Boolean {
    return if (password != repeatedPassword) {
        this.error = text
        false
    } else {
        this.error = null
        true
    }
}
