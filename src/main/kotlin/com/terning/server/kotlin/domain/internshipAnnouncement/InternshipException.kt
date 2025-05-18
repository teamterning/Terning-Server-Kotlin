package com.terning.server.kotlin.domain.internshipAnnouncement

import com.terning.server.kotlin.domain.common.BaseException

class InternshipException(errorCode: InternshipErrorCode) : BaseException(errorCode)
