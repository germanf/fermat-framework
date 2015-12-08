package com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_new.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStatus;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchaseNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_new.exceptions.CantCreateCustomerBrokerNewPurchaseNegotiationTransactionException;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_new.exceptions.CantCreateCustomerBrokerNewSaleNegotiationTransactionException;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_new.exceptions.CantGetCustomerBrokerNewNegotiationTransactionException;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_new.exceptions.CantGetListCustomerBrokerNewNegotiationTransactionException;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_new.interfaces.CustomerBrokerNew;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_new.interfaces.CustomerBrokerNewManager;
import com.bitdubai.fermat_cbp_api.layer.network_service.NegotiationTransmission.interfaces.NegotiationTransmissionManager;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_new.developer.bitdubai.version_1.database.CustomerBrokerNewNegotiationTransactionDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_new.developer.bitdubai.version_1.exceptions.CantNewPurchaseNegotiationTransactionException;

import java.util.List;
import java.util.UUID;

/**
 * Created by Yordin Alayn on 08.12.15.
 */

public class CustomerBrokerNewManagerImpl implements CustomerBrokerNewManager {

    /*Represent the Negotiation Purchase*/
    private CustomerBrokerPurchaseNegotiation                   customerBrokerPurchaseNegotiation;

    /*Represent the Negotiation Sale*/
    private CustomerBrokerSaleNegotiation                       customerBrokerSaleNegotiation;

    /*Represent the Network Service*/
    private NegotiationTransmissionManager                      negotiationTransmissionManager;

    /*Represent the Transaction database DAO */
    private CustomerBrokerNewNegotiationTransactionDatabaseDao  customerBrokerNewNegotiationTransactionDatabaseDao;

    /*Represent the Transaction Negotiation Purchase*/
    private CustomerBrokerNewPurchaseNegotiationTransaction customerBrokerNewPurchaseNegotiationTransaction;

    /*Represent the Transaction Negotiation Sale*/
    private CustomerBrokerNewSaleNegotiationTransaction customerBrokerNewSaleNegotiationTransaction;

    public CustomerBrokerNewManagerImpl(
        CustomerBrokerPurchaseNegotiation                   customerBrokerPurchaseNegotiation,
        CustomerBrokerSaleNegotiation                       customerBrokerSaleNegotiation,
        NegotiationTransmissionManager                      negotiationTransmissionManager,
        CustomerBrokerNewNegotiationTransactionDatabaseDao  customerBrokerNewNegotiationTransactionDatabaseDao
    ){
        this.customerBrokerPurchaseNegotiation                  = customerBrokerPurchaseNegotiation;
        this.customerBrokerSaleNegotiation                      = customerBrokerSaleNegotiation;
        this.negotiationTransmissionManager                     = negotiationTransmissionManager;
        this.customerBrokerNewNegotiationTransactionDatabaseDao = customerBrokerNewNegotiationTransactionDatabaseDao;
    }

    @Override
    public void createCustomerBrokerNewPurchaseNegotiationTranasction(CustomerBrokerPurchaseNegotiation negotiationPurchase) throws CantCreateCustomerBrokerNewPurchaseNegotiationTransactionException {
        try {

            customerBrokerNewPurchaseNegotiationTransaction = new CustomerBrokerNewPurchaseNegotiationTransaction(
                    customerBrokerPurchaseNegotiation,
                    negotiationTransmissionManager,
                    customerBrokerNewNegotiationTransactionDatabaseDao
            );
            customerBrokerNewPurchaseNegotiationTransaction.newPurchaseNegotiationTranasction();

        } catch (CantNewPurchaseNegotiationTransactionException e){
                throw new CantCreateCustomerBrokerNewPurchaseNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantCreateCustomerBrokerNewPurchaseNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CREATE CUSTOMER BROKER NEW PURCHASE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        } catch (Exception e){
            throw new CantCreateCustomerBrokerNewPurchaseNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantCreateCustomerBrokerNewPurchaseNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CREATE CUSTOMER BROKER NEW PURCHASE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        }
    }

    @Override
    public void createCustomerBrokerNewSaleNegotiationTranasction(CustomerBrokerPurchaseNegotiation negotiation) throws CantCreateCustomerBrokerNewSaleNegotiationTransactionException {

        try {

            customerBrokerNewPurchaseNegotiationTransaction = new CustomerBrokerNewPurchaseNegotiationTransaction(
                    customerBrokerPurchaseNegotiation,
                    negotiationTransmissionManager,
                    customerBrokerNewNegotiationTransactionDatabaseDao
            );
            customerBrokerNewPurchaseNegotiationTransaction.newPurchaseNegotiationTranasction();

        } catch (CantNewPurchaseNegotiationTransactionException e){
            throw new CantCreateCustomerBrokerNewSaleNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantCreateCustomerBrokerNewSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CREATE CUSTOMER BROKER NEW SALE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        } catch (Exception e){
            throw new CantCreateCustomerBrokerNewSaleNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantCreateCustomerBrokerNewSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CREATE CUSTOMER BROKER NEW SALE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        }

    }

    @Override
    public CustomerBrokerNew getCustomerBrokerNewNegotiationTranasction(UUID transactionId) throws CantGetCustomerBrokerNewNegotiationTransactionException{
        return null;
    }

    @Override
    public List<CustomerBrokerNew> getAllCustomerBrokerNewNegotiationTranasction() throws CantGetListCustomerBrokerNewNegotiationTransactionException{
        return null;
    }

}
