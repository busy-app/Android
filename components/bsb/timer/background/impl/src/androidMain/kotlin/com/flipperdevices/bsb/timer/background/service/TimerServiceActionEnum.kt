package com.flipperdevices.bsb.timer.background.service

enum class TimerServiceActionEnum(val actionId: String) {
    PAUSE("com.flipperdevices.bsb.timer.background.model.PAUSE"),
    RESUME("com.flipperdevices.bsb.timer.background.model.RESUME"),
    START("com.flipperdevices.bsb.timer.background.model.START"),
    STOP("com.flipperdevices.bsb.timer.background.model.STOP"),
}
