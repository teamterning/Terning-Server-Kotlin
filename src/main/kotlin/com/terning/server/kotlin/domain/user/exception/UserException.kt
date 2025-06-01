package com.terning.server.kotlin.domain.user.exception

import com.terning.server.kotlin.domain.common.BaseException

class UserException(errorCode: UserErrorCode) : BaseException(errorCode)
