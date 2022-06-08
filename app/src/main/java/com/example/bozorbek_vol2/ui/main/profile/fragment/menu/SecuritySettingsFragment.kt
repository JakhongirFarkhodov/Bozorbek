package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import kotlinx.android.synthetic.main.fragment_security_settings.*


class SecuritySettingsFragment : BaseProfileFragment() {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        update_password_button.setOnClickListener {
            Toast.makeText(this.requireContext(), "Click", Toast.LENGTH_LONG).show()
            viewModel.setStateEvent(event = ProfileStateEvent.UpdateProfilePassword(
                old_password = edT_old_password.text.toString(),
                new_password = edT_new_password.text.toString(),
                confirm_new_password = edT_confirm_password.text.toString()
            ))
        }
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null)
            {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.response?.let { event ->
                        event.peekContent()?.let { response ->
                            response.message?.let { message ->
                                if (message.equals("Пароль успешно обнавлен"))
                                {
                                    findNavController().popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        }
        catch (e:Exception)
        {
            Log.d(TAG, "onAttach: ${context} must implement onDataStateChangeListener")
        }
    }

}