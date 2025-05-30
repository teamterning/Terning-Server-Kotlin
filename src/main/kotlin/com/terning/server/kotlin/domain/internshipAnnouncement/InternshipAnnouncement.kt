package com.terning.server.kotlin.domain.internshipAnnouncement

import com.terning.server.kotlin.domain.common.BaseRootEntity
import com.terning.server.kotlin.domain.filter.vo.FilterJobType
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.Company
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementDeadline
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementScrapCount
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementStartDate
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementUrl
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementViewCount
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipTitle
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipWorkingPeriod
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import org.hibernate.Hibernate

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
    val internshipAnnouncementDeadline: InternshipAnnouncementDeadline,

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
    var internshipAnnouncementViewCount: InternshipAnnouncementViewCount = InternshipAnnouncementViewCount.from(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "ScrapCount", nullable = false))
    var internshipAnnouncementScrapCount: InternshipAnnouncementScrapCount = InternshipAnnouncementScrapCount.from(),

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
    val filterJobType: FilterJobType,

    @Lob
    @Column(name = "detail")
    val detail: String? = null,

    @Column(name = "isGraduating", nullable = false)
    val isGraduating: Boolean = false,
) : BaseRootEntity() {
    fun increaseViewCount() {
        internshipAnnouncementViewCount = internshipAnnouncementViewCount.increase()
    }

    fun increaseScrapCount() {
        internshipAnnouncementScrapCount = internshipAnnouncementScrapCount.increase()
    }

    fun decreaseScrapCount() {
        internshipAnnouncementScrapCount = internshipAnnouncementScrapCount.decrease()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as InternshipAnnouncement
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}
