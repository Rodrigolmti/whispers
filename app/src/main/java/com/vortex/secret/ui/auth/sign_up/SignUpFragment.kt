package com.vortex.secret.ui.auth.sign_up

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vortex.secret.R
import com.vortex.secret.ui.app.AppActivity
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val viewModel by viewModel<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btSignUp.setOnClickListener {
            if (validateFields()) {
                viewModel.signUpUser(etEmail.text.toString(), etPassword.text.toString())
            }
        }

        viewModel.loadingLiveData.observe(this, Observer {

            if (it) {
                tvTitle.gone()
                tilNickname.gone()
                tilEmail.gone()
                tilPassword.gone()
                btSignUp.gone()
                ltLoading.visible()
            } else {
                tvTitle.visible()
                tilNickname.visible()
                tilEmail.visible()
                tilPassword.visible()
                btSignUp.visible()
                ltLoading.gone()
            }
        })

        viewModel.successSignUpMutableLiveDate.observe(this, Observer {
            startActivity(Intent(context, AppActivity::class.java))
            activity?.finish()
        })

        viewModel.errorSignUpMutableLiveDate.observe(this, Observer {
            view.showSnackBar(it)
        })
    }

    private fun validateFields() : Boolean {

        if (etNickname.text.isNullOrEmpty()) {
            view?.showSnackBar(getString(R.string.general_nickname_error))
            return false
        }

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

        if (etPassword.text.length < 6) {
            view?.showSnackBar(getString(R.string.general_password_invalid))
            return false
        }

        return true
    }
}