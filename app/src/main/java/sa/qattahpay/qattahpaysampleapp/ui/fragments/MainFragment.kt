package sa.qattahpay.qattahpaysampleapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import sa.qattahpay.qattahpaysampleapp.MainViewModel
import sa.qattahpay.qattahpaysampleapp.base.BaseFragment
import sa.qattahpay.qattahpaysampleapp.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : BaseFragment() {

    override val viewModel: MainViewModel by viewModel()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.tag(TAG).d("onCreateView()")
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        setupBinding()
        setupObservers()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag(TAG).d("onDestroyView()")
        _binding = null
    }

    override fun setupBinding() {
        Timber.tag(TAG).d("setupBinding()")
        with(binding) {
            pay.setOnClickListener {
                val amountStr = amount.text.toString()
                val callBackUrl = callbackUrl.text.toString()
                val refIdStr = refId.text.toString()
                viewModel.pay(refIdStr, callBackUrl, amountStr.toFloat())
                viewModel.setPaymentStarted(true)
            }
        }
    }

    override fun setupObservers() {
        Timber.tag(TAG).d("setupObservers()")

        viewModel.progressing.observe(viewLifecycleOwner) {
            Timber.tag(TAG).d("viewModel.progressing: $it")
            if (it) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        }

        viewModel.newOrderResponse.observe(viewLifecycleOwner) {
            Timber.tag(TAG).d("viewModel.newOrderResponse: $it")
            if (it?.data?.order != null) {
                viewModel.startListener()
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToWebFragment(
                        it.data.order
                    )
                )
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}