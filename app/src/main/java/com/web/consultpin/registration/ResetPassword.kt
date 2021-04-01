package com.web.consultpin.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.app.dialogsnpickers.DialogCallBacks
import com.web.consultpin.R
import com.web.consultpin.Utilclass
import com.web.consultpin.main.BaseActivity
import org.json.JSONObject

class ResetPassword : BaseActivity() {

    lateinit var email: String;
    lateinit var Otp: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initiateObj()
        supportActionBar!!.hide()
        init();
    }

    fun init() {
        var et_password = findViewById<EditText>(R.id.et_password);
        var et_conf_password = findViewById<EditText>(R.id.et_conf_password);
        var et_oldpaswword = findViewById<EditText>(R.id.et_oldpaswword);
        var tv_submit = findViewById<TextView>(R.id.tv_submit);
        var img_eye_password = findViewById<ImageView>(R.id.img_eye_password);
        var img_eye_conf_password = findViewById<ImageView>(R.id.img_eye_conf_password);
        var img_eye_oldpassword = findViewById<ImageView>(R.id.img_eye_oldpassword);

        Otp = intent?.getStringExtra(Utilclass.otp).toString();
        email = intent?.getStringExtra(Utilclass.email).toString();


        img_eye_oldpassword.setTag("0")
        img_eye_oldpassword.setOnClickListener(View.OnClickListener { v ->
            if (v.tag == "0") {
                img_eye_oldpassword.setTag("1")
                hideShowPassword(1, et_oldpaswword, img_eye_oldpassword)
            } else {
                img_eye_oldpassword.setTag("0")
                hideShowPassword(0, et_oldpaswword, img_eye_oldpassword)
            }
        })




        img_eye_password.setTag("0")
        img_eye_password.setOnClickListener(View.OnClickListener { v ->
            if (v.tag == "0") {
                img_eye_password.setTag("1")
                hideShowPassword(1, et_password, img_eye_password)
            } else {
                img_eye_password.setTag("0")
                hideShowPassword(0, et_password, img_eye_password)
            }
        })

        img_eye_conf_password.setTag("0")
        img_eye_conf_password.setOnClickListener(View.OnClickListener { v ->
            if (v.tag == "0") {
                img_eye_conf_password.setTag("1")
                hideShowPassword(1, et_conf_password, img_eye_conf_password)
            } else {
                img_eye_conf_password.setTag("0")
                hideShowPassword(0, et_conf_password, img_eye_conf_password)
            }
        })





        tv_submit.setOnClickListener(View.OnClickListener {
            addClickEventEffet(tv_submit)

            if (et_oldpaswword.text.toString().length == 0) {
                alertDialogs.alertDialog(this, resources.getString(R.string.Required), resources.getString(R.string.enter_old_password), resources.getString(R.string.ok), "", DialogCallBacks { })

            }


          else  if (et_password.text.toString().length == 0) {
                alertDialogs.alertDialog(this, resources.getString(R.string.Required), resources.getString(R.string.enter_password), resources.getString(R.string.ok), "", DialogCallBacks { })
            }

            else if(!passwordValidation(et_password.text.toString()))
            {
                alertDialogs.alertDialog(this, resources.getString(R.string.Required), resources.getString(R.string.passwordformat), resources.getString(R.string.ok), "", DialogCallBacks { })
            }
            else if (et_conf_password.text.toString().length == 0) {
                alertDialogs.alertDialog(this, resources.getString(R.string.Required), resources.getString(R.string.enter_your_new_password), resources.getString(R.string.ok), "", DialogCallBacks { })
            } else if (!et_conf_password.text.toString().equals(et_password.text.toString())) {
                alertDialogs.alertDialog(this, resources.getString(R.string.Invalid), resources.getString(R.string.password_invalid), resources.getString(R.string.ok), "", DialogCallBacks { })
            } else {
                callResetPassword(et_oldpaswword.text.toString(), et_password.text.toString(), et_conf_password.text.toString());
            }

        })

    }

    fun callResetPassword(oldPassword: String, password: String, confirmPass: String) {

        var map = LinkedHashMap<String, String>();
        map.put("old_password", oldPassword);
        map.put("new_password", password);
        map.put("confirm_new_password", confirmPass);
        map.put("user_id", getRestParamsName(Utilclass.user_id));
        map["device_type"] = "android"
        map["device_token"] = getDeviceToken() + ""

        System.out.println("Before---" + map)



        val obj: MutableMap<String, String> = java.util.HashMap()
        obj["token"] = getRestParamsName(Utilclass.token) + ""

        serverHandler.sendToServer(this, getApiUrl() + "updatepassword", map, 0, obj, 20000, R.layout.progressbar) { dta, respons ->
            try {
                val jsonObject = JSONObject(dta)
                if (jsonObject.getBoolean("status"))
                {
                    alertDialogs.alertDialog(this@ResetPassword, resources.getString(R.string.Response), jsonObject.getString("msg"), resources.getString(R.string.ok), "") {
                        var intent= Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    alertDialogs.alertDialog(this@ResetPassword, resources.getString(R.string.Response), jsonObject.getString("msg"), resources.getString(R.string.ok), "") { }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }


    }
}