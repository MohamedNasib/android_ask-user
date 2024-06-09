package com.wesal.askhail.features.TermsandConditions

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wesal.askhail.R
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.databinding.ActivityHailPolicyBinding
import com.wesal.askhail.databinding.ActivityTermsAndConditionsBinding

class TermsandConditions : AppCompatActivity() {

    lateinit var binding:ActivityTermsAndConditionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_terms_and_conditions)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("")

    }
}