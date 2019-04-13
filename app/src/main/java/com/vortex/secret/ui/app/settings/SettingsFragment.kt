package com.vortex.secret.ui.app.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.vortex.secret.R
import com.vortex.secret.ui.auth.LoginActivity
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvLogout.setOnClickListener {
            viewModel.logoutUser()
        }

        viewModel.errorLiveData.observe(this, Observer { error ->
            error.message?.let { view.showSnackBar(it) }
        })

        viewModel.loadingLiveData.observe(this, Observer { isLoading ->
            if (isLoading) {
                tvLogout.gone()
                viSeparator.gone()
                ltLoading.visible()
            } else {
                ltLoading.gone()
                tvLogout.visible()
                viSeparator.visible()
            }
        })

        viewModel.responseLogoutLiveData.observe(this, Observer {
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        })
    }
}
