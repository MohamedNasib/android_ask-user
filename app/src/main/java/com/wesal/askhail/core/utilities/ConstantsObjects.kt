package com.wesal.askhail.core.utilities

import android.content.Context
import com.wesal.askhail.R
import com.wesal.askhail.features.myAccount.AccountModel
import com.wesal.askhail.features.myAccount.AccountNavigate
import com.wesal.entities.models.KeyValueModel

object ConstantsObjects {

    fun getSortingList(context:Context): MutableList<KeyValueModel> {
        val list = mutableListOf<KeyValueModel>()
        list.add(KeyValueModel("latest", context.getString(R.string.sort_latest)))
        list.add(KeyValueModel("nearest", context.getString(R.string.sort_nearest)))
        list.add(KeyValueModel("most_viewed", context.getString(R.string.sort_most_viewed)))
        return list;
    }
    fun getHighlightPlace(context:Context): MutableList<KeyValueModel> {
        val list = mutableListOf<KeyValueModel>()
        list.add(KeyValueModel("main_page", context.getString(R.string.in_main_page)))
        list.add(KeyValueModel("inside_section", context.getString(R.string.inside_section)))
        return list;
    }
    fun getAccountList(context:Context): MutableList<AccountModel?> {
        val list = mutableListOf<AccountModel?>()
        list.add(AccountModel(R.drawable.ic_megaphone,context.getString(R.string.accmyads),AccountNavigate.NAV_MY_ADVERT))
        list.add(AccountModel(R.drawable.ic_shopping_bag_small,context.getString(R.string.accmyorders),AccountNavigate.NAV_MY_ORDERS))
        list.add(AccountModel(R.drawable.ic_right_cir,context.getString(R.string.accmypackages),AccountNavigate.NAV_MY_PACKAGES))
        list.add(AccountModel(R.drawable.ic_qus,context.getString(R.string.accmyquestions),AccountNavigate.NAV_MY_QUESTIONS))
        list.add(AccountModel(R.drawable.ic_my_saves,context.getString(R.string.acc_myfav),AccountNavigate.NAV_MY_FAV))
        list.add(AccountModel(R.drawable._ic_share,context.getString(R.string.shareapp),AccountNavigate.NAV_MY_SHARE))
        return list;
    }
}