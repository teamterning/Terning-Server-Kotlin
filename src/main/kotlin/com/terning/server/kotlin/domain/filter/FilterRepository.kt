package com.terning.server.kotlin.domain.filter

import org.springframework.data.jpa.repository.JpaRepository

interface FilterRepository : JpaRepository<Filter, Long>
