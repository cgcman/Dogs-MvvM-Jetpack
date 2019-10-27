package com.grdj.dogs.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

import com.grdj.dogs.R
import com.grdj.dogs.databinding.FragmentDetailBinding
import com.grdj.dogs.util.getProgressDrawable
import com.grdj.dogs.util.loadImage
import com.grdj.dogs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var dogUuid = 0
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBindng: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBindng = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBindng.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        arguments?.let{
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }

        viewModel.fetch(dogUuid)

        //Log.e("ID", ""+dogUuid)

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.dogLiveData.observe(this, Observer { dog ->
            dog?.let {
                dataBindng.dog = dog
                /*dogName.text = dog.dogBreed
                dogTemperament.text = dog.temperament
                dogPurpose.text = dog.breedFor
                dogLifespan.text = dog.lifeSpn
                context?.let { dogImage.loadImage(dog.imageUrl, getProgressDrawable(it)) }*/
            }
        })
    }

}
