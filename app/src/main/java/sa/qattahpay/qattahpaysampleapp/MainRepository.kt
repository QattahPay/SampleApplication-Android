package sa.qattahpay.qattahpaysampleapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sa.qattahpay.qattahpaysampleapp.model.ApiResponse
import sa.qattahpay.qattahpaysampleapp.model.QattahRequest
import sa.qattahpay.qattahpaysampleapp.network.RetrofitBuilder
import sa.qattahpay.qattahpaysampleapp.network.SocketHelper
import timber.log.Timber

class MainRepository {

    suspend fun createNewOrder(ref_id: String, callback_url: String, amount: Float): ApiResponse? {
        Timber.tag(TAG).d("createNewOrders($ref_id, $callback_url, $amount)")
        var apiResponse: ApiResponse?
        withContext(Dispatchers.IO) {
            apiResponse = RetrofitBuilder.retrofitService.createNewQattahOrderAsync(
                body = QattahRequest(ref_id, callback_url, amount)
            ).await()
        }
        return apiResponse
    }

    suspend fun orderPaymentStatus(orderId: String): ApiResponse? {
        Timber.tag(TAG).d("orderPaymentStatus($orderId)")
        var apiResponse: ApiResponse?
        withContext(Dispatchers.IO) {
            apiResponse = RetrofitBuilder.retrofitService.getOrderPaymentStatusAsync(
                orderId = orderId
            ).await()
        }
        return apiResponse
    }

    fun startSocketListener(onNewPaymentEvent: (orderId: String) -> Unit) {
        SocketHelper.startSocketListener(onNewPaymentEvent)
    }

    fun stopSocketListener() {
        SocketHelper.stopSocketListener()
    }

    companion object {
        private val TAG = MainRepository::class.java.simpleName
    }
}