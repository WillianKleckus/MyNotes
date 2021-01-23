package com.kleckus.mynotes.dialog_creator.internal

import android.content.Context
import androidx.annotation.StringRes
import com.kleckus.mynotes.dialog_creator.R
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.dialog_creator.service.YesOrNoDialog
import kotlinx.android.synthetic.main.layout_yes_or_no_dialog.view.*

internal class YesOrNoDialogImplementation(private val dialogService: DialogService) : YesOrNoDialog {
    override fun create(
        context: Context,
        @StringRes title : Int,
        onConfirm: (option : Boolean) -> Unit
    ) {
        dialogService.create(
            context = context,
            resId = R.layout.layout_yes_or_no_dialog
        ){ dialog ->
            confirmationText.text = context.getText(title)
            yesButton.setOnClickListener {
                onConfirm(true)
                dialog.dismiss()
            }
            noButton.setOnClickListener {
                onConfirm(false)
                dialog.dismiss()
            }
        }
    }
}