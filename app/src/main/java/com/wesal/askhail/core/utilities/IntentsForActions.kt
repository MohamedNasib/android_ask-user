package com.wesal.askhail.core.utilities

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.wesal.askhail.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


object IntentsForActions {
    fun sharePhotoWithText(context: Activity, url: String?, txt: String) {

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val textToShare =
            txt + " " + "   https://play.google.com/store/apps/details?id=" + context.packageName
        url?.let {


            Picasso.get().load(url).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "image/*"
                    //i.setType("*/*");
                    i.putExtra(Intent.EXTRA_TEXT, textToShare)
                    i.putExtra(Intent.EXTRA_TITLE, "Share")

                    //                i.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(Intent.createChooser(i, "Share"))
                }

                override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
            })
        } ?: run {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            //i.setType("*/*");
            i.putExtra(Intent.EXTRA_TEXT, textToShare)
            i.putExtra(Intent.EXTRA_TITLE, "Share")
            //                i.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(i, "Share"))
        }

    }

    fun openWebSite(context: Activity, url: String?) {
        if (url != null && !url.isEmpty()) {
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                context.startActivity(i)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
    fun callNumber(context: Activity, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        context.startActivity(intent)
    }
    fun maps(context: Activity, lat: String,lng:String) {
        val urlAddress = "http://maps.google.com/maps?q=$lat,$lng(Location)&iwloc=A&hl=es"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress))
        context.startActivity(intent)
    }
    fun facebook(context: Activity, FACEBOOK_URL: String) {
        try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            val versionCode =
                context.packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            val path = "fb://facewebmodal/f?href=https://www.facebook.com/$FACEBOOK_URL"
            val x = Intent(Intent.ACTION_VIEW, Uri.parse(path))
            context.startActivity(x)

        } catch (e: Exception) {
            val y = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/$FACEBOOK_URL"))
            context.startActivity(y)

        }

    }
    fun facebookFullLink(context: Activity, FACEBOOK_URL: String) {
        try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            val versionCode =
                context.packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            val path = "fb://facewebmodal/f?href=$FACEBOOK_URL"
            val x = Intent(Intent.ACTION_VIEW, Uri.parse(path))
            context.startActivity(x)

        } catch (e: Exception) {
            val y = Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL))
            context.startActivity(y)

        }

    }
    fun twitter(context: Activity, twitter: String) {
        val intent: Intent
        val FullUrl = "https://twitter.com/#!/$twitter"
        try {
            // get the Twitter app if possible
            context.packageManager.getPackageInfo("com.twitter.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=$twitter"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } catch (e: Exception) {
            // no Twitter app, revert to browser
            val x = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
            context.startActivity(x)

        }

    }
    fun twitterFullLink(context: Activity, twitter: String) {
        val intent: Intent
        val FullUrl = twitter
        try {
            // get the Twitter app if possible
            context.packageManager.getPackageInfo("com.twitter.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } catch (e: Exception) {
            // no Twitter app, revert to browser
            val x = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
            context.startActivity(x)

        }

    }
    fun instgram(context: Activity, insta: String) {
        val intent: Intent
        val FullUrl = "http://instagram.com/$insta"

        try {
            // get the Twitter app if possible
            context.packageManager.getPackageInfo("com.instagram.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$insta"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } catch (e: Exception) {
            // no Twitter app, revert to browser
            try {
                val x = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
                context.startActivity(x)
            }catch (e:Exception){
                e.printStackTrace()
            }


        }

    }
    fun instgramFullLink(context: Activity, insta: String) {
        val intent: Intent
        val FullUrl = insta

        try {
            // get the Twitter app if possible
            context.packageManager.getPackageInfo("com.instagram.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } catch (e: Exception) {
            // no Twitter app, revert to browser
            try {
                val x = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
                context.startActivity(x)
            }catch (e:Exception){
                e.printStackTrace()
            }


        }

    }
    fun tiktok(context: Activity, tiktok: String) {
        val intent: Intent
        val FullUrl = "http://tiktok.com/$tiktok"

        try {
            // get the Twitter app if possible
            context.packageManager.getPackageInfo("com.tiktok.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://tiktok.com/_u/$tiktok"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } catch (e: Exception) {
            // no Twitter app, revert to browser
            try {
                val x = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
                context.startActivity(x)
            }catch (e:Exception){
                e.printStackTrace()
            }


        }

    }
    fun tiktokFullLink(context: Activity, tiktok: String) {
        val intent: Intent
        val FullUrl = tiktok

        try {
            // get the Twitter app if possible
            context.packageManager.getPackageInfo("com.tiktok.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } catch (e: Exception) {
            // no Twitter app, revert to browser
            try {
                val x = Intent(Intent.ACTION_VIEW, Uri.parse(FullUrl))
                context.startActivity(x)
            }catch (e:Exception){
                e.printStackTrace()
            }


        }

    }
    fun copyText(activity: Activity, text: String) {
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Angizha", text)
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity, R.string.copied, Toast.LENGTH_SHORT).show()
        }
    }
    fun shareText(activity: Activity, text: String) {
        val intent2: Intent = Intent()
        intent2.action = Intent.ACTION_SEND
        intent2.type = "text/plain"
        intent2.putExtra(Intent.EXTRA_TEXT, text)
        activity.startActivity(Intent.createChooser(intent2, "Share via"))
    }
    fun snapChat(context: Context, snapchatId: String) {
        val fullPath = "https://snapchat.com/add/$snapchatId"
        try {
            val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
            intent.setPackage("com.snapchat.android")
            context.startActivity(intent)
        } catch (e: Exception) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(fullPath)
                    )
            )
        }
    }
    fun snapChatFullLink(context: Context, snapchatId: String) {
        val fullPath = snapchatId
        try {
            val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
            intent.setPackage("com.snapchat.android")
            context.startActivity(intent)
        } catch (e: Exception) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(fullPath)
                    )
            )
        }
    }
    fun whatsApp(context: Activity, whats: String) {
        var whats = whats
        whats = "" + whats
        if (whats.startsWith("00")) {
            whats = whats.substring(2)
        }
        Log.e("whats", whats)
        try {
            whats = whats.replace(" ", "").replace("+", "")
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(whats) + "@s.whatsapp.net")
            context.startActivity(sendIntent)

        } catch (e: Exception) {
            Toast.makeText(context, R.string.there_error, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "ERROR_OPEN_MESSANGER$e")
        }

    }
    fun getFileThumb(activity: Activity, path: String): File {
        val bitmap =
            ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND)

        val f = File(activity.cacheDir, Date().time.toString() + "123")
        try {
            f.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return f

    }


}