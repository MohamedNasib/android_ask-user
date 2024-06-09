package com.wesal.askhail.features.myAccount

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.ConstantsObjects
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityMyAccountBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.editInfo.EditInfoActivity
import com.wesal.askhail.features.favouritesAdvert.FavouritesAdvertActivity
import com.wesal.askhail.features.myAdverts.MyAdvertsActivity
import com.wesal.askhail.features.myInfo.MyInfoActivity
import com.wesal.askhail.features.myOrders.MyOrdersActivity
import com.wesal.askhail.features.myPackageSteps.myPackage.MyPackageActivity
import com.wesal.askhail.features.myQuestion.MyQuestionsActivity
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.domain.useCases.UseCaseImpl
import kotlinx.coroutines.launch
import timber.log.Timber

class MyAccountActivity : BaseActivity(), AccountAdapter.OnAccount {
    lateinit var binding: ActivityMyAccountBinding
    override fun layoutResource(): Int = R.layout.activity_my_account


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.my_account))
        setUpMyAccount()
        clickers()
        binding.btnBack.setOnClickListener{finish()}
        binding.edit.setOnClickListener {
            launching(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Use a color from your resources
            val window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        }
        launching(false)
    }

    private fun launching(withRediredct: Boolean) {

        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getUserInfo() }
        ) {
            if (it != null) {
                if (withRediredct) {
                    sStartActivity<EditInfoActivity>(
                        ExtraConst.EXTRA_MODEL to it
                    )
                } else {
                    binding.txtUserName.setText(it.advertiserName)
                    binding.txtUserEmail.setText(it.advertiserEmail)
                    binding.txtUserPhone.setText(it.advertiserMobile)
                }
            }else{
                Timber.e("NULLLLLLLLLLLLLL")
            }
        }
    }

    private fun setUpMyAccount() {
        binding.rvMyAccount.layoutManager = LinearLayoutManager(this)
        val accountList = ConstantsObjects.getAccountList(this)
        val adapter = AccountAdapter(accountList, this@MyAccountActivity)
        binding.rvMyAccount.adapter = adapter
    }

    private fun clickers() {
        binding.tvLogOut.setOnClickListener {
            UseCaseImpl().clearUserData()
            sStartActivityWithClear<SplashActivity>()
            scopeIO.launch {
                UseCaseImpl().logOut()
            }
        }

    }

    override fun onAccount(model: AccountModel) {
        when (model.target) {
            AccountNavigate.NAV_MY_ADVERT -> {
                sStartActivity<MyAdvertsActivity>()
            }
            AccountNavigate.NAV_MY_ORDERS -> {
                sStartActivity<MyOrdersActivity>()
            }
            AccountNavigate.NAV_MY_PACKAGES -> {
                sStartActivity<MyPackageActivity>()
            }
            AccountNavigate.NAV_MY_QUESTIONS -> {
                sStartActivity<MyQuestionsActivity>()
            }
            AccountNavigate.NAV_MY_FAV -> {
                sStartActivity<FavouritesAdvertActivity>()
            }
            AccountNavigate.NAV_MY_SHARE -> {
                shareApp()
            }
        }

    }


    private fun shareApp() {
        IntentsForActions.sharePhotoWithText(
            this,
            null,
            getString(R.string.share_app)
        )
    }


}