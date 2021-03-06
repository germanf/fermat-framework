package com.bitdubai.sub_app.crypto_customer_identity.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.interfaces.CryptoCustomerIdentityModuleManager;
import com.bitdubai.sub_app.crypto_customer_identity.R;
import com.bitdubai.sub_app.crypto_customer_identity.util.FragmentsCommons;
import com.edmodo.cropper.CropImageView;

/**
 * Developed by abicelis on 15/06/16.
 * Edited by Penny for the new Crypto Customer Design on 26/07/2016
 */
public class CryptoCustomerImageCropperFragment extends AbstractFermatFragment<ReferenceAppFermatSession<CryptoCustomerIdentityModuleManager>, ResourceProviderManager> implements View.OnClickListener {


    //Constants
    private static final int IMAGE_WIDTH = 400;
    private static final int IMAGE_HEIGHT = 400;
    private static final int IMAGE_COMPRESSION_PERCENTAGE = 30;

    //UI
    CropImageView cropImageView;


    //DATA
    Enum<Activities> backActivity;
    Bitmap originalImage;
    Bitmap croppedImage;

    public CryptoCustomerImageCropperFragment() {
    }

    public static CryptoCustomerImageCropperFragment newInstance() {
        return new CryptoCustomerImageCropperFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Capture data from session, then clean it.
        originalImage = (Bitmap) appSession.getData(FragmentsCommons.ORIGINAL_IMAGE);
        appSession.removeData(FragmentsCommons.ORIGINAL_IMAGE);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.cci_fragment_crop_image_v2, container, false);

        cropImageView = (CropImageView) layout.findViewById(R.id.cbi_crop_image_view);
        cropImageView.setImageBitmap(originalImage);
        cropImageView.setGuidelines(2);

        ImageButton cropButton = (ImageButton) layout.findViewById(R.id.cbi_crop_button);
        ImageButton rotateButton = (ImageButton) layout.findViewById(R.id.cbi_rotate_button);
        ImageButton cancelButton = (ImageButton) layout.findViewById(R.id.cbi_cancel_button);

        cropButton.setOnClickListener(this);
        rotateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return layout;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.cbi_crop_button) {

            //Crop image
            croppedImage = cropImageView.getCroppedImage();

            if (croppedImage.getHeight() >= 80) {


                //Scale it to default size (IMAGE_WIDTH x IMAGE_HEIGHT)
                croppedImage = Bitmap.createScaledBitmap(croppedImage, IMAGE_WIDTH, IMAGE_HEIGHT, true);

                //Compress it
                byte[] croppedImageByteArray = ImagesUtils.toCompressedByteArray(croppedImage, IMAGE_COMPRESSION_PERCENTAGE);

                //Save it to session
                appSession.setData(FragmentsCommons.CROPPED_IMAGE, croppedImageByteArray);

                goBackToCallerActivity();

            } else
                Toast.makeText(getActivity(), getResources().getString(R.string.cropped_small), Toast.LENGTH_SHORT).show();

        }

        if (i == R.id.cbi_rotate_button)
            cropImageView.rotateImage(90);

        if (i == R.id.cbi_cancel_button)
            goBackToCallerActivity();

    }

    private void goBackToCallerActivity() {
        changeActivity(Activities.CBP_SUB_APP_CRYPTO_CUSTOMER_IDENTITY_CREATE_IDENTITY, appSession.getAppPublicKey());
    }
}