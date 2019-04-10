package com.vortex.secret.ui.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vortex.secret.R
import com.vortex.secret.ui.custom.PostBottomSheet
import com.vortex.secret.util.extensions.showSnackBar
import org.koin.android.viewmodel.ext.android.viewModel

class AppActivity : AppCompatActivity() {

    private val viewModel by viewModel<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        viewModel.likeResponseErrorLiveData.observe(this, Observer {
            it.message?.let { message -> window.decorView.rootView.showSnackBar(message) }
        })

        viewModel.likeResponseLiveData.observe(this, Observer { success ->
            if (!success) {
                window.decorView.rootView.showSnackBar(getString(R.string.general_error))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_post -> {
                PostBottomSheet.newInstance {
                    viewModel.addNewPost(it)
                }.show(supportFragmentManager)
            }
        }
        return true
    }
}
