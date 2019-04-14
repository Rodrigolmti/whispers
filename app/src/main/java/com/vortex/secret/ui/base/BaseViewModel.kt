package com.vortex.secret.ui.base

import androidx.lifecycle.ViewModel
import com.vortex.secret.util.Block
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val mViewModelJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mViewModelJob)

    override fun onCleared() {
        super.onCleared()
        mViewModelJob.cancel()
    }

    protected fun launchData(block: Block): Job {
        return mUiScope.launch {
            block()
        }
    }
}