package com.ryccoatika.imagetotext.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        view_pager.adapter = sectionsPagerAdapter

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        btn_skip.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        btn_finish.setOnClickListener(this)
    }

    private fun updateUI(mode: Int) {
        when (mode) {
            FIRST_INTRO -> {
                selectedDot(listOf(dot_1), true)
                selectedDot(listOf(dot_2, dot_3), false)
                lastIntro(false)
            }
            SECOND_INTRO -> {
                selectedDot(listOf(dot_2), true)
                selectedDot(listOf(dot_1, dot_3), false)
                lastIntro(false)
            }
            THIRD_INTRO -> {
                selectedDot(listOf(dot_3), true)
                selectedDot(listOf(dot_1, dot_2), false)
                lastIntro(true)
            }
        }
    }

    private fun lastIntro(state: Boolean) {
        if (state) {
            btn_finish.visibility = View.VISIBLE
            btn_next.visibility = View.GONE
        } else {
            btn_finish.visibility = View.GONE
            btn_next.visibility = View.VISIBLE
        }
    }

    private fun selectedDot(views: List<ImageView>, state: Boolean) {
        val drawable = if (state) R.drawable.intro_dot_indicator_selected else R.drawable.intro_dot_indicator_unselected
        for (v in views) {
            v.background = ContextCompat.getDrawable(applicationContext, drawable)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_skip -> {
                startActivity(Intent(this, HomeActivity::class.java))
                this.finish()
            }
            R.id.btn_next -> {
                view_pager.currentItem++
            }
            R.id.btn_finish -> {
                startActivity(Intent(this, HomeActivity::class.java))
                this.finish()
            }
        }
    }
}