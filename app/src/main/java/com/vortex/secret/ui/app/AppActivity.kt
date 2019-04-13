package com.vortex.secret.ui.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vortex.secret.R
import com.vortex.secret.ui.custom.bottom_sheet.PostBottomSheet
import com.vortex.secret.util.exceptions.NetworkError
import com.vortex.secret.util.extensions.showSnackBar
import kotlinx.android.synthetic.main.activity_app.*
import org.koin.android.viewmodel.ext.android.viewModel

const val NAVIGATION_HOME = "fragment_home"

class AppActivity : AppCompatActivity() {

    private val viewModel by viewModel<AppViewModel>()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val navController = findNavController(R.id.nh)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
                NAVIGATION_HOME -> {
                    controlMenuVisibility(true)
                }
                else -> {
                    controlMenuVisibility(false)
                }
            }
        }
        nv.setupWithNavController(navController)

        viewModel.likeResponseErrorLiveData.observe(this, Observer { error ->
            when (error) {
                is NetworkError -> {
                    window.decorView.rootView.showSnackBar(getString(R.string.general_error_connection))
                }
                else -> {
                    error.message?.let { message -> window.decorView.rootView.showSnackBar(message) }
                }
            }
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
        this.menu = menu
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

    private fun controlMenuVisibility(visible: Boolean) {
        menu?.let {
            for (i in 0..(it.size() - 1)) {
                it.getItem(i).isVisible = visible
            }
        }
    }
}
