package com.vortex.secret.ui.base

import androidx.fragment.app.Fragment
import com.vortex.secret.data.remote.AnalyticsManager
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment(), BaseView {

    override val analyticsManager: AnalyticsManager by inject()
}