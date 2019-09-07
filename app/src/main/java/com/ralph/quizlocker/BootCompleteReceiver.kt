package com.ralph.quizlocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import java.util.concurrent.locks.Lock

class BootCompleteReceiver : BroadcastReceiver() {
    // 브로드캐스트 메시지 수신시 호출되는 콜백함수
    override fun onReceive(context: Context?, intent: Intent?) {
        // 부팅이 완료될 때의 메시지인지 확인
        when {
            intent?.action == Intent.ACTION_BOOT_COMPLETED -> {
                Log.d("quizlocker", "부팅이 완료됨")

                context?.let {
                    // 퀴즈잠금화면 설정값이 ON 인지 확인
                    val pref = PreferenceManager.getDefaultSharedPreferences(context)
                    val useLockScreen = pref.getBoolean("useLockScreen", false)
                    if(useLockScreen) {
                        // LockScreenService 시작
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            it.startForegroundService(Intent(context, LockScreenService::class.java))
                        } else {
                            it.startService(Intent(context, LockScreenService::class.java))
                        }
                    }
                }
            }
        }
    }
}