package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleckus.mynotes.R
import com.kleckus.mynotes.system.Option
import com.kleckus.mynotes.system.OptionAdapter
import kotlinx.android.synthetic.main.options_dialog_layout.view.*

class OptionsDialog(private val optionList : MutableList<Option>) : DialogFragment() {

    private val adapter = OptionAdapter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater!!.inflate(R.layout.options_dialog_layout, null)

        adapter.dataset = optionList
        view.optionRecyclerView.adapter = adapter
        view.optionRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter.onOptionChosen = {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}