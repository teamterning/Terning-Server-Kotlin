package com.terning.server.kotlin.domain.auth.exception

import com.terning.server.kotlin.domain.common.BaseException

class AuthException(errorCode: AuthErrorCode) : BaseException(errorCode)
