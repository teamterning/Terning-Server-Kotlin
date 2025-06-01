package com.terning.server.kotlin.domain.scrap.exception

import com.terning.server.kotlin.domain.common.BaseException

class ScrapException(errorCode: ScrapErrorCode) : BaseException(errorCode)
