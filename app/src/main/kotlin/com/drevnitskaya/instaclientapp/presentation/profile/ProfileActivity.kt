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
import com.drevnitskaya.instaclientapp.data.entities.Profile
import com.drevnitskaya.instaclientapp.extensions.loadImage
import com.drevnitskaya.instaclientapp.extensions.showSnackbar
import com.drevnitskaya.instaclientapp.presentation.login.LoginActivity
import com.drevnitskaya.instaclientapp.presentation.profile.adapter.FeedAdapter
import com.drevnitskaya.instaclientapp.presentation.profile.adapter.PaginationListener
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModel()
    private val adapterMedia = FeedAdapter(onRetryClicked = {
        viewModel.loadMoreFeed()
    })

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
            glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapterMedia.getItemViewType(position)) {
                        FeedAdapter.FeetItemType.FEED_ITEM.value -> 1
                        else -> 2
                    }
                }
            }
            layoutManager = glm
            adapter = adapterMedia
            addOnScrollListener(
                PaginationListener(layoutManager = glm,
                    onShouldLoadMore = { localVisibleRectBottomPosition, itemHeight ->
                        viewModel.loadMoreFeedIfNeeded(localVisibleRectBottomPosition, itemHeight)
                    })
            )
        }
        profileErrorState.onRetryClicked = {
            viewModel.loadProfileContent()
        }
        profileLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            showProgress.observe(this@ProfileActivity, Observer { shouldShow ->
                profileProgress.visibility = if (shouldShow) View.VISIBLE else View.GONE
            })
            showUserInfo.observe(this@ProfileActivity, Observer { profile ->
                TransitionManager.beginDelayedTransition(profileRoot)
                val visibility = profile?.let {
                    setUserData(profile)
                    View.VISIBLE
                } ?: View.GONE
                setProfileContentVisibility(visibility)
            })
            showFeed.observe(this@ProfileActivity, Observer { feed ->
                adapterMedia.feed = feed
            })
            showLoadMoreFeed.observe(this@ProfileActivity, Observer { shouldShow ->
                adapterMedia.showLoadMore = shouldShow
            })
            showLoadMoreError.observe(this@ProfileActivity, Observer { shouldShow ->
                adapterMedia.showLoadMoreError = shouldShow
            })
            openLogin.observe(this@ProfileActivity, Observer {
                val intent = LoginActivity.getStartIntent(this@ProfileActivity)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            })
            showErrorState.observe(this@ProfileActivity, Observer { shouldShow ->
                TransitionManager.beginDelayedTransition(profileRoot)
                profileErrorState.visibility = if (shouldShow) View.VISIBLE else View.GONE
            })
            showLogoutFailed.observe(this@ProfileActivity, Observer {
                showSnackbar(profileRoot, getString(R.string.profile_logoutFailed))
            })
        }
    }

    private fun setProfileContentVisibility(visibility: Int) {
        profileAppBar.visibility = visibility
        profileFeed.visibility = visibility
    }

    private fun setUserData(profile: Profile) {
        profileImage.loadImage(null, profile.profilePictureUrl)
        profileFullName.text = profile.fullName
        profileBio.text = profile.bio
        profileFollowersCount.text = "${profile.counts?.follows ?: 0}"
        profileFollowingCount.text = "${profile.counts?.followedBy ?: 0}"
    }
}