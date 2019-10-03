package com.drevnitskaya.instaclientapp.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.drevnitskaya.instaclientapp.R
import com.drevnitskaya.instaclientapp.extensions.loadImage
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModel()
    private val adapterMedia = MediaAdapter()

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        profileFeed.apply {
            val glm = GridLayoutManager(context, 2)
            layoutManager = glm
            adapter = adapterMedia
        }

    }

    private fun initViewModel() {
        viewModel.showProgress.observe(this, Observer { shouldShow ->
            profileProgress.visibility = if (shouldShow) View.VISIBLE else View.GONE
        })
        viewModel.showUserInfo.observe(this, Observer { profile ->
            TransitionManager.beginDelayedTransition(profileRoot)
            val visibility = profile?.let {
                profileImage.loadImage(null, it.profilePictureUrl)
                profileFullName.text = it.fullName
                profileBio.text = it.bio
                profileFollowersCount.text = "${it.counts?.follows ?: 0}"
                profileFollowingCount.text = "${it.counts?.followedBy ?: 0}"
                View.VISIBLE
            } ?: View.GONE
            setProfileContentVisibility(visibility)
        })
        viewModel.showFeed.observe(this, Observer { feed ->
            adapterMedia.media = feed
        })
    }

    private fun setProfileContentVisibility(visibility: Int) {
        profileAppBar.visibility = visibility
        profileFeed.visibility = visibility
    }

}