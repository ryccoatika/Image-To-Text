package com.ryccoatika.imagetotext.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.home.HomeActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val FIRST_INTRO = 1
        private const val SECOND_INTRO = 2
        private const val THIRD_INTRO = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        updateUI(FIRST_INTRO)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        intro_view_pager.adapter = sectionsPagerAdapter

        intro_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> updateUI(FIRST_INTRO)
                    1 -> updateUI(SECOND_INTRO)
                    2 -> updateUI(THIRD_INTRO)
                }
            }

        })

        intro_btn_skip.setOnClickListener(this)
        intro_btn_next.setOnClickListener(this)
        intro_btn_finish.setOnClickListener(this)
    }

    private fun updateUI(mode: Int) {
        when (mode) {
            FIRST_INTRO -> {
                selectedDot(listOf(intro_dot_1), true)
                selectedDot(listOf(intro_dot_2, intro_dot_3), false)
                lastIntro(false)
            }
            SECOND_INTRO -> {
                selectedDot(listOf(intro_dot_2), true)
                selectedDot(listOf(intro_dot_1, intro_dot_3), false)
                lastIntro(false)
            }
            THIRD_INTRO -> {
                selectedDot(listOf(intro_dot_3), true)
                selectedDot(listOf(intro_dot_1, intro_dot_2), false)
                lastIntro(true)
            }
        }
    }

    private fun lastIntro(state: Boolean) {
        if (state) {
            intro_btn_finish.visibility = View.VISIBLE
            intro_btn_next.visibility = View.GONE
        } else {
            intro_btn_finish.visibility = View.GONE
            intro_btn_next.visibility = View.VISIBLE
        }
    }

    private fun selectedDot(views: List<ImageView>, state: Boolean) {
        val drawable = if (state) R.drawable.intro_dot_indicator_selected else R.drawable.intro_dot_indicator_unselected
        for (v in views) {
            v.background = getDrawable(drawable)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.intro_btn_skip -> {
                startActivity(Intent(this, HomeActivity::class.java))
                this.finish()
            }
            R.id.intro_btn_next -> {
                intro_view_pager.currentItem++
            }
            R.id.intro_btn_finish -> {
                startActivity(Intent(this, HomeActivity::class.java))
                this.finish()
            }
        }
    }
}