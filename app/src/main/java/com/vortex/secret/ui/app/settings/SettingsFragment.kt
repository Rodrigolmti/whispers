package com.vortex.secret.ui.app.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.vortex.secret.R
import com.vortex.secret.data.remote.AnalyticsEvents
import com.vortex.secret.ui.auth.LoginActivity
import com.vortex.secret.ui.base.BaseFragment
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        tvLogout.setOnClickListener {
            viewModel.logoutUser()
        }

        swAnonymous.setOnCheckedChangeListener { _, checked ->
            viewModel.updateUserAnonymousMode(checked)
        }

        viewModel.responseAnonymousModeLiveData.observe(this, Observer {
            swAnonymous.isChecked = it
        })

        viewModel.errorLiveData.observe(this, Observer { error ->
            error.message?.let { view.showSnackBar(it) }
        })

        viewModel.loadingLiveData.observe(this, Observer { isLoading ->
            if (isLoading) {
                tvLogout.gone()
                swAnonymous.gone()
                viSeparatorLogout.gone()
                viSeparatorAnonymous.gone()
                ltLoading.visible()
            } else {
                ltLoading.gone()
                tvLogout.visible()
                swAnonymous.visible()
                viSeparatorLogout.visible()
                viSeparatorAnonymous.visible()
            }
        })

        viewModel.responseLogoutLiveData.observe(this, Observer {
            startActivity(Intent(context, LoginActivity::class.java))
            analyticsManager.sendEvent(AnalyticsEvents.LOGOUT.name)
            activity?.finish()
        })
    }
}
