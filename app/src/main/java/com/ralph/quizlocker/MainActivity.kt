package com.ralph.quizlocker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v14.preference.MultiSelectListPreference
import android.support.v14.preference.PreferenceFragment
import android.support.v7.preference.PreferenceFragmentCompat

class MainActivity : AppCompatActivity() {
    val fragment = MyPreferenceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // preferenceContent FrameLayout 영역을 PreferenceFragment로 교체한다.
        //fragmentManager.beginTransaction().replace(R.id.preferenceContent, fragment).commit()
        //supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
        supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
    }

    class MyPreferenceFragment : PreferenceFragmentCompat() {
        //override fun onCreate(savedInstanceState: Bundle?) {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            //super.onCreate(savedInstanceState)

            // 환경설정 리소스 파일 적용
            addPreferencesFromResource(R.xml.pref)

            // 퀴즈 종류 요약정보에 현재 선택된 항목을 보여주는 코드
            val categoryPref = findPreference("category") as MultiSelectListPreference
            categoryPref.summary = categoryPref.values.joinToString(", ")

            // 환경설정 정보값이 변경될 때에도 요약정보를 변경하도록 리스너 등록
            categoryPref.setOnPreferenceChangeListener { preference, newValue ->
                // newValue 파라미터가 HashSet으로 캐스팅 실패하면 리턴
                val newValueSet = newValue as? HashSet<*> ?: return@setOnPreferenceChangeListener true

                // 선택된 퀴즈종류로 요약정보 보여줌
                categoryPref.summary = newValue.joinToString(", ")

                true
            }
        }
    }
}
