package com.terning.server.kotlin.domain.internshipAnnouncement.exception

import com.terning.server.kotlin.domain.common.BaseException

class InternshipAnnouncementException(errorCode: InternshipAnnouncementErrorCode) : BaseException(errorCode)
