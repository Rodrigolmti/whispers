package com.vortex.secret.ui.custom.bottom_sheet

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vortex.secret.R

abstract class BaseBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, PostBottomSheet::javaClass.name)
    }
}