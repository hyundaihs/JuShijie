package com.android.shuizu.myutillibrary.widget

import android.content.Context
import android.os.Message
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import java.util.*
import android.widget.Toast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/9/009.
 */
class YzmButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        var countTimer: Timer? = null
        var countTask: CountTask? = null
        var isRun = false
        var time = 60L
    }

    class CountTask(var text: AppCompatTextView) : TimerTask() {
        override fun run() {
            doAsync {
                uiThread {
                    if(time == 0L){
                        countTimer?.cancel()
                        countTimer = null
                        countTask?.cancel()
                        countTask = null
                        time = 60
                        isRun = false
                        text.text = "获取验证码"
                    }else{
                        text.text = "重新获取(${time--}秒)"
                    }
                }
            }
        }
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        super.setOnClickListener {
            if (!isRun) {
                l?.onClick(it)
            }
        }
    }

    fun startCount() {
        isRun = true
        if(countTimer == null){
            countTimer = Timer()
        }
        if (countTask == null) {
            countTask = CountTask(this)
        }
        countTimer?.schedule(CountTask(this), 0, 1000)
    }
}