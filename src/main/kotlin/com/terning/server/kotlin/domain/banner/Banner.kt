package com.terning.server.kotlin.domain.banner

import com.terning.server.kotlin.domain.common.BaseRootEntity
import jakarta.persistence.*

@Entity
@Table(name = "banners")
class Banner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "imageUrl"))
    var imageUrl: ImageUrl,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "link"))
    var link: Link,
    @Column
    private var priority: Int,
) : BaseRootEntity()
