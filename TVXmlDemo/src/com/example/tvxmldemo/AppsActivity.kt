package com.example.tvxmldemo

import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AppsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps)

        val gridView = findViewById<GridView>(R.id.grid_apps)
        val apps = getAllInstalledApps()
        gridView.adapter = AppAdapter(apps)
    }

    /**
     * 核心逻辑：拦截遥控器按键
     * 无论焦点在哪里，只要按了确认键，就尝试打开当前选中的应用
     */
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val keyCode = event.keyCode
            // 兼容各种遥控器的确认键 (中间键、回车、小键盘回车)
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                keyCode == KeyEvent.KEYCODE_ENTER ||
                keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {

                val focusedView = currentFocus
                // 如果当前焦点的 View 身上藏着 App 信息，就启动它
                if (focusedView?.tag is ResolveInfo) {
                    launchApp(focusedView.tag as ResolveInfo)
                    return true // 拦截事件，防止重复处理
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun launchApp(appInfo: ResolveInfo) {
        val packageName = appInfo.activityInfo.packageName
        try {
            // 如果是打开自己，就回到主页
            if (packageName == applicationContext.packageName) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return
            }

            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(launchIntent)
            } else {
                Toast.makeText(this, "无法启动", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAllInstalledApps(): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(mainIntent, 0)
    }

    inner class AppAdapter(private val apps: List<ResolveInfo>) : BaseAdapter() {
        override fun getCount(): Int = apps.size
        override fun getItem(position: Int): Any = apps[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val context = parent?.context

            val layout = if (convertView == null) {
                LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                    layoutParams = android.widget.AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 300
                    )
                    setPadding(16, 16, 16, 16)

                    // 样式与焦点设置
                    setBackgroundResource(R.drawable.bg_btn_selected)
                    isFocusable = true
                    isClickable = true
                    descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                }
            } else {
                convertView as LinearLayout
            }

            layout.removeAllViews()
            val appInfo = apps[position]

            // ★ 将 App 信息绑定到 View 上
            layout.tag = appInfo

            val iconView = ImageView(context)
            val icon: Drawable = appInfo.loadIcon(packageManager)
            iconView.setImageDrawable(icon)
            iconView.layoutParams = LinearLayout.LayoutParams(120, 120)
            layout.addView(iconView)

            val nameView = TextView(context)
            nameView.text = appInfo.loadLabel(packageManager)
            nameView.setTextColor(Color.WHITE)
            nameView.textSize = 14f
            nameView.gravity = Gravity.CENTER
            nameView.maxLines = 1
            nameView.ellipsize = android.text.TextUtils.TruncateAt.END
            val textParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textParams.topMargin = 12
            nameView.layoutParams = textParams
            layout.addView(nameView)

            // 保留鼠标点击支持
            layout.setOnClickListener {
                launchApp(appInfo)
            }

            return layout
        }
    }
}