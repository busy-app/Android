package com.flipperdevices.bsb.timer.statefactory.iteration.model

sealed interface IterationType {
    enum class Default : IterationType {
        WORK,
        REST,
        LONG_REST,
    }

    enum class Await : IterationType {
        WAIT_AFTER_WORK,
        WAIT_AFTER_REST
    }
}
