package com.example.inspector.Utils

import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import com.example.inspector.R
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Lucas Alves dos Santos on 19/05/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class Validator {
    companion object {
        fun isEmptyField(textInputLayout: TextInputLayout): Boolean {
            if(textInputLayout.editText?.text.toString().isBlank()) {
                textInputLayout.editText?.error = Strings.get(R.string.mandatory_default_field)
                textInputLayout.requestFocus()
                return true
            }
            return false
        }

        fun isEmailPatters(textInputLayout: TextInputLayout): Boolean {
            return if(Patterns.EMAIL_ADDRESS.matcher(textInputLayout.editText?.text.toString()).matches()) {
                true
            } else {
                textInputLayout.editText?.error = Strings.get(R.string.mantaory_email)
                textInputLayout.requestFocus()
                false
            }
        }

        fun isCorrectSize(textInputLayout: TextInputLayout, size: Int): Boolean {
            if(textInputLayout.editText?.text.toString().count() < 6) {
                textInputLayout.editText?.error = Strings.get(R.string.mandatory_password)
                textInputLayout.requestFocus()
                return false
            }
            return false
        }
    }
}