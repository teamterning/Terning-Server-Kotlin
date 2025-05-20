package com.terning.server.kotlin.domain.internshipAnnouncement

import com.terning.server.kotlin.domain.common.BaseRootEntity
import com.terning.server.kotlin.domain.filter.JobType
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob

@Entity
class InternshipAnnouncement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "title", nullable = false, length = 64))
    val title: InternshipTitle,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "deadline", nullable = false))
    val deadline: Deadline,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "workingPeriod"))
    val workingPeriod: InternshipWorkingPeriod,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "year.value", column = Column(name = "startYear", nullable = false)),
        AttributeOverride(name = "month.value", column = Column(name = "startMonth", nullable = false)),
    )
    val startDate: InternshipAnnouncementStartDate,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "viewCount", nullable = false))
    var viewCount: ViewCount = ViewCount.from(),
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "ScrapCount", nullable = false))
    var scrapCount: ScrapCount = ScrapCount.from(),
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "url", length = 256))
    val url: InternshipAnnouncementUrl,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "name.value", column = Column(name = "companyInfo")),
        AttributeOverride(name = "logoUrl.value", column = Column(name = "companyImage")),
    )
    val company: Company,
    @Lob
    @Column(name = "qualifications")
    val qualifications: String? = null,
    @Lob
    @Column(name = "jobType")
    val jobType: JobType,
    @Lob
    @Column(name = "detail")
    val detail: String? = null,
    @Column(name = "isGraduating", nullable = false)
    val isGraduating: Boolean = false,
) : BaseRootEntity() {
    fun increaseViewCount() {
        viewCount = viewCount.increase()
    }

    fun increaseScrapCount() {
        scrapCount = scrapCount.increase()
    }

    fun decreaseScrapCount() {
        scrapCount = scrapCount.decrease()
    }
}
