package com.example.bozorbek_vol2.ui

import android.content.Context
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog

fun Context.showToast(message:String)
{
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showSuccessDialog(message: String)
{
    MaterialDialog(this).show {
        title(text = "Успешно")
        message(text = message)
        positiveButton(text = "Ок")
    }
}

fun Context.showErrorDialog(message: String)
{
    MaterialDialog(this).show {
        title(text = "Ошибка")
        message(text = message)
        positiveButton(text = "Ок")
    }
}