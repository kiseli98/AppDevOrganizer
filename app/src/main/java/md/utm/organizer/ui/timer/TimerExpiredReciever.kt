package md.utm.organizer.ui.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import md.utm.organizer.utils.PrefUtil

class TimerExpiredReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        PrefUtil.setTimerState(TimerFragment.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}
