package com.tatx.userapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.adapter.ImageSlideAdapter;
import com.tatx.userapp.commonutills.CirclePageIndicator;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.tatx.userapp.commonutills.PageIndicator;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.dataset.Product;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.network.RetrofitResponseListener;
import com.tatx.userapp.pojos.ApiResponseVo;
import com.tatx.userapp.pojos.PromoListVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OfferFragment extends Fragment implements RetrofitResponseListener{

	public static final String ARG_ITEM_ID = "home_fragment";

	private static final long ANIM_VIEWPAGER_DELAY = 5000;
	private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

	// UI References
	private ViewPager mViewPager;
	TextView imgNameTxt;
	PageIndicator mIndicator;

	AlertDialog alertDialog;

	List<Product> products;
	//RequestImgTask task;
	boolean stopSliding = false;
	String message;

	private Runnable animateViewPager;
	private Handler handler;

	//String url = "http://192.168.3.119:8080/products.json";
	FragmentActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		products=new ArrayList<>();
		new RetrofitRequester(this).sendStringRequest(ServiceUrls.RequestNames.PROMO_LIST,null);

		for (int i=0;i<7;i++) {
//			Product product = new Product();
//			product.setImageUrl("http://web.tatx.com/dev/backend/resources/assets/images/notification/Las-Tunas-Car-Rental-Cuba.png");
//			product.setId(1);
//			product.setName("Anil kumar");
//			products.add(product);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_offer, container, false);
		findViewById(view);

		mIndicator.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction()) {

				case MotionEvent.ACTION_CANCEL:
					break;

				case MotionEvent.ACTION_UP:
					// calls when touch release on ViewPager
					if (products != null && products.size() != 0) {
						stopSliding = false;
						runnable(products.size());
						handler.postDelayed(animateViewPager,
								ANIM_VIEWPAGER_DELAY_USER_VIEW);
					}
					break;

				case MotionEvent.ACTION_MOVE:
					// calls when ViewPager touch
					if (handler != null && stopSliding == false) {
						stopSliding = true;
						handler.removeCallbacks(animateViewPager);
					}
					break;
				}
				return false;
			}
		});

		return view;
	}

	private void findViewById(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		imgNameTxt = (TextView) view.findViewById(R.id.img_name);
	}

	public void runnable(final int size) {
		handler = new Handler();
		animateViewPager = new Runnable() {
			public void run() {
				if (!stopSliding) {
					if (mViewPager.getCurrentItem() == size - 1) {
						mViewPager.setCurrentItem(0);
					} else {
						mViewPager.setCurrentItem(
								mViewPager.getCurrentItem() + 1, true);
					}
					handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}


	@Override
	public void onResume() {
		super.onResume();
		if (products == null||products.size()==0) {
			return;
			//sendRequest();
		} else {

			mViewPager.setAdapter(new ImageSlideAdapter(activity, products,
					OfferFragment.this));

			mIndicator.setViewPager(mViewPager);
			imgNameTxt.setText(""
					+ ((Product) products.get(mViewPager.getCurrentItem()))
							.getName());
			runnable(products.size());
			//Re-run callback
			handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
		}

	}

	
	@Override
	public void onPause() {
		super.onPause();
//		if (task != null)
//			task.cancel(true);
		if (handler != null) {
			//Remove callback
			handler.removeCallbacks(animateViewPager);
		}

	}

	/*private void sendRequest() {
		if (CheckNetworkConnection.isConnectionAvailable(activity)) {
			task = new RequestImgTask(activity);
			task.execute(url);
		} else {
			message = getResources().getString(R.string.no_internet_connection);
			showAlertDialog(message, true);
		}
	}*/

	public void showAlertDialog(String message, final boolean finish) {
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (finish)
							activity.finish();
					}
				});
		alertDialog.show();
	}

	@Override
	public void onResponseSuccess(ApiResponseVo apiResponseVo, HashMap<String, String> requestParams, int requestId) {

		if (apiResponseVo.code != Constants.SUCCESS)
		{

		//	Common.customToast(getActivity(), apiResponseVo.status);

			switch(apiResponseVo.requestname)
			{
				case ServiceUrls.RequestNames.PROMO_LIST:

					break;
			}




			return;


		}



		switch (apiResponseVo.requestname)
		{

			case ServiceUrls.RequestNames.PROMO_LIST:
				PromoListVo promoListVo = Common.getSpecificDataObject(apiResponseVo.data, PromoListVo.class);
				Log.d("list", promoListVo.promo.toString());
				for (int i = 0; i< promoListVo.promo.size(); i++) {
						Product product = new Product();
						product.setImageUrl(promoListVo.promo.get(i).image);
						product.setId(promoListVo.promo.get(i).id);
						product.setName(promoListVo.promo.get(i).promoCode);
						products.add(product);
				}
				if (products == null||products.size()==0) {
					return;
					//sendRequest();
				} else {

					mViewPager.setAdapter(new ImageSlideAdapter(activity, products,
							OfferFragment.this));

					mIndicator.setViewPager(mViewPager);
					imgNameTxt.setText(""
							+ ((Product) products.get(mViewPager.getCurrentItem()))
							.getName());
					runnable(products.size());
					//Re-run callback
					handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
				}

				break;
		}




	}


	private class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				if (products != null) {
					imgNameTxt.setText(""
							+ ((Product) products.get(mViewPager
									.getCurrentItem())).getName());
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
/*
	private class RequestImgTask extends AsyncTask<String, Void, List<Product>> {
		private final WeakReference<Activity> activityWeakRef;
		Throwable error;

		public RequestImgTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Product> doInBackground(String... urls) {
			try {
				JSONObject jsonObject = getJsonObject(urls[0]);
				if (jsonObject != null) {
					boolean status = jsonObject.getBoolean(TagName.TAG_STATUS);

					if (status) {
						JSONObject jsonData = jsonObject
								.getJSONObject(TagName.TAG_DATA);
						if (jsonData != null) {
							products = JsonReader.getHome(jsonData);

						} else {
							message = jsonObject.getString(TagName.TAG_DATA);
						}
					} else {
						message = jsonObject.getString(TagName.TAG_DATA);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return products;
		}

		*//**
		 * It returns jsonObject for the specified url.
		 * 
		 * @param url
		 * @return JSONObject
		 *//*
		public JSONObject getJsonObject(String url) {
			JSONObject jsonObject = null;
			try {
				jsonObject = GetJSONObject.getJSONObject(url);
			} catch (Exception e) {
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(List<Product> result) {
			super.onPostExecute(result);

			if (activityWeakRef != null && !activityWeakRef.get().isFinishing()) {
				if (error != null && error instanceof IOException) {
					message = getResources().getString(R.string.time_out);
					showAlertDialog(message, true);
				} else if (error != null) {
					message = getResources().getString(R.string.error_occured);
					showAlertDialog(message, true);
				} else {
					products = result;
					if (result != null) {
						if (products != null && products.size() != 0) {

							mViewPager.setAdapter(new ImageSlideAdapter(
									activity, products, OfferFragment.this));

							mIndicator.setViewPager(mViewPager);
							imgNameTxt.setText(""
									+ ((Product) products.get(mViewPager
											.getCurrentItem())).getName());
							runnable(products.size());
							handler.postDelayed(animateViewPager,
									ANIM_VIEWPAGER_DELAY);
						} else {
							imgNameTxt.setText("No Products");
						}
					} else {
					}
				}
			}
		}
	}*/
}
