package sa.qattahpay.qattahpaysampleapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sa.qattahpay.qattahpaysampleapp.R
import sa.qattahpay.qattahpaysampleapp.databinding.ActivityMainBinding
import sa.qattahpay.qattahpaysampleapp.utils.interfaces.OnBackPressed

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
        (fragment as? OnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}