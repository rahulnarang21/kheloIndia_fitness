package kheloindia.com.assessment.model

data class CoachModel(
        @JvmField val KITDUniqueId: String? = null,
        private val FirstName: String? = null,
        private val MiddleName: String? = null,
        private val LastName: String? = null,
        private val date_of_birth: String? = null,
        private val sport_detail_id: String? = null,
        private val Sport: String? = null,
        private val email_id: String? = null,
        private val mobile_number: String? = null
)

