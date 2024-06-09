package com.wesal.askhail.core.utilities

import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wesal.askhail.R

object ValidationLayer {

    fun validateWebSite(tilPhone: TextInputLayout, tiePhone: TextInputEditText): Boolean {
        return if (tiePhone.text.toString().isEmpty()) {
            true
        } else {
            if (!Patterns.WEB_URL.matcher(tiePhone.text.toString()).matches()) {
                tilPhone.error = tilPhone.context.getString(R.string.validate_web)
                false
            } else {
                tilPhone.error = null
                true
            }
        }

    }

    fun validateEmail(tilPhone: TextInputLayout, tiePhone: TextInputEditText): Boolean {
        return if (tiePhone.text.toString().isEmpty()) {
            tilPhone.error = tilPhone.context.getString(R.string.required_field)
            false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(tiePhone.text.toString()).matches()) {
                tilPhone.error = tilPhone.context.getString(R.string.validate_email)
                false
            } else {
                tilPhone.error = null
                true

            }
        }

    }
    fun validatePhone(tilPhone: TextInputLayout, tiePhone: TextInputEditText): Boolean {
        return if (tiePhone.text.toString().isEmpty()) {
            tilPhone.error = tilPhone.context.getString(R.string.required_field)
            false
        } else {
            if (!Patterns.PHONE.matcher(tiePhone.text.toString()).matches()) {
                tilPhone.error = tilPhone.context.getString(R.string.validate_phone)
                false
            } else {
                tilPhone.error = null
                true

            }
        }

    }
    fun validateLength(tilPhone: TextInputLayout, tiePhone: TextInputEditText): Boolean {
        return if (tiePhone.text.toString().isEmpty()) {
            tilPhone.error = tiePhone.context.getString(R.string.required_field)
            false
        } else {

            if (tiePhone.text.toString().length < 5) {
                tilPhone.error = tiePhone.context.getString(R.string.validae_lengh)
                false
            } else {
                tilPhone.error = null
                true

            }
        }
    }

    fun validateMatch(tieSecond: TextInputEditText, tieFirst: TextInputEditText, tilSecond: TextInputLayout): Boolean {
        if (tieSecond.text.toString().isEmpty()) {
            tilSecond.error = tieSecond.context.getString(R.string.required_field)
            return false
        } else {
            if (tieSecond.text.toString() != tieFirst.text.toString()) {
                tilSecond.error = tieSecond.context.getString(R.string.validae_dismatch)
                return false
            } else {
                tilSecond.error = null
                return true
            }
        }
    }
    fun validateEmpty(editText: EditText): Boolean {
        return if (editText.text.toString().isEmpty()) {
            editText.error = editText.context.getString(R.string.required_field)
            false
        } else {
            true
        }
    }
    fun validateEmpty(tilPhone: TextInputLayout, tiePhone: TextInputEditText): Boolean {
        return if (tiePhone.text.toString().isEmpty()) {
            tilPhone.error = tiePhone.context.getString(R.string.required_field)
            false
        } else {

            tilPhone.error = null
            true
        }
    }


    fun empty(editText: EditText): Boolean {
        return if (editText.text.toString().isEmpty()) {
            editText.error = editText.context.getString(R.string.required_field)
            false
        } else {
            true
        }
    }


}