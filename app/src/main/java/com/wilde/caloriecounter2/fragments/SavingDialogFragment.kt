package com.wilde.caloriecounter2.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment


interface SavingDialogCallback {
    fun cancelResponse()
    fun addNewResponse()
    fun updateOldResponse()
}

class SavingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val callback = parentFragment as SavingDialogCallback

        val builder = AlertDialog.Builder(requireParentFragment().context)
        builder.apply {
            setMessage(
                "You have already added this food from online. Would you like to update " +
                        "it, or add this food a second time?"
            )
            setNegativeButton(
                "Cancel"
            ) { _, _ ->
                callback.cancelResponse()
            }
            setPositiveButton(
                "Update Old Food"
            ) { _, _ ->
                callback.updateOldResponse()
            }
            setNeutralButton(
                "Add New Food"
            ) { _, _ ->
                callback.addNewResponse()
            }
        }

        return builder.create()
    }

}