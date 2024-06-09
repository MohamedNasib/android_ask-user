package com.wesal.askhail.features.notifications

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.customViews.CustomLinearLayoutManager
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityNotificationsBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.advertHighlightStatus.AdvertHighlightStatusActivity
import com.wesal.askhail.features.askHailDetails.AskHailDetailsActivity
import com.wesal.askhail.features.myPackageSteps.myPackage.MyPackageActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.NotificationsModel


class NotificationsActivity : BaseActivity(), NotificationsAdapter.OnNotification {
    lateinit var binding: ActivityNotificationsBinding
    private  var currentList: List<NotificationsModel>?=null

    override fun layoutResource(): Int = R.layout.activity_notifications
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.notifications))
        launching()
        clickers()
    }

    private fun clickers() {

        binding.tvDeleteAll.setOnClickListener {
            AppDialogs.redActionDialog(
                this,
                getString(R.string.remove_all_notifications),
                getString(R.string.delete)
            ){
                ParaNormalProcess.loadingProcessActivity(
                    this@NotificationsActivity,
                    {UseCaseImpl().removeAllNotifications()}
                ){
                    launching()
                }
            }.show()
        }
    }

    private fun launching() {
        binding.tvDeleteAll.gone()
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getNotifications() },
            R.drawable.ic_status_empty_notifications,
            R.string.empty_notifications,
            false
        ) {

            if (it != null) {
                currentList = it
                updateUi(it)
            }
        }

    }

    private fun updateUi(it: List<NotificationsModel>) {
        binding.rvNotifications.layoutManager = CustomLinearLayoutManager(this)
        val adapter = NotificationsAdapter(it.toMutableList(), this)
        binding.rvNotifications.adapter = adapter
        if (it.isEmpty()){
            binding.tvDeleteAll.gone()
        }else{
            binding.tvDeleteAll.visible()
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvNotifications)
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            Log.e("dsf","sdf")
            val position = viewHolder.adapterPosition
            if (currentList!=null){
                val notificationsModel = currentList!![position]
                deleteNotification(notificationsModel)
            }

        }
    }

    private fun deleteNotification(notificationsModel: NotificationsModel) {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().removeOneNotification(notificationsModel.notifyId)}
        ){
            launching()
        }
    }

    override fun onNotification(model: NotificationsModel) {
        when (model.notifyType) {
            "adv" -> {
                sStartActivity<AdvertDetailsActivity>(ExtraConst.EXTRA_ADVERT_ID to model.notifyTypeId)
            }
            "special_subscription" -> {
                sStartActivity<AdvertHighlightStatusActivity>(ExtraConst.EXTRA_ADVERTER_ID to model.notifyTypeId)
            }
            "main_subscription" -> {
                sStartActivity<MyPackageActivity>()
            }
            "public" -> {
            }
            "question" -> {
                sStartActivity<AskHailDetailsActivity>(ExtraConst.EXTRA_ASK_ID to model.notifyTypeId)
            }
        }
    }
}