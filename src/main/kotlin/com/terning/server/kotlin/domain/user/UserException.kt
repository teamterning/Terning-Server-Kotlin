package com.terning.server.kotlin.domain.user

import com.terning.server.kotlin.domain.common.BaseException

class UserException(errorCode: UserErrorCode) : BaseException(errorCode)
