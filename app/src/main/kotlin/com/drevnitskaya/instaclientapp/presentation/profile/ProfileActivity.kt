package com.drevnitskaya.instaclientapp.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
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
import androidx.core.content.ContextCompat
import com.drevnitskaya.instaclientapp.presentation.profile.adapter.VIEW_TYPE_FEED_ITEM


class ProfileActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModel()
    private val adapterFeed = FeedAdapter(onRetryClicked = {
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
        profileRoot.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        profileAppBar.setOnApplyWindowInsetsListener { view, windowInsets ->
            view.updatePadding(top = windowInsets.systemWindowInsetTop)
            windowInsets
        }
        profileFeed.setOnApplyWindowInsetsListener { view, windowInsets ->
            view.updatePadding(bottom = windowInsets.systemWindowInsetBottom)
            windowInsets
        }
        profileFeed.apply {
            val glm = GridLayoutManager(context, 2)
            glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapterFeed.getItemViewType(position)) {
                        VIEW_TYPE_FEED_ITEM -> 1
                        else -> 2
                    }
                }
            }
            layoutManager = glm
            adapter = adapterFeed
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
        profileRefreshLayout.setOnRefreshListener {
            viewModel.refreshContent()
        }
        profileLogout.setOnClickListener {
            confirmLogoutPopup().show()
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            showProgress.observe(this@ProfileActivity, Observer { shouldShow ->
                profileProgress.visibility = if (shouldShow) View.VISIBLE else View.GONE
            })
            showRefreshing.observe(this@ProfileActivity, Observer { shouldShow ->
                profileRefreshLayout.isRefreshing = shouldShow
            })
            showRefreshingFailedState.observe(this@ProfileActivity, Observer { error ->
                showSnackbar(
                    profileRoot,
                    getString(error.errorMsgRes),
                    getString(R.string.profile_retry),
                    actionListener = {
                        viewModel.refreshContent()
                    }
                )
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
                adapterFeed.feed = feed
            })
            showEmptyFeedState.observe(this@ProfileActivity, Observer { shouldShow ->
                showNoFeedMessage(shouldShow, R.string.profile_emptyFeed)
            })
            showFeedErrorState.observe(this@ProfileActivity, Observer { shouldShow ->
                showNoFeedMessage(shouldShow, R.string.profile_loadFeedError)
            })
            showOfflineModeMessage.observe(this@ProfileActivity, Observer {
                showSnackbar(profileRoot, getString(R.string.profile_offlineMode))
            })
            showLoadMoreFeed.observe(this@ProfileActivity, Observer { shouldShow ->
                adapterFeed.showLoadMore = shouldShow
            })
            showLoadMoreError.observe(this@ProfileActivity, Observer { shouldShow ->
                adapterFeed.showLoadMoreError = shouldShow
            })
            openLogin.observe(this@ProfileActivity, Observer {
                val intent = LoginActivity.getStartIntent(this@ProfileActivity)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            })
            showErrorState.observe(this@ProfileActivity, Observer { error ->
                profileErrorState.visibility = if (error != null) {
                    TransitionManager.beginDelayedTransition(profileRoot)
                    profileErrorState.setErrorMessage(error.errorMsgRes)
                    View.VISIBLE
                } else {
                    View.GONE
                }
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

    private fun showNoFeedMessage(shouldShow: Boolean, @StringRes msgResId: Int? = null) {
        profileNoFeedMessage.visibility = if (shouldShow && msgResId != null) {
            profileNoFeedMessage.text = getString(msgResId)
            TransitionManager.beginDelayedTransition(profileRoot)
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun confirmLogoutPopup(): AlertDialog {
        return AlertDialog.Builder(this)
            .setMessage(R.string.profile_logoutMsg)
            .setPositiveButton(R.string.shared_yes) { dialog, _ ->
                dialog.dismiss()
                viewModel.logout()
            }.setNegativeButton(R.string.shared_no) { dialog, _ ->
                dialog.dismiss()
            }.create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(
                            this@ProfileActivity,
                            R.color.woodyBrown
                        )
                    )
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(
                            this@ProfileActivity,
                            R.color.woodyBrown
                        )
                    )
                }
            }
    }
}