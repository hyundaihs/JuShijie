package com.android.shuizu.myutillibrary.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.shuizu.myutillibrary.R
import com.cazaea.sweetalert.SweetAlertDialog
import com.squareup.picasso.Picasso
import java.io.File


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/3/31/031.
 */

fun Any.getLoadingDialog(context: Context): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE).apply {
        titleText = "Loading"
        setCancelable(false)
    }
}

fun Any.getMessageDialog(
    context: Context,
    meesage: String,
    listener: SweetAlertDialog.OnSweetClickListener? = null
): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE).apply {
        titleText = meesage
        confirmText = "确定"
        setConfirmClickListener(listener)
        show()
    }
}

fun Any.getSuccessDialog(
    context: Context,
    meesage: String,
    listener: SweetAlertDialog.OnSweetClickListener? = null
): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).apply {
        titleText = meesage
        confirmText = "确定"
        setConfirmClickListener(listener)
        show()
    }
}

fun Any.getErrorDialog(
    context: Context,
    error: String,
    listener: SweetAlertDialog.OnSweetClickListener? = null
): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).apply {
        titleText = error
        confirmText = "确定"
        setConfirmClickListener(listener)
        show()
    }
}

fun Any.getLoginErrDialog(context: Context, listener: SweetAlertDialog.OnSweetClickListener? = null): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).apply {
        titleText = "需要重新验证登陆!"
        confirmText = "确定"
        setConfirmClickListener(listener)
        show()
    }
}