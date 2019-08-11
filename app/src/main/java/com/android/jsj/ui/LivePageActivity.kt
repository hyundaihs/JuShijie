package com.android.jsj.ui

import android.os.Bundle
import com.android.jsj.R
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.tencent.rtmp.TXLivePlayer
import com.tencent.rtmp.ui.TXCloudVideoView
import kotlinx.android.synthetic.main.activity_live_page.*


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/11/011.
 */
class LivePageActivity : MyBaseActivity() {

    private var flvUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_page)
        val flvUrl = intent.getStringExtra("url")
        //创建 player 对象
        val mLivePlayer = TXLivePlayer(this)

        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(video_view)

        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV) //推荐 FLV
    }
}