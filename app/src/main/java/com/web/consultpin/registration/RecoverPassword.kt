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

class RecoverPassword : BaseActivity() {

    lateinit var email: String;
    lateinit var Otp: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recover_password)
        supportActionBar?.hide();
        initiateObj()
        init();
    }

    fun init() {
        var et_password = findViewById<EditText>(R.id.et_password);
        var et_conf_password = findViewById<EditText>(R.id.et_conf_password);
        var tv_submit = findViewById<TextView>(R.id.tv_submit);
        var img_eye_password = findViewById<ImageView>(R.id.img_eye_password);
        var img_eye_conf_password = findViewById<ImageView>(R.id.img_eye_conf_password);

        Otp = intent?.getStringExtra(Utilclass.otp).toString();
        email = intent?.getStringExtra(Utilclass.email).toString();

        img_eye_password.setTag("0")
        img_eye_password.setOnClickListener(View.OnClickListener { v ->
            if (v.tag == "0") {
                img_eye_password.setTag("1")
                hideShowPassword(1, et_password,img_eye_password)
            } else {
                img_eye_password.setTag("0")
                hideShowPassword(0, et_password,img_eye_password)
            }
        })

        img_eye_conf_password.setTag("0")
        img_eye_conf_password.setOnClickListener(View.OnClickListener { v ->
            if (v.tag == "0") {
                img_eye_conf_password.setTag("1")
                hideShowPassword(1, et_conf_password,img_eye_conf_password)
            } else {
                img_eye_conf_password.setTag("0")
                hideShowPassword(0, et_conf_password,img_eye_conf_password)
            }
        })





        tv_submit.setOnClickListener(View.OnClickListener {
            addClickEventEffet(tv_submit)
            if (et_password.text.toString().length == 0) {
                alertDialogs.alertDialog(this, resources.getString(R.string.Required), resources.getString(R.string.enter_password), resources.getString(R.string.ok), "", DialogCallBacks { })
            } else if (et_conf_password.text.toString().length == 0) {
                alertDialogs.alertDialog(this, resources.getString(R.string.Required), resources.getString(R.string.enter_your_new_password), resources.getString(R.string.ok), "", DialogCallBacks { })
            } else if (!et_conf_password.text.toString().equals(et_password.text.toString())) {
                alertDialogs.alertDialog(this, resources.getString(R.string.Invalid), resources.getString(R.string.password_invalid), resources.getString(R.string.ok), "", DialogCallBacks { })
            } else {
                callResetPassword(et_password.text.toString(), et_conf_password.text.toString());
            }

        })

    }

    fun callResetPassword(password: String, confirmPass: String) {

        var map = LinkedHashMap<String, String>();
        map.put("password", password);
        map.put("confirm_password", confirmPass);
        map.put("email", email);
        map.put("otp", Otp);
        map["device_type"] = "android"
        map["device_token"] = getDeviceToken() + ""

        System.out.println("Before---"+map)

        val obj: MutableMap<String, String> = HashMap()
//        obj["X-API-KEY"] = getXapiKey()
//        obj["Token"] = savePreferences.reterivePreference(this, "session_token").toString() + ""
//        obj["uid"] = getRestParamsName("uid")
        serverHandler.sendToServer(this, getApiUrl() + "reset", map, 0, obj, 20000, R.layout.progressbar) { dta, respons ->
            try {
                val jsonObject = JSONObject(dta)
                if (jsonObject.getBoolean("status"))
                {
                    alertDialogs.alertDialog(this@RecoverPassword, resources.getString(R.string.Response), jsonObject.getString("msg"), resources.getString(R.string.ok), "") {
                        var intent= Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    alertDialogs.alertDialog(this@RecoverPassword, resources.getString(R.string.Response), jsonObject.getString("msg"), resources.getString(R.string.ok), "") { }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }


    }
}
