package com.wesal.askhail.subFeatures.dialogSingle

import android.app.Activity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.entities.models.*


class SingleChoiceDialog<T>(
    val context: Activity,
    private val title: String,
    private val myList: List<T>?,
    private val callback: OnSingleDialogSelected<T>
) : SingleChoiceAdapter.OnSingleClicked {
    override fun onClicked(position: Int) {
        myList?.let {
            callback.onSelected(myList[position])
            if (dialog.isShowing)
                dialog.dismiss()
        }
    }

    private val adapterList: MutableList<String> = ArrayList()
    private var dialog: AlertDialog

    init {
        myList?.let {
            for (model in myList) {
                when (model) {
                    is SectionModel -> {
                        adapterList.add(model.sectionName)
                    }
                    is BlocksModel -> {
                        adapterList.add(model.blockName)
                    }
                    is SidesModel -> {
                        adapterList.add(model.sideName)
                    }
                    is FeatureDataModel -> {
                        adapterList.add(model.dataTitle)
                    }
                    is KeyValueModel -> {
                        adapterList.add(model.name)
                    }


                }
            }
        }
        dialog = creatingDialog()
        dialog.show()
    }

    private fun creatingDialog(): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val headerView = context.layoutInflater.inflate(R.layout.custom_alert_title, null)
        val dialogView = context.layoutInflater.inflate(R.layout.dialog_single_choise, null)
        val mTextView = headerView.findViewById<TextView>(R.id.alertTitle)
        mTextView.text = title
        builder.setCustomTitle(headerView)
        builder.setView(dialogView)

        val rv = dialogView.findViewById<RecyclerView>(R.id.rvSingleSelect)
        val depthCategoryAdapter = SingleChoiceAdapter(adapterList, this)

        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = depthCategoryAdapter
        // Show the AlertDialog


        //        // Change the title divider
        //        final Resources res = context.getResources();
        //        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        //        final View titleDivider = dialog.findViewById(titleDividerId);
        //        if (titleDivider != null) {
        //            titleDivider.setBackgroundColor(res.getColor(android.R.color.holo_orange_dark));
        //        }
        return builder.create()
    }

}

interface OnSingleDialogSelected<T> {
    fun onSelected(value: T)
}