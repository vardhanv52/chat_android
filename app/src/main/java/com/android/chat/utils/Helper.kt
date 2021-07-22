package com.android.chat.utils

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.chat.R
import com.android.chat.dtos.UserDTO
import com.android.chat.interfaces.IDialogActions
import com.android.chat.utils.Chat.Companion.context
import com.google.gson.Gson
import kotlinx.android.synthetic.main.dialog_confirmation.*
import org.json.JSONObject
import kotlin.math.roundToInt

object Helper {
    private var progressDialog: ProgressDialog? = null
    private var toast: Toast? = null

    fun isNetworkAvailable(): Boolean {
        var result = false
        val cm =
            Chat.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun showToast(context: Context, msg: String?) {
        toast?.cancel()
        toast = Toast.makeText(context, msg ?: context.getString(R.string.n_a), Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }

    fun log(msg: String?) {
        Log.d(Chat.tag, "$msg")
    }

    fun isValidEmailAddress(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun saveUserDTO(json: JSONObject) {
        PreferenceManager.saveUserData(Constants.keyUserDTO, json.toString())
    }

    fun getUserDTO(): UserDTO? {
        return Gson().fromJson(
            PreferenceManager.getUserStringData(Constants.keyUserDTO), UserDTO::class.java
        )
    }

    fun getToken(): String? {
        return PreferenceManager.getUserStringData(Constants.keyToken)
    }

    fun getHeaderAuthorization(): String {
        return "Bearer ${
            PreferenceManager.getUserSharedPreferences()?.getString(Constants.keyToken, "")
        }"
    }

    fun displayProgressBar(context: Context) {
        dismissProgressBar()
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(context.getString(R.string.please_wait))
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun dismissProgressBar() {
        progressDialog?.dismiss()
    }

    fun showConfirmationDialog(context: Context, msg: String, actions: IDialogActions) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_confirmation)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.are_you_sure_subtext?.text = msg
        dialog.no_btn.background = ResourceUtil.getSolidRectDrawable(10, R.color.btn_color)
        dialog.yes_btn.background = ResourceUtil.getSolidRectDrawable(
            10, R.color.white
        )
        dialog.yes_btn?.setOnClickListener {
            dialog.dismiss()
            Handler(Looper.getMainLooper()).postDelayed({
                actions.onBtn1Clicked()
            }, 400)
        }
        dialog.no_btn?.setOnClickListener {
            dialog.dismiss()
            Handler(Looper.getMainLooper()).postDelayed({
                actions.onBtn2Clicked()
            }, 400)
        }
        try {
            dialog.show()
        } catch (err: Exception) {
            err.printStackTrace()
        }
    }

    fun generateCircularBitmap(
        context: Context, circleColor: String, diameterDP: Float,
        text: String?, textSizeValue: Double
    ): Bitmap? {
        val textColor = ContextCompat.getColor(context, R.color.btn_color)
        val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
        val diameterPixels = diameterDP * (metrics.densityDpi / 160f)
        val radiusPixels = diameterPixels / 2
        // Create the bitmap
        val output = Bitmap.createBitmap(
            diameterPixels.toInt(), diameterPixels.toInt(),
            Bitmap.Config.ARGB_8888
        )
        // Create the canvas to draw on
        val canvas = Canvas(output)
        canvas.drawARGB(0, 0, 0, 0)
        // Draw the circle
        val paintC = Paint()
        paintC.isAntiAlias = true
        paintC.color = Color.parseColor(circleColor)
        canvas.drawCircle(radiusPixels, radiusPixels, radiusPixels, paintC)
        // Draw the text
        if (text != null && text.isNotEmpty()) {
            val paintT = Paint()
            paintT.color = textColor
            paintT.isAntiAlias = true
            paintT.textSize = radiusPixels * textSizeValue.toFloat()
            val typeFace =
                ResourcesCompat.getFont(context, R.font.now_medium)
            paintT.typeface = typeFace
            val textBounds = Rect()
            paintT.getTextBounds(text, 0, text.length, textBounds)
            canvas.drawText(
                text,
                radiusPixels - textBounds.exactCenterX(),
                radiusPixels - textBounds.exactCenterY(),
                paintT
            )
        }
        return output
    }
}