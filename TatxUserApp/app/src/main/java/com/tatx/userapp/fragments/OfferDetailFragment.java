package com.tatx.userapp.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tatx.userapp.R;
import com.tatx.userapp.dataset.Product;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class OfferDetailFragment extends Fragment {

	TextView pdtIdTxt;
	TextView pdtNameTxt;
	ImageView pdtImg;
	Activity activity;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;

	Product product;

	public static final String ARG_ITEM_ID = "pdt_detail_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.ic_launcher)
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().build();

		imageListener = new ImageDisplayListener();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_offer_detail, container,
				false);
		findViewById(view);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			product = bundle.getParcelable("singleProduct");
			setProductItem(product);
		}

		return view;
	}

	private void findViewById(View view) {

		pdtNameTxt = (TextView) view.findViewById(R.id.pdt_name);
		pdtIdTxt = (TextView) view.findViewById(R.id.product_id_text);

		pdtImg = (ImageView) view.findViewById(R.id.product_detail_img);
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

	private void setProductItem(Product resultProduct) {
		pdtNameTxt.setText("" + resultProduct.getName());
		pdtIdTxt.setText("Product Id: " + resultProduct.getId());

		imageLoader.displayImage(resultProduct.getImageUrl(), pdtImg, options,
				imageListener);
	}
}
