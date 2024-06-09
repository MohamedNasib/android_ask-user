package com.wesal.askhail.features.inactiveAdvertList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvertModel

class InactiveAdvertListActivity : BaseActivity(), InActiveAdvertsAdapter.OnInActiveAdvertCallBack {
    lateinit var binding: ActivityInactiveAdvertListBinding
    override fun layoutResource(): Int =R.layout.activity_inactive_advert_list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInactiveAdvertListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar(getString(R.string.inactive_adv))
        setWhiteActivity()
        launching()
        clickers()
    }

    private fun clickers() {

        binding.btnActiveAll.setOnClickListener {
            AppDialogs.activeAdvertDialog(this){
                ParaNormalProcess.loadingProcessActivity(
                    this,
                    {UseCaseImpl().activeAllInactiveAdverts()}
                ){
                    onBackPressed()
                }
            }.show()
        }
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            {UseCaseImpl().showInactiveAdverts()}
        ){
            if (it != null) {
                updateUi(it)
            }
        }

    }

    private fun updateUi(it: List<AdvertModel>) {
        binding.rvInactiveAdverts.layoutManager = LinearLayoutManager(this)
        val adapter = InActiveAdvertsAdapter(it.toMutableList(),false,this)
        binding.rvInactiveAdverts.adapter = adapter
    }

    override fun onInActiveClicked(model: AdvertModel) {
        AppDialogs.activeAdvertDialog(this){
            ParaNormalProcess.loadingProcessActivity(
                this,
                {UseCaseImpl().activeOrBlockAdverts(model.advId,"active")}
            ){
                onBackPressed()
            }
        }.show()

    }
}