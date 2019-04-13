package com.vortex.secret.ui.auth.sign_in

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vortex.secret.R
import com.vortex.secret.ui.app.AppActivity
import com.vortex.secret.util.exceptions.NetworkError
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.paintText
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.android.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {

    private val viewModel by viewModel<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idSignUpLink.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_signUpFragment) }

        val message = getString(R.string.sign_in_fragment_sign_up_link)
        idSignUpLink.text = paintText(message, resources.getColor(R.color.colorPrimary), 20, message.length)

        btSignIn.setOnClickListener {
            if (validateFields()) {
                viewModel.signInUser(etEmail.text.toString(), etPassword.text.toString())
            }
        }

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                tvTitle.gone()
                tilEmail.gone()
                tilPassword.gone()
                btSignIn.gone()
                idSignUpLink.gone()
                ltLoading.visible()
            } else {
                ltLoading.gone()
                tvTitle.visible()
                tilEmail.visible()
                tilPassword.visible()
                btSignIn.visible()
                idSignUpLink.visible()
            }
        })

        viewModel.authResponseLiveData.observe(this, Observer {
            if (it) {
                startActivity(Intent(context, AppActivity::class.java))
                activity?.finish()
            } else {
                view.showSnackBar(getString(R.string.general_error))
            }
        })

        viewModel.errorSignInMutableLiveDate.observe(this, Observer { error ->
            when (error) {
                is NetworkError -> {
                    view.showSnackBar(getString(R.string.general_error_connection))
                }
                else -> {
                    error.message?.let { message -> view.showSnackBar(message) }
                }
            }
        })
    }

    private fun validateFields() : Boolean {

        if (etEmail.text.isNullOrEmpty()) {
            view?.showSnackBar(getString(R.string.general_email_error))
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches()) {
            view?.showSnackBar(getString(R.string.general_email_invalid))
            return false
        }

        if (etPassword.text.isNullOrEmpty()) {
            view?.showSnackBar(getString(R.string.general_password_error))
            return false
        }

        return true
    }
}
