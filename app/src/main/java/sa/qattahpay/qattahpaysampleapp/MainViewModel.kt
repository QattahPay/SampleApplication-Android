package sa.qattahpay.qattahpaysampleapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sa.qattahpay.qattahpaysampleapp.model.ApiResponse
import timber.log.Timber

class MainViewModel(
    application: Application,
    private val _mainRepository: MainRepository
) : AndroidViewModel(application) {

    private val _progressing = MutableLiveData<Boolean>()
    val progressing: MutableLiveData<Boolean>
        get() = _progressing

    private val _newOrderResponse = MutableLiveData<ApiResponse>()
    val newOrderResponse: MutableLiveData<ApiResponse>
        get() = _newOrderResponse

    private val _isPaymentSucceeded = MutableLiveData<Boolean>()
    val isPaymentSucceeded: MutableLiveData<Boolean>
        get() = _isPaymentSucceeded

    init {
        Timber.tag(TAG).d("init()")
    }

    fun pay(ref_id: String, callback_url: String, amount: Float) {
        viewModelScope.launch {
            newOrderResponse.value = _mainRepository.createNewOrders(ref_id, callback_url, amount)
        }
    }

    fun startListener() {
        _mainRepository.startSocketListener { orderId ->
            viewModelScope.launch {
                _isPaymentSucceeded.value =
                    _mainRepository.orderPaymentStatus(orderId)?.data?.order?.payment_status == "paid"
            }
        }
    }

    fun stopListener() {
        _mainRepository.stopSocketListener()
    }

    fun setPaymentStarted(isProgressing: Boolean) {
        _progressing.value = isProgressing
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}