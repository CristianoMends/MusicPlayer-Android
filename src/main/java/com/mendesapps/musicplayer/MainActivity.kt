package com.mendesapps.musicplayer

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.graphics.Color


class MainActivity : AppCompatActivity() {
    private lateinit var musicView: MusicView
    private lateinit var activityMain: ConstraintLayout

    private val REQUEST_PERMISSION_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMain = findViewById(R.id.activityMain)
        activityMain.setBackgroundColor(Color.BLACK)
        musicView = MusicView(this)

        activityMain.addView(musicView.scrollView)

        checkPermissions()
    }

    override fun onPause() {
        super.onPause()

    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val notGrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                notGrantedPermissions.toTypedArray(),
                REQUEST_PERMISSION_CODE
            )
        } else {
            // As permissões já foram concedidas
            // Realize as operações de leitura/escrita de arquivos aqui
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // As permissões foram concedidas
                // Realize as operações de leitura/escrita de arquivos aqui
            } else {
                // As permissões foram negadas
                Toast.makeText(
                    this,
                    "As permissões foram negadas. Não é possível acessar o armazenamento externo.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}