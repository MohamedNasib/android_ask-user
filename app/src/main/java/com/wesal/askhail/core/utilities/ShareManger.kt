package com.wesal.askhail.core.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.wesal.askhail.core.presentationEnums.AppContentsType

object ShareManger {
    private const val SCHEME = "https"
    private const val AUTHORITY = "askhail.page.link"
    const val QUERY_ADVERT = "advert"
    const val QUERY_ORDER = "order"
    const val QUERY_QUESTION = "question"
    private const val TARGET_APP_ID = "com.wesal.askhail"

    fun share(
        context: Activity,
        type: AppContentsType,
        relatedId: Int,
        title: String
    ) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(buildSharingUri(type, relatedId))
            .setDomainUriPrefix("$SCHEME://$AUTHORITY/")
            .setAndroidParameters(AndroidParameters.Builder(TARGET_APP_ID).build())
            .setIosParameters(DynamicLink.IosParameters.Builder(TARGET_APP_ID).build())
            .buildShortDynamicLink()
            .addOnCompleteListener { task: Task<ShortDynamicLink?> ->
                if (task.isSuccessful) {
                    if (task.result != null) {
                        // share
                        val textToShare = "$title \n ${task.result?.shortLink?.toString()}"
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
                        shareIntent.type = "text/plain"
                        context.startActivity(shareIntent)

                    }
                } else {
                }
            }
    }

    private fun buildSharingUri(
        type: AppContentsType,
        relatedId: Int
    ): Uri {
        val path = Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
        when (type) {
            AppContentsType.ADVERT -> path.appendPath(QUERY_ADVERT)
            AppContentsType.ORDER -> path.appendPath(QUERY_ORDER)
            AppContentsType.QUESTION -> path.appendPath(QUERY_QUESTION)
        }
        path.appendPath("$relatedId")
        return path.build()
    }
}