package com.flipperdevices.bsb.cloud.api

import com.flipperdevices.bsb.cloud.model.BSBUser

interface BSBUserApi {
    suspend fun getUser(): Result<BSBUser>
}
