package com.ryccoatika.imagetotext

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented intro_1, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under intro_1.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ryccoatika.imagetotext", appContext.packageName)
    }
}
