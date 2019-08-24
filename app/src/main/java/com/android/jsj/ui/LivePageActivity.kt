package com.android.jsj.ui

import android.os.Bundle
import com.android.jsj.R
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.E
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayer
import com.tencent.rtmp.TXVodPlayer
import kotlinx.android.synthetic.main.activity_live_page.*
import org.jetbrains.anko.toast
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/11/011.
 */
class LivePageActivity : MyBaseActivity(), ITXLivePlayListener {
    override fun onPlayEvent(p0: Int, p1: Bundle?) {
        if (p0 == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            toast("[AnswerRoom] 拉流失败：网络断开")
        } else if (p0 == TXLiveConstants.PLAY_EVT_GET_MESSAGE) {
            if (p1 == null) return
            try {
                val msg = String(p1.getByteArray(TXLiveConstants.EVT_GET_MSG), Charset.forName("UTF-8"))
                toast(msg)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

        }
    }

    override fun onNetStatus(p0: Bundle?) {

    }

    private lateinit var mLivePlayer: TXVodPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_page)
//        val flvUrl = intent.getStringExtra("url")
        val flvUrl = "http://5815.liveplay.myqcloud.com/live/5815_89aad37e06ff11e892905cb9018cf0d4_900.flv"
        E("flvUrl = $flvUrl")

        mLivePlayer = TXVodPlayer (this)
        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(video_view)

        // 设置填充模式
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION)
        // 设置画面渲染方向
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE)

//        mLivePlayer.setAutoPlay(true)

        when (mLivePlayer.startPlay(flvUrl)) {//0 success; -1 empty url; -2 invalid url; -3 invalid playType;
            0 -> {
                toast("success")
            }
            -1 -> {
                toast("empty url")
            }
            -2 -> {
                toast("invalid url")
            }
            -3 -> {
                toast("invalid playType")
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        mLivePlayer.stopPlay(true) // true 代表清除最后一帧画面
        video_view.onDestroy()
    }
}