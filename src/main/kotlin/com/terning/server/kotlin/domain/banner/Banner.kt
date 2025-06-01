package com.terning.server.kotlin.domain.banner

import com.terning.server.kotlin.domain.banner.vo.ImageUrl
import com.terning.server.kotlin.domain.banner.vo.Link
import com.terning.server.kotlin.domain.common.BaseRootEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "banners")
class Banner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "imageUrl", length = 255))
    var imageUrl: ImageUrl,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "link", length = 255))
    var link: Link,

    @Column(name = "priority")
    private var priority: Int,
) : BaseRootEntity()
