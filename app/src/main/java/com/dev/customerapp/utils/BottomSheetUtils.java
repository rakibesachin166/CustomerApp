package com.dev.customerapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.dev.customerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;

public class BottomSheetUtils {
    private static final int REQUEST_IMAGE_CAPTURE = 222;
    private static Uri photoURI;

    public interface ImageSelectionCallback {
        void onImageSelected(File imageFile);

        void onImageSelected(Uri imageUri);
    }

    public static void showImageSelectionBottomSheet(Activity activity, ImageSelectionCallback callback) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.select_image_bottom_sheet, null);

        ImageView cameraImageView = view.findViewById(R.id.camera);
        ImageView galleryImageView = view.findViewById(R.id.gallery);

        galleryImageView.setOnClickListener(v -> {
            selectGallery(activity, callback);
            bottomSheetDialog.dismiss();
        });

        cameraImageView.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private static void selectGallery(@NonNull Activity activity, ImageSelectionCallback callback) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
    }

}
