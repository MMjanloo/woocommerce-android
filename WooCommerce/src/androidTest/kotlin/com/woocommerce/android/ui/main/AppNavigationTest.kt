package com.woocommerce.android.ui.main

import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.NoActivityResumedException
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.woocommerce.android.R.id.bottom_nav
import com.woocommerce.android.ui.TestBase
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest : TestBase() {
    class MainActivityTestRule : ActivityTestRule<MainActivity>(MainActivity::class.java, false, false) {
        override fun launchActivity(startIntent: Intent?): MainActivity {
            return launchActivity(startIntent, false)
        }

        fun launchActivity(startIntent: Intent?, userIsLoggedIn: Boolean): MainActivity {
            // Configure the mocked MainPresenter to pretend the user is logged in.
            // We normally wouldn't need the MockedMainModule method, and just configure the mocked presenter directly
            // using whenever(activityTestRule.activity.presenter.userIsLoggedIn()).thenReturn(true)
            // In this case, however, userIsLoggedIn() is called in the activity's onCreate(), which means after
            // launchActivity() is too late, but the activity's presenter is null before that.
            // So, we need to configure this at the moment the injection is happening: when the presenter is initialized.
            MockedMainModule.setUserIsLoggedInResponse(userIsLoggedIn)
            return super.launchActivity(startIntent)
        }
    }

    @Rule
    @JvmField var activityTestRule = MainActivityTestRule()

    @Test
    fun pressingBackOnMainScreenExitsApp() {
        activityTestRule.launchActivity(null, userIsLoggedIn = true)

        assertPressingBackExitsApp()
    }

    @Test
    fun bottomBarShouldBeVisibleOnLoggedInLaunch() {
        activityTestRule.launchActivity(null, userIsLoggedIn = true)

        // Since the MainPresenter is telling us the user is logged in, we should see the main activity and bottom bar
        // (and not the login screen)
        Espresso.onView(withId(bottom_nav)).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun assertPressingBackExitsApp() {
        try {
            Espresso.pressBack()
            fail("Expected app to be closed and throw an exception")
        } catch (e: NoActivityResumedException) {
            // Test OK
        }
    }
}