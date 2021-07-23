package com.android.chat.activities

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.android.chat.R
import com.android.chat.dtos.UserDTO
import com.android.chat.retrofit.APIManager
import com.android.chat.utils.Constants
import com.android.chat.utils.Helper
import com.android.chat.utils.ResourceUtil
import kotlinx.android.synthetic.main.activity_add_update_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddUpdateUserActivity : AppCompatActivity() {
    private val context: Context = this
    private var isUpdated = false
    private var user: UserDTO? = null
    private val resultCode = 568

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_user)
        user = intent.getSerializableExtra("user") as UserDTO?
        initialise()
        setListeners()
        if (user != null) {
            title_tv?.text = getString(R.string.update_user)
            setData()
        }
    }

    private fun initialise() {
        name_et?.background = ResourceUtil.getHollowStrokeDrawable(5, R.color.white)
        email_et?.background = ResourceUtil.getHollowStrokeDrawable(5, R.color.white)
        mobile_et?.background = ResourceUtil.getHollowStrokeDrawable(5, R.color.white)
        pwd_et?.background = ResourceUtil.getHollowStrokeDrawable(5, R.color.white)
        roles_spinner?.background = ResourceUtil.getHollowStrokeDrawable(5, R.color.white)
        val array = resources.getStringArray(R.array.array_roles)
        val adapter = ArrayAdapter(context, R.layout.item_spinner, R.id.title_tv, array)
        roles_spinner?.adapter = adapter
        submit?.background = ResourceUtil.getSolidRectDrawable(10, R.color.btn_color)
    }

    private fun setListeners() {
        submit?.setOnClickListener {
            val name = name_et?.text.toString().trim()
            val email = email_et?.text.toString().trim()
            val mobile = mobile_et?.text.toString().trim()
            val pwd = pwd_et?.text.toString().trim()
            if (name.isEmpty())
                Helper.showToast(context, getString(R.string.check_name))
            else if (email.isEmpty() || !Helper.isValidEmailAddress(email))
                Helper.showToast(context, getString(R.string.check_email))
            else if (mobile.isEmpty())
                Helper.showToast(context, getString(R.string.check_mobile))
            else if (user == null && pwd.isEmpty())
                Helper.showToast(context, getString(R.string.check_password))
            else if (roles_spinner?.selectedItemPosition == 0)
                Helper.showToast(context, getString(R.string.check_role))
            else {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        Helper.displayProgressBar(context)
                        val role = if (roles_spinner?.selectedItemPosition == 1)
                            Constants.ROLE_USER
                        else
                            Constants.ROLE_ADMIN
                        val resp = if (user == null)
                            APIManager.createUser(name, email, mobile, role, pwd)
                        else {
                            val map = HashMap<String, Any>()
                            map["user_id"] = user?.id!!
                            map["name"] = name
                            map["email"] = email
                            map["mobile"] = mobile
                            map["password"] = pwd
                            map["role"] = role
                            map["is_active"] = rb_enable?.isChecked ?: true
                            APIManager.updateUser(map)
                        }
                        Helper.dismissProgressBar()
                        if (resp.getBoolean("status")) {
                            isUpdated = true
                            if (user == null) {
                                name_et?.setText("")
                                email_et?.setText("")
                                mobile_et?.setText("")
                                pwd_et?.setText("")
                                roles_spinner?.setSelection(0)
                                rb_enable?.isChecked = true
                            }
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

        back?.setOnClickListener {
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (isUpdated)
            setResult(resultCode)
        super.onBackPressed()
    }

    private fun setData() {
        name_et?.setText(user?.name)
        email_et?.setText(user?.email)
        mobile_et?.setText(user?.mobile)
        if (user?.role == Constants.ROLE_ADMIN)
            roles_spinner?.setSelection(2)
        else
            roles_spinner?.setSelection(1)
        if (user?.isActive == true)
            rb_enable?.isEnabled = true
        else
            rb_disable?.isEnabled = true
    }
}