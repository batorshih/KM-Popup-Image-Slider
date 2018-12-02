package com.kodmap.app.library.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kodmap.app.library.R
import com.kodmap.app.library.constant.ScaleType
import com.kodmap.app.library.model.BaseItem
import com.kodmap.app.library.ui.KmRelativeLayout
import com.kodmap.app.library.ui.zoomableImaveView.KmZoomableImageView
import com.kodmap.app.library.ui.zoomableImaveView.Util.enableTls12OnPreLollipop
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.Interceptor
import java.util.*
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException


class PopupSliderAdapter : PagerAdapter() {

    private var mImageScaleType: ImageView.ScaleType = ScaleType.FIT_CENTER
    private var mIsZoomable: Boolean = false
    lateinit var mLoadingView: View
    private val itemList = ArrayList<BaseItem>()

    fun setItemList(itemList: List<BaseItem>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = View.inflate(container.context, R.layout.item_slider, null) as KmRelativeLayout
        val imageView = itemView.findViewById<KmZoomableImageView>(R.id.km_iv_item_slider)

        if (::mLoadingView.isInitialized) {
            itemView.addLoadingLayout(mLoadingView)
            imageView.setLoadingLayout(mLoadingView)
            imageView.enableLoading()
        }

        imageView.scaleType = mImageScaleType
        imageView.isZoomable = mIsZoomable
        var okHttpClient = OkHttpClient.Builder()
        val okHttpDownloader = OkHttp3Downloader(enableTls12OnPreLollipop(okHttpClient).build())
        val picasso = Picasso.Builder(container.context).downloader(okHttpDownloader).build()
        if (itemList[position].imageUrl == null) {
            picasso
                    .load(itemList[position].drawableId!!)
                    .into(imageView, object : Callback {
                        override fun onSuccess() {
                            imageView.disableLoading()
                        }

                        override fun onError(e: java.lang.Exception?) {
                            imageView.disableLoading()
                        }
                    })
        } else {
            picasso.load(itemList[position].imageUrl)
                    .into(imageView, object : Callback {
                        override fun onSuccess() {
                            imageView.disableLoading()
                        }

                        override fun onError(e: java.lang.Exception?) {
                            imageView.disableLoading()
                        }
                    })
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    fun setScaleType(type: ImageView.ScaleType) {
        mImageScaleType = type
    }

    fun setLoadingView(mLoadingView: View?) {
        if (mLoadingView != null) {
            this.mLoadingView = mLoadingView
        }
    }

    fun setIsZoomable(bool: Boolean) {
        mIsZoomable = bool
    }


}