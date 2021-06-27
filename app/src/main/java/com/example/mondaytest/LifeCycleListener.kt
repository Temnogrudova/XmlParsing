package com.example.mondaytest

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mondaytest.ui.MainActivity

class LifeCycleListener : Application.ActivityLifecycleCallbacks {
    companion object {
        const val REFRESH_HOMEPAGE_TIME = 1 // 10 minutes

        /**
         * Get minutes in milliseconds
         */
        fun Int.toMillis(): Int {
            return this * 60 * 1000 // 60 seconds in 1 minute, 1000 milliseconds in 1 second
        }
    }
    private val TAG: String = LifeCycleListener::class.java.simpleName
    private var mIsInBackground = false
    private var mPauseTime: Long = 0
    private var mIsPaused = false
    val shouldRefresh: Boolean
        get() {
            if (mPauseTime == 0L) {
                return false
            }
            val refreshTime =  REFRESH_HOMEPAGE_TIME.toMillis()

            val interval = System.currentTimeMillis() - mPauseTime
            return interval > refreshTime
        }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        mIsPaused = false
        if (mIsInBackground) {
            Log.e(TAG, "App just started or resumed from the background")
            mIsInBackground = false
            onApplicationReturnFromBackground(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {

        mIsPaused = true
        Handler(Looper.getMainLooper()).postDelayed({
            if (mIsPaused) {
                Log.e(TAG, "App went into background")
                mIsInBackground = true
                mPauseTime = System.currentTimeMillis()
            }
        }, 1500)
    }

    private fun onApplicationReturnFromBackground(activity: Activity) {
        if (shouldRefresh) {
            Log.e(TAG, "Refresh time passed, restarting the app")
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtras(activity.intent)
            activity.startActivity(intent)
            activity.finish()
        }
        mPauseTime = 0
    }
}