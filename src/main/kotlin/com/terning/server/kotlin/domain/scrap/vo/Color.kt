package com.terning.server.kotlin.domain.scrap.vo

import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException

enum class Color(
    val color: String,
    private val hexCode: String,
) {
    RED("red", "ED4E54"),
    ORANGE("orange", "F3A649"),
    LIGHT_GREEN("lightgreen", "C4E953"),
    MINT("mint", "45D0CC"),
    PURPLE("purple", "9B64E2"),
    CORAL("coral", "EE7647"),
    YELLOW("yellow", "F5E660"),
    GREEN("green", "84D558"),
    BLUE("blue", "4AA9F2"),
    PINK("pink", "F260AC"),
    ;

    fun toHexString(): String = "#$hexCode"

    companion object {
        fun from(color: String): Color =
            entries.firstOrNull { it.color.equals(color, ignoreCase = true) }
                ?: throw ScrapException(ScrapErrorCode.INVALID_COLOR)
    }
}
