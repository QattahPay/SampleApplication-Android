package sa.qattahpay.qattahpaysampleapp.model

import java.io.Serializable

data class Order(
    val id: String?,
    val merchant_id: String?,
    val reference: String?,
    val amount: String?,
    val started_at: String?,
    val is_expired: Boolean?,
    val payment_status: String?,
    val invoices: List<Invoice>?
) : Serializable

data class Data(
    val order: Order?
) : Serializable

data class Links(
    val redirect_to: String?
) : Serializable

data class ApiResponse(
    val successful: Boolean?,
    val data: Data?,
    val links: Links?,
    val message: String?
) : Serializable
