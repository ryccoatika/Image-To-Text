package com.ryccoatika.imagetotext.intro


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.ryccoatika.imagetotext.R
import kotlinx.android.synthetic.main.fragment_intro.*

private val images = listOf(
    R.drawable.intro_1,
    R.drawable.intro_2,
    R.drawable.intro_3
)
private val captions = listOf(
    R.string.intro_caption_1,
    R.string.intro_caption_2,
    R.string.intro_caption_3
)

class IntroFragment(private val position: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val caption = view.context.getString(captions[position])
        val drawable = ContextCompat.getDrawable(requireContext(), images[position])

        intro_tv_caption.text = caption
        intro_iv_hint.setImageDrawable(drawable)
    }

}
