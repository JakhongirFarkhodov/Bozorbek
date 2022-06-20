package com.example.bozorbek_vol2.ui

import android.content.Context
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showToast(message:String)
{
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showSuccessDialog(message: String)
{

    MaterialAlertDialogBuilder(this)
        .setTitle("Успешно")
        .setMessage(message)
        .setNegativeButton("Ok",{dialog,which ->
            dialog.dismiss()
        }).show()
//    MaterialDialog(this).show {
//        title(text = "Успешно")
//        message(text = message)
//        positiveButton(text = "Ок")
//    }
}

fun Context.showErrorDialog(message: String)
{

    MaterialAlertDialogBuilder(this)
        .setTitle("Ошибка")
        .setMessage(message)
        .setNegativeButton("Ok",{dialog,which ->
            dialog.dismiss()
        }).show()
//    MaterialDialog(this).show {
//        title(text = "Ошибка")
//        message(text = message)
//        positiveButton(text = "Ок")
//    }
}