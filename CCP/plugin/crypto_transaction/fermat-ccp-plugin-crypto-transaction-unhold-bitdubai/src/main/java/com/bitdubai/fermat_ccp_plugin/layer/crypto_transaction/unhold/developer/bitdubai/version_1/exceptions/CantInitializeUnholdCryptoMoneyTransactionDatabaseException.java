package com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.unhold.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * The Class <code>CantInitializeHoldCryptoMoneyTransactionDatabaseException</code>
 * is thrown when an error occurs initializing database
 * <p/>
 *
 * Created by Franklin Marcano on 23/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CantInitializeUnholdCryptoMoneyTransactionDatabaseException extends FermatException {

    public static final String DEFAULT_MESSAGE = "CAN'T INTIALIZE HOLD CASH MONEY TRANSACTION DATABASE EXCEPTION";

    public CantInitializeUnholdCryptoMoneyTransactionDatabaseException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantInitializeUnholdCryptoMoneyTransactionDatabaseException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantInitializeUnholdCryptoMoneyTransactionDatabaseException(final String message) {
        this(message, null);
    }

    public CantInitializeUnholdCryptoMoneyTransactionDatabaseException() {
        this(DEFAULT_MESSAGE);
    }
}