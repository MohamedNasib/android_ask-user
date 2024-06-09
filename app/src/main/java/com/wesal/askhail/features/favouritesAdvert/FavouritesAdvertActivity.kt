package com.wesal.askhail.features.favouritesAdvert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityConfirmPhoneBinding
import com.wesal.askhail.databinding.ActivityFavouritesAdvertBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvertModel

class FavouritesAdvertActivity : BaseActivity(), AdvertsListAdapter.OnAdvertCallBack {
    lateinit var binding: ActivityFavouritesAdvertBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var adapter: AdvertsListAdapter? =
        AdvertsListAdapter(mutableListOf(), false, this@FavouritesAdvertActivity)

    override fun layoutResource(): Int = R.layout.activity_favourites_advert
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesAdvertBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar(getString(R.string.acc_myfav))
        setWhiteActivity()
        clickers()
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getMyFavouritesAdvert() },
            R.drawable.ic_status_empty_fav,
            R.string.empty_fav,
            false
        ) {
            it?.let { data ->
                if (data.isNullOrEmpty()) {
                    binding.tvClearAll.gone()
                } else {
                    binding.tvClearAll.visible()
                }
                binding.rvAdvertsList.layoutManager = LinearLayoutManager(this)
                adapter?.addMoreInList(it)
                binding.rvAdvertsList.adapter = adapter
            }
        }

    }

    private fun clickers() {

        binding.tvClearAll.setOnClickListener {
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().clearFavouritesAdverts() }
            ) {
                launching()
            }
        }
    }

    override fun onViewAdvertDetails(model: AdvertModel) {
        sStartActivity<AdvertDetailsActivity>(
            ExtraConst.EXTRA_ADVERT_ID to model.advId
        )

    }

    override fun onToggleFavorite(model: AdvertModel) {
        ParaNormalProcess.silentProcessActivity(
            this,
            { UseCaseImpl().toggleFavoritesAdvert(model.advId) }
        ) {
        }
    }
}