package com.flipperdevices.bsb.timer.background.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.flipperdevices.bsb.timer.background.service.TimerForegroundService
import com.flipperdevices.bsb.timer.background.service.TimerServiceActionEnum
import com.flipperdevices.bsb.timer.notification.TimerPendingIntents
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info

class TimerBroadcastReceiver : BroadcastReceiver(), LogTagProvider {
    override val TAG = "TimerBroadcastReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        info { "Receive ${intent.toFullString()}" }
        val stopIntent = Intent(context, TimerForegroundService::class.java).apply {
            action = intent.action
        }
        context.startService(stopIntent)
    }

    companion object {
        @SuppressLint("UnspecifiedImmutableFlag", "ObsoleteSdkInt")
        private fun getTimerIntent(
            context: Context,
            serviceActionEnum: TimerServiceActionEnum
        ): PendingIntent {
            val intent = Intent(context, TimerBroadcastReceiver::class.java)
            intent.action = serviceActionEnum.actionId

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
        }

        fun getTimerIntents(
            context: Context,
        ): TimerPendingIntents = TimerPendingIntents(
            nextStep = getTimerIntent(context, TimerServiceActionEnum.NEXT_STEP),
            resume = getTimerIntent(context, TimerServiceActionEnum.RESUME),
            pause = getTimerIntent(context, TimerServiceActionEnum.PAUSE)
        )
    }
}
