package sa.qattahpay.qattahpaysampleapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import es.dmoral.toasty.Toasty
import im.delight.android.webview.AdvancedWebView
import org.koin.androidx.viewmodel.ext.android.viewModel
import sa.qattahpay.qattahpaysampleapp.MainViewModel
import sa.qattahpay.qattahpaysampleapp.base.BaseFragment
import sa.qattahpay.qattahpaysampleapp.databinding.FragmentWebBinding
import sa.qattahpay.qattahpaysampleapp.model.Order
import sa.qattahpay.qattahpaysampleapp.utils.Constants
import timber.log.Timber

class WebFragment : BaseFragment(), AdvancedWebView.Listener {

    override val viewModel: MainViewModel by viewModel()

    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!

    private val order: Order by lazy {
        WebFragmentArgs.fromBundle(
            requireArguments()
        ).order
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.tag(TAG).d("onCreateView()")
        _binding = FragmentWebBinding.inflate(inflater, container, false)

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
            webView.setListener(activity, this@WebFragment)
            webView.loadUrl(Constants.APPLICATION_URL + order.id)
        }
    }

    override fun setupObservers() {
        Timber.tag(TAG).d("setupObservers()")
        viewModel.isPaymentSucceeded.observe(viewLifecycleOwner) {
            Timber.tag(TAG).d("viewModel.isPaymentSucceeded: $it")
            if (it) {
                Toasty.success(requireContext(), "Payment Succeeded").show()
                val action =
                    WebFragmentDirections.actionWebFragmentToMainFragment()
                findNavController().navigate(action)

                viewModel.stopListener()
            } else {
                Toasty.error(requireContext(), "Payment Failed").show()
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.webView.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        binding.webView.onBackPressed()
        return true
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        Timber.tag(TAG).d("onPageStarted($url)")
    }

    override fun onPageFinished(url: String?) {
        Timber.tag(TAG).d("onPageFinished($url)")
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        Timber.tag(TAG).e("onPageError($errorCode)")
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        Timber.tag(TAG).d("onDownloadRequested($url)")
    }

    override fun onExternalPageRequest(url: String?) {
        Timber.tag(TAG).d("onExternalPageRequest($url)")
    }

    companion object {
        private val TAG = WebFragment::class.java.simpleName
    }
}