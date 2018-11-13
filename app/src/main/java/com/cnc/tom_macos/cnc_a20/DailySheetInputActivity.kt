package com.cnc.tom_macos.cnc_a20

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dailysheetinput.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_dailysheetinput.*
import kotlinx.android.synthetic.main.content_main.*

class DailySheetInputActivity : AppCompatActivity() {

    // 委托属性，Preference把取值和存值的操作代理给variable，我们对userId的赋值和取值最终是操作的Preference得setValue和getValue函数。

    private var mapTextView = mutableMapOf<String, TextView>()

    private var viewNub = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dailysheetinput)
        /*setSupportActionBar(toolbar)
        toolbar.title = this.resources.getString(R.string.input)*/
//        actionBar.title = this.resources.getString(R.string.input)

        mapAddTextView(parent_jitai_daily_input_view)
        setViewText()

        fab_dailsheetinput.setOnClickListener { view ->
            /*Snackbar.make(view, "已输入！", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            Toast.makeText(
                applicationContext, "已输入！",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            //onKeyDown(KeyEvent.KEYCODE_BACK, null)
            /*onPause()
            SystemClock.sleep(1000)
            System.exit(0)*/
        }

    }


    override fun onPause() {
        super.onPause()

        for ((key, view) in mapTextView) {
            var str by Preference(this, key, "")
            str = view.getText().toString()
        }

    }





    private fun mapAddTextView(viewGroups: ViewGroup) {
        for (i in 0 until viewGroups.childCount) {
            val viewGroup = viewGroups.getChildAt(i)
            when (viewGroup) {
                is ViewGroup -> addTextView(i, viewGroup)
            }
            viewNub = 0
            //Log.w("father-view", "$i")
        }
    }



    private fun addTextView(key: Int, viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is ViewGroup) {
                addTextView(key, view)
                //Log.w("father-view-2", "$i")
            } else {
                when (view) {
                    is TextInputEditText -> {
                        mapTextView.put("$key-$viewNub", view)
                        viewNub++
                    }
                    is MyTextView -> {
                        mapTextView.put("$key-$viewNub", view)
                        viewNub++
                    }
                }
                val sh by Preference(this, "$key-$i", "")
                //Log.w("addTextView", "$key-$viewNub")
                //Log.w("prefrence========-===", "$sh-----")
            }
        }

    }

    private fun setViewText() {
        for ((key, view) in mapTextView) {
            val str by Preference(this, key, "")
            view.text = str
        }
    }


}
