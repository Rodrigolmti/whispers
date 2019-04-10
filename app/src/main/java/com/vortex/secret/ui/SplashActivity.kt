package com.vortex.secret.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import com.vortex.secret.R
import com.vortex.secret.ui.app.AppActivity
import com.vortex.secret.ui.auth.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()
        viewModel.userSessionLiveData.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, AppActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        })
    }
}
