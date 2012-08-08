package com.wadpam.rnr.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;


/**
 * An Spring transaction manager implementing the transaction strategy for GAE.
 * @author mlv
 */
// TODO: Currently the transaction is cross-entity group transaction. Needed since all entities are in the root.
public class GaeTransacationManager extends AbstractPlatformTransactionManager {

    static final Logger LOG = LoggerFactory.getLogger(GaeTransacationManager.class);


    @Override
    protected Object doGetTransaction() throws TransactionException {
        LOG.debug("Get transaction");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        TransactionOptions options = TransactionOptions.Builder.withXG(true);
        Transaction txn = datastore.beginTransaction(options);

        return txn;
    }

    @Override
    protected void doBegin(Object Transaction, TransactionDefinition transactionDefinition) throws TransactionException {
        LOG.debug("Begin transaction");
        // The transaction already starts in the doGetTransaction above, do nothing here
    }

    @Override
    protected void doCommit(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
        LOG.debug("Commit transaction");

        Transaction txn = (Transaction)defaultTransactionStatus.getTransaction();
        if (null != txn)
            txn.commit();
        else
            LOG.error("Commit failed. Not able to get the transaction");
    }

    @Override
    protected void doRollback(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
        LOG.debug("**Rollback transaction");

        Transaction txn = (Transaction)defaultTransactionStatus.getTransaction();
        if (null != txn) {
            // Rollback
            txn.isActive();
            txn.rollback();
        }
        else
            LOG.error("Rollback failed. Not able to get the transaction");
    }
}
