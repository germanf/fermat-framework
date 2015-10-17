package com.bitdubai.fermat_api.layer.dmp_network_service.intra_user.interfaces;

import com.bitdubai.fermat_api.layer.dmp_module.intra_user.interfaces.IntraUserInformation;

/**
 * Created by MAtias Furszyfer on 2015.10.15..
 */
public interface IntraUserFactory {

    /**
     * Construct intraUSer
     *
     * @param publicKey
     * @param intraUserName
     * @param profileImage
     * @return
     */
    public IntraUserInformation constructIntraUser(String publicKey,String intraUserName, byte[] profileImage);
}
