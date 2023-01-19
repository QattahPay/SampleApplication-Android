package sa.qattahpay.qattahpaysampleapp.utils

import sa.qattahpay.qattahpaysampleapp.BuildConfig

object Constants {

    private const val MODE = "staging" // "production" or "staging"

    private const val API_PROD = "https://api.qattahpay.sa/api/v1/merchant-integration/"
    private const val API_TEST = "https://staging-api.qattahpay.sa/api/v1/merchant-integration/"

    private const val APP_PROD = "https://app.qattahpay.sa/"
    private const val APP_TEST = "https://staging.qattahpay.sa/"

    const val PAYMENT_CALLBACK_SOCKET_URL = "http://161.35.70.189:3000/"
    const val ORDER_PAYMENT_STATUS = "payment"

    fun qattahPayToken(): String {
        return when (MODE) {
            "production" -> "Bearer ${BuildConfig.QATTAH_PRODUCTION_TOKEN}"
            "staging" -> "Bearer ${BuildConfig.QATTAH_TESTING_TOKEN}"
            else -> "Bearer ${BuildConfig.QATTAH_TESTING_TOKEN}"
        }
    }

    fun apiBaseUrl(): String {
        return when (MODE) {
            "production" -> API_PROD
            "staging" -> API_TEST
            else -> API_TEST
        }
    }

    fun applicationBaseUrl(): String {
        return when (MODE) {
            "production" -> APP_PROD
            "staging" -> APP_TEST
            else -> APP_TEST
        }
    }
}