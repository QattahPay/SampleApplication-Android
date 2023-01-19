package sa.qattahpay.qattahpaysampleapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import es.dmoral.toasty.Toasty
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

    private val _newOrderResponse = MutableLiveData<ApiResponse?>()
    val newOrderResponse: MutableLiveData<ApiResponse?>
        get() = _newOrderResponse

    private val _isPaymentSucceeded = MutableLiveData<Boolean>()
    val isPaymentSucceeded: MutableLiveData<Boolean>
        get() = _isPaymentSucceeded

    init {
        Timber.tag(TAG).d("init()")
    }

    fun pay(ref_id: String, callback_url: String, amount: Float) {
        viewModelScope.launch {
            try {
                val apiResponse = _mainRepository.createNewOrder(ref_id, callback_url, amount)
                if (apiResponse?.message != null) {
                    Toasty.error(getApplication(), "Error: ${apiResponse.message}").show()
                } else {
                    if (apiResponse?.successful == true) {
                        _newOrderResponse.value = apiResponse
                    } else {
                        _isPaymentSucceeded.value = false
                    }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                Toasty.error(getApplication(), "Error: ${e.message}").show()
            }

            progressing.value = false
        }
    }

    fun startListener() {
        _mainRepository.startSocketListener { orderId ->
            viewModelScope.launch {
                try {
                    _isPaymentSucceeded.value =
                        _mainRepository.orderPaymentStatus(orderId)?.data?.order?.payment_status == "PAID"
                } catch (e: Exception) {
                    Timber.tag(TAG).e(e)
                    Toasty.error(getApplication(), "Error: ${e.message}").show()
                }
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