package com.wesal.askhail.features.snapShotRequest

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySnapShotRequestBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.subFeatures.dialogSingle.OnSingleDialogSelected
import com.wesal.askhail.subFeatures.dialogSingle.SingleChoiceDialog
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.KeyValueModel
import java.text.SimpleDateFormat
import java.util.*


class SnapShotRequestActivity : BaseActivity() {
    lateinit var binding: ActivitySnapShotRequestBinding
    private var selectedTime: String = ""
    private var selectedDate: String = ""
    private var selectedStatus = "";
    override fun layoutResource(): Int = R.layout.activity_snap_shot_request
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapShotRequestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.ask_for_shot))
        clickers()
    }

    private fun clickers() {
        val newCalendar: Calendar = Calendar.getInstance()
        val dateDialog = DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate: Calendar = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                val myFormat = "dd MMM yyyy" //In which you need put here
                val myString = SimpleDateFormat(myFormat, Locale.US)
                binding.tieDate.setText(myString.format(newDate.time))
                val webFormat = "yyyy-MM-dd" //In which you need put here
                val webString = SimpleDateFormat(webFormat, Locale.US)
                selectedDate = webString.format(newDate.time)

            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        )

        binding.btnCont.setOnClickListener {
            if (validateRequest()) {
                sendRequest()
            }
        }
        binding.tieBuildingStatus.setOnClickListener {
            SingleChoiceDialog(
                this,
                getString(R.string.state_status),
                getBuildingStateList(),
                object : OnSingleDialogSelected<KeyValueModel> {
                    override fun onSelected(value: KeyValueModel) {
                        binding.tieBuildingStatus.setText(value.name)
                        selectedStatus = value.key
                    }

                }
            )
        }
        binding.tieDate.setOnClickListener {
            dateDialog.show()
            dateDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#8660B8"));
            dateDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#8660B8"));
        }
        val c = Calendar.getInstance()
        val oldHour = c.get(Calendar.HOUR)
        val oldMinute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val timeCalendar = Calendar.getInstance()
                timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                timeCalendar.set(Calendar.MINUTE, minute)
                val myFormat = "hh:mm:ss" //In which you need put here
                val myString = SimpleDateFormat(myFormat, Locale.US)
                binding.tieTime.setText(myString.format(timeCalendar.time))
                selectedTime = myString.format(timeCalendar.time)
            }, oldHour, oldMinute, false
        )
        binding.tieTime.setOnClickListener {
            timePickerDialog.show()
            timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#8660B8"));
            timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#8660B8"));
        }
    }

    private fun sendRequest() {
        val map = hashMapOf<String, Any>()
        map["address"] = binding.tieAddress.value()
        map["real_estate_status"] = selectedStatus
        map["name"] = binding.tieFullName.value()
        map["mobile"] = binding.tiePhone.value()
        map["visit_date"] = selectedDate
        map["visit_time"] = selectedTime
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().snapShotRequest(map)}
        ){
            toasting(R.string.success_apply_job)
            onBackPressed()
        }

    }

    private fun getBuildingStateList(): MutableList<KeyValueModel> {
        val list = mutableListOf<KeyValueModel>()
        list.add(KeyValueModel("buy", getString(R.string.buy)))
        list.add(KeyValueModel("rent", getString(R.string.rent)))
        return list;
    }

    private fun validateRequest(): Boolean {
        var isValid = true
        if (!ValidationLayer.validateEmpty(binding.tilFullName, binding.tieFullName)) {
            binding.tieFullName.requestFocus();isValid = false
        }


        if (!ValidationLayer.validatePhone(binding.tilPhone, binding.tiePhone)) {
            binding.tiePhone.requestFocus();isValid = false
        }

        if (!ValidationLayer.validateEmpty(binding.tilAddress, binding.tieAddress)) {
            binding.tieAddress.requestFocus();isValid = false
        }
        if (selectedDate.isNullOrEmpty()) {
            toasting("${getString(R.string.visit_date)} ${getString(R.string.required_field)}")
            isValid = false
        }
        if (selectedTime.isNullOrEmpty()) {
            toasting("${getString(R.string.visit_time)} ${getString(R.string.required_field)}")
            isValid = false
        }
        if (selectedStatus.isNullOrEmpty()) {
            toasting("${getString(R.string.state_status)} ${getString(R.string.required_field)}")
            isValid = false
        }
        return isValid
    }

}