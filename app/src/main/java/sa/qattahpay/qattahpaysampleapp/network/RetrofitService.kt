package sa.qattahpay.qattahpaysampleapp.network

import kotlinx.coroutines.Deferred
import retrofit2.http.*
import sa.qattahpay.qattahpaysampleapp.model.ApiResponse
import sa.qattahpay.qattahpaysampleapp.model.QattahRequest
import sa.qattahpay.qattahpaysampleapp.utils.Constants

interface RetrofitService {

    @POST("orders")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    fun createNewQattahOrderAsync(
        @Header("Authorization") token: String = Constants.qattahPayToken(),
        @Body body: QattahRequest,
    ): Deferred<ApiResponse>

    @GET("orders/{order_id}")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    fun getOrderPaymentStatusAsync(
        @Header("Authorization") token: String = Constants.qattahPayToken(),
        @Path(value = "order_id") orderId: String
    ): Deferred<ApiResponse>
}