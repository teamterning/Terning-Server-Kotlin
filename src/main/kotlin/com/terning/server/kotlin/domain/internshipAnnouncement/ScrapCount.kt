import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
import jakarta.persistence.Embeddable

@Embeddable
class ScrapCount private constructor(
    val value: Int,
) {

    protected constructor() : this(MIN_VALUE)

    init {
        validateMinimum()
    }

    fun increase(): ScrapCount = ScrapCount(value + 1)

    fun decrease(): ScrapCount {
        if (value == MIN_VALUE) {
            throw InternshipException(InternshipErrorCode.SCRAP_COUNT_CANNOT_BE_DECREASED_BELOW_ZERO)
        }
        return ScrapCount(value - 1)
    }

    override fun equals(other: Any?): Boolean =
        other is ScrapCount && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    private fun validateMinimum() {
        if (value < MIN_VALUE) {
            throw InternshipException(InternshipErrorCode.INVALID_SCRAP_COUNT)
        }
    }

    companion object {
        private const val MIN_VALUE = 0

        fun from(): ScrapCount = ScrapCount(MIN_VALUE)
    }
}

