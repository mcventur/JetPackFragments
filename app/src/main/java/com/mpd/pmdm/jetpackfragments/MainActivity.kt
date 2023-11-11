package com.mpd.pmdm.jetpackfragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

const val STAR_SIGN_ID = "STAR_SIGN_ID"
interface StarSignListener {
    /**
     * Se lanzará cuando se seleccione un signo desde el ListFragment
     */
    fun onSelected(id: Int)
}


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}