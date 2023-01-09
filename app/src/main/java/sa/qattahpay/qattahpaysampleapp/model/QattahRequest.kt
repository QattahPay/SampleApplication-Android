package sa.qattahpay.qattahpaysampleapp.model

data class QattahRequest(
    val reference: String, // unique id for the order
    val callback_url: String, // will be called after successful payment
    val amount: Float // amount to be paid
)