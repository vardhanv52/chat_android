package com.android.chat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.chat.R
import com.android.chat.retrofit.APIManager
import com.android.chat.utils.Constants
import com.android.chat.utils.Helper
import com.android.chat.utils.PreferenceManager
import com.android.chat.utils.ResourceUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if(Helper.getToken() != null) {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }
        initialise()
        setListeners()
    }

    private fun initialise() {
        email?.background =
            ResourceUtil.getHollowStrokeDrawable(5, R.color.primaryColor)
        password_editText?.background =
            ResourceUtil.getHollowStrokeDrawable(5, R.color.primaryColor)
        sign_in_login?.background = ResourceUtil.getSolidRectDrawable(5, R.color.primaryColor)
    }

    private fun setListeners() {
        sign_in_login?.setOnClickListener {
            when {
                email?.text.toString().isEmpty() -> {
                    Helper.showToast(context, context.getString(R.string.email_empty))
                }
                password_editText?.text.toString().isEmpty() -> Helper.showToast(
                    context,
                    context.getString(R.string.password_empty)
                )
                !Helper.isValidEmailAddress(email?.text.toString()) -> Helper.showToast(
                    context, context.getString(R.string.email_invalid)
                )
                else ->
                    loginUser(
                        email.text.toString().trim(),
                        password_editText.text.toString().trim()
                    )
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Helper.displayProgressBar(context)
                val resp = APIManager.login(email, password)
                Helper.dismissProgressBar()
                if (resp.getBoolean("status")) {
                    PreferenceManager.saveUserData(
                        Constants.keyToken, resp.getString("token")
                    )
                    Helper.saveUserDTO(resp.getJSONObject("data"))
                    startActivity(Intent(context, MainActivity::class.java))
                    finish()
                }
                Helper.showToast(context, resp.getString("message"))
            } catch (ex: Exception) {
                ex.printStackTrace()
                Helper.dismissProgressBar()
                Helper.showToast(context, getString(R.string.went_wrong))
            }
        }
    }

}