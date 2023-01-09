package sa.qattahpay.qattahpaysampleapp.utils

import sa.qattahpay.qattahpaysampleapp.BuildConfig

object Constants {

    private const val MODE = "production" // or "staging"
    private const val PRODUCTION_SUBDOMAIN = "api"
    private const val STAGING_SUBDOMAIN = "staging-api"
    private const val API_VERSION = "v1"

    private const val BASE_PRODUCTION_URL =
        "https://${PRODUCTION_SUBDOMAIN}.qattahpay.sa/api/${API_VERSION}/merchant-integration/"
    private const val BASE_STAGING_URL =
        "https://${STAGING_SUBDOMAIN}.qattahpay.sa/api/${API_VERSION}/merchant-integration/"

    const val APPLICATION_URL = "https://app.qattahpay.sa/"
    const val PAYMENT_CALLBACK_SOCKET_URL = "http://161.35.70.189:3000/"

    const val ORDER_PAYMENT_STATUS = "payment"

    var qattahPayToken = qattahPayToken()
    var qattahBaseUrl = getBaseUrl()


    private fun qattahPayToken(): String {
        return if (MODE == "production") {
            "Bearer ${BuildConfig.QATTAH_PRODUCTION_TOKEN}"
        } else {
            "Bearer ${BuildConfig.QATTAH_TESTING_TOKEN}"
        }
    }

    private fun getBaseUrl(): String {
        return if (MODE == "production") {
            BASE_PRODUCTION_URL
        } else {
            BASE_STAGING_URL
        }
    }
}