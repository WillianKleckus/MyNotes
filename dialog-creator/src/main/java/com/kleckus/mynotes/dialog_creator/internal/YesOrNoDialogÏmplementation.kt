package com.kleckus.mynotes.dialog_creator.internal

import android.content.Context
import com.kleckus.mynotes.dialog_creator.R
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.dialog_creator.service.YesOrNoDialog
import kotlinx.android.synthetic.main.layout_yes_or_no_dialog.view.*

internal class YesOrNoDialogÏmplementation(private val dialogService: DialogService) : YesOrNoDialog {
    override fun create(context: Context, onYes: () -> Unit, onNo: () -> Unit) {
        dialogService.create(
            context = context,
            resId = R.layout.layout_yes_or_no_dialog
        ){ dialog ->
            yesButton.setOnClickListener {
                onYes()
                dialog.dismiss()
            }
            noButton.setOnClickListener {
                onNo()
                dialog.dismiss()
            }
        }
    }
}