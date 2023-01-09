package sa.qattahpay.qattahpaysampleapp.base

import androidx.fragment.app.Fragment
import sa.qattahpay.qattahpaysampleapp.MainViewModel
import sa.qattahpay.qattahpaysampleapp.utils.interfaces.OnBackPressed

abstract class BaseFragment : Fragment(), OnBackPressed {

    abstract val viewModel: MainViewModel

    abstract fun setupBinding()

    abstract fun setupObservers()

}