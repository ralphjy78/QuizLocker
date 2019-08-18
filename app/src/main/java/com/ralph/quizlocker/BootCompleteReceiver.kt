package com.ralph.quizlocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootCompleteReceiver : BroadcastReceiver() {
    // 브로드캐스트 메시지 수신시 호출되는 콜백함수
    override fun onReceive(context: Context?, intent: Intent?) {
        // 부팅이 완료될 때의 메시지인지 확인
        when {
            intent?.action == Intent.ACTION_BOOT_COMPLETED -> {
                Log.d("quizlocker", "부팅이 완료됨")
                Toast.makeText(context, "퀴즈 잠금화면: 부팅이 완료됨", Toast.LENGTH_LONG).show()
            }
        }
    }
}