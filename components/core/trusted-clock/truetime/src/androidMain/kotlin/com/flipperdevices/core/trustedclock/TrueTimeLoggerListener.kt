package com.flipperdevices.core.trustedclock

import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.instacart.truetime.TrueTimeEventListener
import com.instacart.truetime.time.TrueTimeParameters
import java.net.InetAddress
import java.util.Date

private const val VERBOSE_LOGGING_NTP = false

class TrueTimeLoggerListener : TrueTimeEventListener, LogTagProvider {
    override val TAG = "TrueTimeLoggerListener"

    override fun initialize(params: TrueTimeParameters) {
        info { "Initialize: $params" }
    }

    override fun initializeFailed(e: Exception) {
        error(e) { "#initializeFailed" }
    }

    override fun initializeSuccess(ntpResult: LongArray) {
        info { "Initialize success: $ntpResult" }
    }

    override fun lastSntpRequestAttempt(ipHost: InetAddress) {
        info { "#lastSntpRequestAttempt $ipHost" }
    }

    override fun nextInitializeIn(delayInMillis: Long) {
        if (VERBOSE_LOGGING_NTP) {
            info { "#nextInitializeIn $delayInMillis" }
        }
    }

    override fun resolvedNtpHostToIPs(ntpHost: String, ipList: List<InetAddress>) {
        info { "#resolvedNtpHostToIPs $ntpHost $ipList" }
    }

    override fun returningDeviceTime() {
        if (VERBOSE_LOGGING_NTP) {
            info { "#returningDeviceTime" }
        }
    }

    override fun returningTrueTime(trueTime: Date) {
        if (VERBOSE_LOGGING_NTP) {
            info { "#returningTrueTime $trueTime" }
        }
    }

    override fun sntpRequest(address: InetAddress) {
        if (VERBOSE_LOGGING_NTP) {
            info { "#sntpRequest $address" }
        }
    }

    override fun sntpRequestFailed(e: Exception) {
        error(e) { "Request failed" }
    }

    override fun sntpRequestFailed(address: InetAddress, e: Exception) {
        error(e) { "Request failed $address" }
    }

    override fun sntpRequestSuccessful(address: InetAddress) {
        if (VERBOSE_LOGGING_NTP) {
            info { "#sntpRequestSuccessful $address" }
        }
    }

    override fun storingTrueTime(ntpResult: LongArray) {
        if (VERBOSE_LOGGING_NTP) {
            info { "#storingTrueTime $ntpResult" }
        }
    }

    override fun syncDispatcherException(t: Throwable) {
        error(t) { "#syncDispatcherException" }
    }
}