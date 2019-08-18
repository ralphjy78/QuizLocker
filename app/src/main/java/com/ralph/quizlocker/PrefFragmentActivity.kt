package com.ralph.quizlocker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class PrefFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_fragment)

        // 액티비티 컨텐트 뷰를 MyPrefFragment로 교체한다.
        supportFragmentManager.beginTransaction().replace(android.R.id.content, MyPrefFragment()).commit()
    }

    // PreferenceFragment: XML로 작성한 Preference를 UI로 보여주는 클래스
    class MyPrefFragment: PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            //super.onCreatePreferences(savedInstanceState, rootKey)

            // Preference 정보가 있는 XML 파일 지정
            addPreferencesFromResource(R.xml.ex_pref)

            //setPreferencesFromResource(R.xml.ex_pref, rootKey)
        }
    }
}
