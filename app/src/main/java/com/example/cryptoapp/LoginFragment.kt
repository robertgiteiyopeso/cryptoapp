package com.example.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.login.CredentialsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mdbRepo = MDBRepositoryRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bttnLogin.setOnClickListener {

//            //takes too long, just press the button and we worry about actual login when we need it
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.replace(R.id.fragment_container_view_tag, HomeFragment())
//                ?.commit()

            //Try to login
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    //Get new token
                    val token = mdbRepo.getNewTokenParsed()

                    //Login
                    mdbRepo.login(
                        CredentialsModel(
                            binding.tiUsername.editText?.text.toString(),
                            binding.tiPassword.editText?.text.toString(),
                            token.requestToken
                        )
                    )

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container_view_tag, HomeFragment())
                        ?.commit()

                } catch (e: Exception) {
                    Log.e("LoginFragment: ", e.message.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}