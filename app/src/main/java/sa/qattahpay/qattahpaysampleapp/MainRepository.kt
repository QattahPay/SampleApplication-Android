package sa.qattahpay.qattahpaysampleapp

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.SocketOptionBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sa.qattahpay.qattahpaysampleapp.model.ApiResponse
import sa.qattahpay.qattahpaysampleapp.model.QattahRequest
import sa.qattahpay.qattahpaysampleapp.network.RetrofitBuilder
import sa.qattahpay.qattahpaysampleapp.utils.Constants
import timber.log.Timber
import java.net.URISyntaxException

class MainRepository {

    private lateinit var _socket: Socket

    suspend fun createNewOrders(ref_id: String, callback_url: String, amount: Float): ApiResponse? {
        Timber.tag(TAG).d("createNewOrders($amount)")
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
        try {
            _socket = IO.socket(
                Constants.PAYMENT_CALLBACK_SOCKET_URL,
                SocketOptionBuilder
                    .builder()
                    .setTransports(arrayOf("websocket"))
                    .build()
            )
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }

        handleSocketEvents(onNewPaymentEvent)
        _socket.connect()
    }

    fun stopSocketListener() {
        _socket.disconnect()
    }

    private fun handleSocketEvents(onNewPaymentEvent: (orderId: String) -> Unit) {
        _socket.on(Socket.EVENT_CONNECT) {
            Timber.tag(TAG).d("%s%s", Socket.EVENT_CONNECT, it.toString())
        }
        _socket.on(Constants.ORDER_PAYMENT_STATUS) {
            Timber.tag(TAG).d("%s%s", Constants.ORDER_PAYMENT_STATUS, it)
            val orderId = it[0].toString()
            onNewPaymentEvent.invoke(orderId)
        }
        _socket.on(Socket.EVENT_DISCONNECT) {
            Timber.tag(TAG).d("%s%s", Socket.EVENT_DISCONNECT, it.toString())
        }
        _socket.on(Socket.EVENT_CONNECT_ERROR) {
            Timber.tag(TAG).e("%s%s", Socket.EVENT_CONNECT_ERROR, it)
        }
    }

    companion object {
        private val TAG = MainRepository::class.java.simpleName
    }
}