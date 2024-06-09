package com.wesal.askhail.features.confimPhone.savemember

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wesal.askhail.R
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.databinding.ActivitySaveMemberBinding
import com.wesal.askhail.databinding.ActivityTermsAndConditionsBinding

class SaveMember : AppCompatActivity() {

    lateinit var binding:ActivitySaveMemberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_save_member)
        binding = ActivitySaveMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("")

    }
}