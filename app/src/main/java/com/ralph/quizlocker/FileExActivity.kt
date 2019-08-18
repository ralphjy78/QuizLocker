package com.ralph.quizlocker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class FileExActivity : AppCompatActivity() {

    val filename = "data.txt"
    var granted = false

    val MY_PERMISSION_REQUEST = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)

        // 외부저장소의 권한을 동적으로 체크
        checkPermission()

        // 저장 버튼
        saveButton.setOnClickListener {
            val text = textField.text.toString()

            when {
                TextUtils.isEmpty(text) -> Toast.makeText(applicationContext, "텍스트가 비었습니다", Toast.LENGTH_LONG).show()
                !isExternalStorageWritable() -> Toast.makeText(applicationContext, "외부 저장장치가 없습니다.", Toast.LENGTH_LONG).show()
                else -> {
                    // 내부 저장소 파일에 저장하는 함수 호출
                    //saveToInnerStorage(text, filename)

                    // 외부 저장소 파일에 저장하는 함수 호출
                    //saveToExternalStorage(text, filename)

                    // 외부저장소 /sdcard/data.txt 에 데이터를 저장
                    saveToExternalCustomDirectory(text)
                }
            }
        }

        // 불러오기 버튼
        loadButton.setOnClickListener {
            try {
                //textField.setText(loadFromInnerStorage(filename))
                // 외부저장소 앱전용 디렉토리의 파일에서 읽어온 데이터로 textField의 텍스트를 설정
                //textField.setText(loadFromExternalStroage(filename))

                // 외부저장소 /sdcard/data.txt 에서 데이터를 로드
                textField.setText(loadFromExternalCustomDirectory())

            } catch (e: FileNotFoundException) {
                Toast.makeText(applicationContext, "저장된 텍스트가 없습니다", Toast.LENGTH_LONG).show()
            }
        }
    }


    // 권한 체크 및 요청 함수
    fun checkPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this@FileExActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        when {
            permissionCheck != PackageManager.PERMISSION_GRANTED -> {
                // 권한을 요청
                ActivityCompat.requestPermissions(this@FileExActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSION_REQUEST)
            }
        }
    }

    // 권한요청 결과 콜백 함수
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            MY_PERMISSION_REQUEST -> {
                when {
                    grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        granted = true      // 권한 요청 성공
                    }
                    else -> {
                        granted = false     // 사용자가 권한을 허용하지 않음
                    }
                }
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // 임의 경로 파일에 데이터를 저장하는 함수
    fun saveToExternalCustomDirectory(text: String, filepath: String = "/sdcard/data.txt") {
        when {
            // 권한이 있는 경우
            granted -> {
                // 파라미터로 전달받은 경로의 파일 출력 스트림 객체 생성
                val fileOutputStream = FileOutputStream(File(filepath))

                fileOutputStream.write(text.toByteArray())
                fileOutputStream.close()
            }
            // 권한이 없는 경우
            else -> {
                Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 임의 경로 파일에서 데이터를 읽는 함수
    fun loadFromExternalCustomDirectory(filepath: String = "/sdcard/data.txt"): String {
        when {
            // 권한이 있는 경우
            granted -> {
                return FileInputStream(File(filepath)).reader().readText()
            }
            // 권한이 없는 경우
            else -> {
                Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_LONG).show()
                return ""
            }
        }
    }




    fun saveToInnerStorage(text: String, filename: String) {
        val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)

        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()
    }

    fun loadFromInnerStorage(filename: String): String {
        val fileInputStream = openFileInput(filename)

        return fileInputStream.reader().readText()
    }

    // 외부 저장장치를 사용할 수 있고 쓸 수 있는지 체크하는 함수
    fun isExternalStorageWritable(): Boolean {
        when {
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> return true
            else -> return false
        }
    }

    // 외부 저장장치에서 앱 전용 데이터로 사용할 파일 객체를 반환하는 함수
    fun getAppDataFileFromExternalStorage(filename: String): File {
        // KITKAT 버전부터는 앱 전용 디렉토리의 디렉토리 타입 상수인 Environment.DIRECTORY_DOCUMENTS를 지원
        var dir =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            } else {
                File(Environment.getExternalStorageDirectory().absolutePath + "/Documents")
            }
        // 디렉토리 경로 중 없는 디렉토리가 있으면 생성한다
        dir?.mkdir()

        return File("${dir.absolutePath}${File.separator}${filename}")
    }

    // 외부저장소 앱 전용 디렉토리에 파일로 저장하는 함수
    fun saveToExternalStorage(text: String, filename: String) {
        val fileOutputStream = FileOutputStream(getAppDataFileFromExternalStorage(filename))
        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()
    }

    // 외부저장소 앱 전용 디렉토리에서 파일 데이터를 불러오는 함수
    fun loadFromExternalStroage(filename: String): String {
        return FileInputStream(getAppDataFileFromExternalStorage(filename)).reader().readText()
    }
}
