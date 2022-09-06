package com.eliseubatista.pocketdex.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.databinding.ProgressBarBinding

class ProgressBarDialog(val inflater: LayoutInflater, val context: Context) {

    private var alertDialog: AlertDialog? = null

    fun startLoading(loadingMessage: String) {

        if(alertDialog?.isShowing == true)
        {
            return
        }

        val binding: ProgressBarBinding =
            DataBindingUtil.inflate(inflater, R.layout.progress_bar, null, false)

        binding.loadingText.text = loadingMessage

        //val dialogView = inflater.inflate(R.layout.progress_bar, null)

        val builder = AlertDialog.Builder(context)

        builder.setView(binding.root)
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog!!.show()
    }

    fun dismiss() {
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
    }

}