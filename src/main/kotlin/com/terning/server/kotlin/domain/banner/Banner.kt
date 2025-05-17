package com.terning.server.kotlin.domain.banner

import com.terning.server.kotlin.domain.common.BaseRootEntity
import jakarta.persistence.Column
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
    @Column(length = 255)
    private var imageUrl: String,
    @Column(length = 255)
    private var link: String,
    @Column
    private var priority: Int,
) : BaseRootEntity()
