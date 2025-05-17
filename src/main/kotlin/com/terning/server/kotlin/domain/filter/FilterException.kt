package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.common.BaseException

class FilterException(errorCode: WorkingPeriodErrorCode) : BaseException(errorCode)
