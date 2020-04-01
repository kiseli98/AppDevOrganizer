package md.utm.organizer.ui.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import md.utm.organizer.internal.AppConstants
import md.utm.organizer.utils.NotificationUtil
import md.utm.organizer.utils.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                TimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerFragment.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerFragment.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                TimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerFragment.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime =
                    TimerFragment.setAlaram(context, TimerFragment.nowSeconds, secondsRemaining)

                PrefUtil.setTimerState(TimerFragment.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L

                val wakeUpTime =
                    TimerFragment.setAlaram(context, TimerFragment.nowSeconds, secondsRemaining)

                PrefUtil.setTimerState(TimerFragment.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}
