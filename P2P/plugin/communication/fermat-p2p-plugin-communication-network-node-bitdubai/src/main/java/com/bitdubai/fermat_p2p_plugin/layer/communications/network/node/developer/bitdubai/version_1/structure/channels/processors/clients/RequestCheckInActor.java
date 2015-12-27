/*
 * @#RequestCheckInActor.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.RequestProfileCheckInMsg;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.RespondProfileCheckInMsj;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.WebSocketChannelServerEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedActorsHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInActor;

import org.jboss.logging.Logger;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.RequestCheckInActor</code>
 * process all messages received the type <code>MessageType.REQUEST_CHECK_IN_NETWORK_SERVICE</code><p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class RequestCheckInActor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(RequestCheckInActor.class.getName());

    /**
     * Constructor whit parameter
     *
     * @param webSocketChannelServerEndpoint register
     */
    public RequestCheckInActor(WebSocketChannelServerEndpoint webSocketChannelServerEndpoint) {
        super(webSocketChannelServerEndpoint, PackageType.REQUEST_CHECK_IN_ACTOR);
    }


    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        LOG.info("Processing new package received");

        String channelIdentityPrivateKey = getChannel().getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get("");
        ActorProfile actorProfile = null;

        try {

            RequestProfileCheckInMsg messageContent = (RequestProfileCheckInMsg) packageReceived.getContent();

            /*
             * Create the method call history
             */
            methodCallsHistory(getGson().toJson(messageContent.getProfileToRegister()), destinationIdentityPublicKey);

            /*
             * Validate if content type is the correct
             */
            if (messageContent.getMessageContentType() == MessageContentType.JSON){

                /*
                 * Obtain the profile of the actor
                 */
                actorProfile = (ActorProfile) messageContent.getProfileToRegister();

                /*
                 * CheckedInActor into data base
                 */
                insertCheckedInActor(actorProfile);

                /*
                 * CheckedActorsHistory into data base
                 */
                insertCheckedActorsHistory(actorProfile);

                /*
                 * If all ok, respond whit success message
                 */
                RespondProfileCheckInMsj respondProfileCheckInMsj = new RespondProfileCheckInMsj(RespondProfileCheckInMsj.STATUS.SUCCESS, RespondProfileCheckInMsj.STATUS.SUCCESS.toString(), actorProfile.getIdentityPublicKey());
                Package packageRespond = Package.createInstance(respondProfileCheckInMsj, packageReceived.getNetworkServiceTypeSource(), PackageType.RESPOND_CHECK_IN_CLIENT, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getBasicRemote().sendObject(packageRespond);

            }

        }catch (Exception exception){

            try {

                LOG.error(exception.getMessage());

                /*
                 * Respond whit fail message
                 */
                RespondProfileCheckInMsj respondProfileCheckInMsj = new RespondProfileCheckInMsj(RespondProfileCheckInMsj.STATUS.FAIL, exception.getLocalizedMessage(), actorProfile.getIdentityPublicKey());
                Package packageRespond = Package.createInstance(respondProfileCheckInMsj, packageReceived.getNetworkServiceTypeSource(), PackageType.RESPOND_CHECK_IN_CLIENT, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getBasicRemote().sendObject(packageRespond);

            } catch (IOException iOException) {
                LOG.error(iOException.getMessage());
            } catch (EncodeException encodeException) {
                LOG.error(encodeException.getMessage());
            }

        }

    }

    /**
     * Create a new row into the data base
     *
     * @param actorProfile
     * @throws CantInsertRecordDataBaseException
     */
    private void insertCheckedInActor(ActorProfile actorProfile) throws CantInsertRecordDataBaseException {

        /*
         * Create the CheckedInActor
         */
        CheckedInActor checkedInActor = new CheckedInActor();
        checkedInActor.setIdentityPublicKey(actorProfile.getIdentityPublicKey());
        checkedInActor.setActorType(actorProfile.getActorType());
        checkedInActor.setAlias(actorProfile.getAlias());
        checkedInActor.setName(actorProfile.getName());
        checkedInActor.setPhoto(actorProfile.getPhoto());
        checkedInActor.setExtraData(actorProfile.getExtraData());
        checkedInActor.setNsIdentityPublicKey(actorProfile.getNsIdentityPublicKey());

        //Validate if location are available
        if (actorProfile.getLocation() != null){
            checkedInActor.setLatitude(actorProfile.getLocation().getLatitude());
            checkedInActor.setLongitude(actorProfile.getLocation().getLongitude());
        }

        /*
         * Save into the data base
         */
        getDaoFactory().getCheckedInActorDao().create(checkedInActor);

    }

    /**
     * Create a new row into the data base
     *
     * @param actorProfile
     * @throws CantInsertRecordDataBaseException
     */
    private void insertCheckedActorsHistory(ActorProfile actorProfile) throws CantInsertRecordDataBaseException {

        /*
         * Create the CheckedActorsHistory
         */
        CheckedActorsHistory checkedActorsHistory = new CheckedActorsHistory();
        checkedActorsHistory.setIdentityPublicKey(actorProfile.getIdentityPublicKey());
        checkedActorsHistory.setActorType(actorProfile.getActorType());
        checkedActorsHistory.setAlias(actorProfile.getAlias());
        checkedActorsHistory.setName(actorProfile.getName());
        checkedActorsHistory.setPhoto(actorProfile.getPhoto());
        checkedActorsHistory.setExtraData(actorProfile.getExtraData());
        checkedActorsHistory.setCheckType(CheckedActorsHistory.CHECK_TYPE_IN);

        //Validate if location are available
        if (actorProfile.getLocation() != null){
            checkedActorsHistory.setLastLatitude(actorProfile.getLocation().getLatitude());
            checkedActorsHistory.setLastLongitude(actorProfile.getLocation().getLongitude());
        }

        /*
         * Save into the data base
         */
        getDaoFactory().getCheckedActorsHistoryDao().create(checkedActorsHistory);

    }

}
