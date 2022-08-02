package com.example.cryptoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.login.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val MDBRepo = MDBRepositoryRetrofit("96d31308896f028f63b8801331250f03")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bttnLogin.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val token = MDBRepo.getNewTokenParsed()
                println("getNewTokenParsed() ran")
                val credentials = Credentials("robertyopeso", "filme123", token.requestToken)
                println("credentials: $credentials")
                val login = MDBRepo.login(credentials)
                println("login() ran")
                val session = MDBRepo.createSession(login)
                println("createSession() ran")
                val invalidate = MDBRepo.invalidateSession(session)
                println("invalidateSession() ran")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}