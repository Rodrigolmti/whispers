package com.vortex.secret.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.vortex.secret.data.remote.AnalyticsManager
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity(), BaseView {

    override val analyticsManager: AnalyticsManager by inject()
}