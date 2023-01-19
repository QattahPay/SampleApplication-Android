package sa.qattahpay.qattahpaysampleapp.network

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.SocketOptionBuilder
import sa.qattahpay.qattahpaysampleapp.utils.Constants
import timber.log.Timber
import java.net.URISyntaxException

object SocketHelper {

    private lateinit var socket: Socket
    private val TAG = SocketHelper::class.java.simpleName

    fun startSocketListener(onNewPaymentEvent: (orderId: String) -> Unit) {
        try {
            socket = IO.socket(
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
        socket.connect()
    }

    fun stopSocketListener() {
        socket.disconnect()
    }

    private fun handleSocketEvents(onNewPaymentEvent: (orderId: String) -> Unit) {
        socket.on(Socket.EVENT_CONNECT) {
            Timber.tag(TAG).d("%s%s", Socket.EVENT_CONNECT, it.toString())
        }
        socket.on(Constants.ORDER_PAYMENT_STATUS) {
            Timber.tag(TAG).d("%s%s", Constants.ORDER_PAYMENT_STATUS, it)
            val orderId = it[0].toString()
            onNewPaymentEvent.invoke(orderId)
        }
        socket.on(Socket.EVENT_DISCONNECT) {
            Timber.tag(TAG).d("%s%s", Socket.EVENT_DISCONNECT, it.toString())
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Timber.tag(TAG).e("%s%s", Socket.EVENT_CONNECT_ERROR, it)
        }
    }
}