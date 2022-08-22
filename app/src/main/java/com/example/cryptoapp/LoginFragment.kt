package com.example.cryptoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.cryptoapp.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            requireContext().applicationContext as MovieApplication
        )
    }

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginViewModel = viewModel

        viewModel.checkOldLogin()

        binding.bttnLogin.setOnClickListener {
            //actual login
            viewModel.doLogin()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            loginStateObserver(state)
        }

    }

    private fun loginStateObserver(state: LoginState) {
        when (state) {
            is LoginState.Error -> {
                binding.bttnLogin.text = getString(R.string.login)
                binding.bttnLogin.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondaryColor
                    )
                )
                binding.bttnLogin.isClickable = true

                Toast.makeText(
                    requireContext(),
                    state.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            LoginState.InProgress -> {
                binding.bttnLogin.text = getString(R.string.in_progress)
                binding.bttnLogin.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondaryDarkColor
                    )
                )
                binding.bttnLogin.isClickable = false
            }
            is LoginState.Success -> {
                binding.bttnLogin.text = getString(R.string.login)
                binding.bttnLogin.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondaryColor
                    )
                )
                binding.bttnLogin.isClickable = true

                //Change screen
                findNavController().navigate(
                    R.id.home_action,
                    null,
                    navOptions { popUpTo(R.id.login_fragment) { inclusive = true } })
            }
        }
    }


}