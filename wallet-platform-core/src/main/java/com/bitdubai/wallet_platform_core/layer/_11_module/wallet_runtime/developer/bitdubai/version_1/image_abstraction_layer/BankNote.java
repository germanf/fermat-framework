package com.bitdubai.wallet_platform_core.layer._11_module.wallet_runtime.developer.bitdubai.version_1.image_abstraction_layer;

/**
 * Created by ciencias on 15.01.15.
 */
public class BankNote implements Movable, Rotatable, Displayable {

    int mFaceValue;
    BankNoteSide mCurrentSide;
    BankNoteImage mSideFrontImage;
    BankNoteImage mSideBackImage;

    @Override
    public EnvelopeStatus getPosition() {
        return null;
    }

    @Override
    public Void moveTo(Position pPosition) {
        return null;
    }

    @Override
    public void rotateTo(Rotation pRotation) {

    }
}
