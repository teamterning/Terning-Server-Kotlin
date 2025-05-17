package com.terning.server.kotlin.domain.scrap

import com.terning.server.kotlin.domain.common.BaseException

class ScrapException(errorCode: ScrapErrorCode) : BaseException(errorCode)
