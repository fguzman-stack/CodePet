package com.tamagotchi.code.util

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdManager {
    private var rewardedAd: RewardedAd? = null
    private const val TAG = "AdManager"
    
    // Test ID for Rewarded Ad
    private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    fun loadRewardedAd(activity: Activity) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(activity, AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                rewardedAd = ad
            }
        })
    }

    fun showRewardedAd(activity: Activity, onRewardEarned: () -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                onRewardEarned()
                // Load the next ad
                loadRewardedAd(activity)
            }
        } ?: run {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
            // Fallback: If ad not loaded, maybe just allow it for now or tell user to wait
            // For now, let's just trigger reward to not block user in dev
            onRewardEarned()
            loadRewardedAd(activity)
        }
    }
}
