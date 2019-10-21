package com.tatx.userapp.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tatx.userapp.R;
import com.tatx.userapp.activities.GoogleMapDrawerActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.customviews.CustomButton;
import com.tatx.userapp.dataset.Product;
import com.tatx.userapp.fragments.OfferFragment;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



public class ImageSlideAdapter extends PagerAdapter {
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;
	FragmentActivity activity;
	List<Product> products;
	OfferFragment offerFragment;

	public ImageSlideAdapter(FragmentActivity activity, List<Product> products,
							 OfferFragment offerFragment) {
		this.activity = activity;
		this.offerFragment = offerFragment;
		this.products = products;
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.ic_error)
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheInMemory()
				.cacheOnDisc().build();

		imageListener = new ImageDisplayListener();
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.offer_image, container, false);

		ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);
		CustomButton cbUsePromoCode = (CustomButton) view.findViewById(R.id.cb_use_promo_code);

/*
		cbUsePromoCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{

				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(ServiceUrls.RequestParams.PCODE,products.get(position).getName());

				new RetrofitRequester((RetrofitResponseListener) activity).sendStringRequest(ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY,hashMap);

			}
		});
*/


		cbUsePromoCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
/*

				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(ServiceUrls.RequestParams.PCODE,products.get(position).getName());

				new RetrofitRequester((RetrofitResponseListener) activity).sendStringRequest(ServiceUrls.RequestNames.CHECK_PROMO_VALIDITY,hashMap);
*/

				GoogleMapDrawerActivity.currentPoromocode = products.get(position).getName();

				Common.customToast(activity,"Promocode Successfully added to the Current Trip.");

				activity.finish();



			}
		});




		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Bundle arguments = new Bundle();
				Fragment fragment = null;
				Log.d("position adapter", "" + position);
				Product product = (Product) products.get(position);
				arguments.putParcelable("singleProduct", product);

				// Start a new fragment
				fragment = new OfferDetailFragment();
				fragment.setArguments(arguments);

				FragmentTransaction transaction = activity
						.getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.content_frame, fragment,
						OfferDetailFragment.ARG_ITEM_ID);
				transaction.addToBackStack(OfferDetailFragment.ARG_ITEM_ID);
				transaction.commit();*/
			}
		});


		imageLoader.displayImage(
				((Product) products.get(position)).getImageUrl(), mImageView,
				options, imageListener);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private static class ImageDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}