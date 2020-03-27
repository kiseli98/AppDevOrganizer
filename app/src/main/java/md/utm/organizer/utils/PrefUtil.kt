package md.utm.organizer.utils

import android.content.Context
import androidx.preference.PreferenceManager
import md.utm.organizer.ui.timer.TimerFragment

class PrefUtil {
    companion object {

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID =
            "md.utm.organizer.timer.previous_timer_length"

        fun getTimerLength(context: Context): Int {
            return 1
        }

        private const val TIMER_STATE_ID = "md.utm.organizer.timer.timer_state"

        //when timer length is changed while a timer is running
        // the current length will not be changed, only future
        fun getPreviousTimerSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }


        fun getTimerState(context: Context): TimerFragment.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            //as enums are ints
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerFragment.TimerState, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "md.utm.organizer.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID =  "md.utm.organizer.timer.background_time"

        fun getAlarmSetTime(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}