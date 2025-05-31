package com.terning.server.kotlin.domain.filter.exception

import com.terning.server.kotlin.domain.common.BaseException

class FilterException(errorCode: FilterErrorCode) : BaseException(errorCode)
