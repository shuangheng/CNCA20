package com.cnc.tom_macos.cnc_a20

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.widget.Toast
import java.lang.System.exit


class MainActivity : AppCompatActivity() {

    // 委托属性，Preference把取值和存值的操作代理给variable，我们对userId的赋值和取值最终是操作的Preference得setValue和getValue函数。

    private var mapTextView = mutableMapOf<String, TextView>()

    var viewNub = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        jumpActivity2(DailySheetInputActivity())
        //setListColor(resources.getColor(R.color.colorPrimaryDark2))


        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "计算完成！", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            Toast.makeText(
                applicationContext, "计算完成！",
                Toast.LENGTH_SHORT
            ).show()

            for (i in 0..parent_view.childCount) {
                val resultNum = toInt(mapTextView["$i-2"]) * toInt(mapTextView["$i-4"]) + toInt(mapTextView["$i-5"]) - toInt(mapTextView["$i-6"])// - toInt(mapTextView["$i-7"])
                val biaoChan = 43200.0 / toInt(mapTextView["$i-1"]).toFloat()
                val resultH = (biaoChan - resultNum.toFloat() - toInt(mapTextView["$i-7"])) * toInt(mapTextView["$i-1"]).toFloat() / 3600.0
                val result = "$resultNum/${formatDouble(resultH.toString())}"

                //if (mapTextView["$i-3"] != null) {
                mapTextView["$i-3"]?.text = formatDouble(biaoChan.toString())
                //}
                //if (mapTextView["$i-8"] != null) {
                mapTextView["$i-8"]?.text = result
                //}
            }
        }
    }

    private fun setListColor(any: Int) {
        for (i in 0..(parent_view.childCount-1)) {
            when(i % 2 == 0) {
                true -> parent_view.getChildAt(i+1).setBackgroundColor(any)

            }
        }
    }

    private fun jumpActivity2(activity: AppCompatActivity) {
        val isEmpty by Preference(this, "0-0", "")
        if (isEmpty.isEmpty()) {
            val intent: Intent = Intent(this, activity::class.java)
            startActivity(intent)
        }
    }

    private fun jumpActivity(activity: AppCompatActivity) {
        val intent: Intent = Intent(this, activity::class.java)
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()

        mapAddTextView(parent_view)
        setListColor(ContextCompat.getColor(this, R.color.colorPrimaryDark2))
        setViewText()

    }

    override fun onPause() {
        super.onPause()

        for ((key, view) in mapTextView) {
            var str by Preference(this, key, "")
            /*when (key.last().toString()) {
                "0", "1", "2", "3" -> str = view.getText().toString()
            }*/
            str = view.getText().toString()

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_add -> jumpActivity(DailySheetInputActivity())
            R.id.action_delete -> delect()
        }

        return when (item.itemId) {
            R.id.action_add -> true
            R.id.action_delete -> true
            else -> super.onOptionsItemSelected(item)
        }
    }



    //保存点击返回键的时间
    private var exitTime: Long = 0
    //双击返回键退出
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(
                    applicationContext, "再按一次退出程序",
                    Toast.LENGTH_SHORT
                ).show()
                exitTime = System.currentTimeMillis()
            } else {
                return super.onKeyDown(keyCode, event)
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun setViewText() {
        for ((key, view) in mapTextView) {
            val str by Preference(this, key, "")
            view.text = str
        }
    }

    private fun delect() {
        for ((key, view) in mapTextView) {
            val str by Preference(this, key, "")
            when(key.last().toString()) {
                "4", "5", "6", "7", "8" -> view.text = ""
            }
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

    //str 转换成Int
    private fun toInt(textView: TextView?): Int {
        return if (textView != null) {
            val str = textView.text.toString()
            when (str) {
                "" -> 0
                else -> str.toInt()
            }
        } else {
            0
        }

    }

    //当str是无限循环小数时截取字符串
    private fun formatDouble(str: String): String {
        return if (str.length > 6 && str.contains(".")) {
            str.substring(0, str.indexOf(".") + 2)
        } else {
            str
        }
    }
}
