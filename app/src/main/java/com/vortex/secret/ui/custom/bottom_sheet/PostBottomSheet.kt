package com.vortex.secret.ui.custom.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.vortex.secret.R
import com.vortex.secret.data.model.PostColor
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.ui.app.home.adapter.PostColorAdapter
import com.vortex.secret.util.extensions.showDialog
import kotlinx.android.synthetic.main.fragment_bottom_sheet_post.*

typealias PostEvent = (post: PostModel) -> Unit

class PostBottomSheet : BaseBottomSheet() {

    private lateinit var postEvent: PostEvent
    private val postModel = PostModel()

    companion object {

        fun newInstance(postEvent: PostEvent): PostBottomSheet {
            val postBottomSheet = PostBottomSheet()
            postBottomSheet.postEvent = postEvent
            return postBottomSheet
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        rvColors.layoutManager = layoutManager
        rvColors.adapter = PostColorAdapter(listOf(
                PostColor(R.color.dark_pink),
                PostColor(R.color.indigo),
                PostColor(R.color.turquoise_blue),
                PostColor(R.color.turquoise),
                PostColor(R.color.shamrock),
                PostColor(R.color.green),
                PostColor(R.color.robins_egg_blue),
                PostColor(R.color.spiro_disco_ball),
                PostColor(R.color.dull_blue),
                PostColor(R.color.radical_red),
                PostColor(R.color.san_juan),
                PostColor(R.color.geyser),
                PostColor(R.color.carnation),
                PostColor(R.color.goldenrod),
                PostColor(R.color.golden_tainoi),
                PostColor(R.color.black_pearl),
                PostColor(R.color.regent_gray),
                PostColor(R.color.coral_red),
                PostColor(R.color.bright_sun),
                PostColor(R.color.sea_buckthorn)
        )) {
            postModel.color = it.color
        }

        btPost.setOnClickListener {
            if (validateFields()) {
                postModel.body = etPost.text.toString()
                postEvent(postModel)
                dismiss()
            } else {
                context?.showDialog(
                    getString(R.string.general_dialog_title),
                    getString(R.string.bottom_sheet_post_error_message)
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        if (etPost.text.isNullOrEmpty()) {
            return false
        }

        if (postModel.color == null) {
            return false
        }

        return true
    }
}