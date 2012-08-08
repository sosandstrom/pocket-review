package com.wadpam.rnr.datastore;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import java.util.ConcurrentModificationException;

/**
 * Class for implementing a finite number of retries when a GAE datastore transaction fail.
 */
public class GaeConcurrentOperationExecutor implements Ordered {

    static final Logger LOG = LoggerFactory.getLogger(GaeConcurrentOperationExecutor.class);

    private static final int DEFAULT_MAX_RETRIES = 3;

    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int order = 1;

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        int numAttempts = 0;
        ConcurrentModificationException concurrentModificationException = null;
        do {
            numAttempts++;
            try {
                LOG.debug("Concurrent operation before");
                return pjp.proceed();
            }
            catch(ConcurrentModificationException exception) {
                LOG.info("Datastore transaction failed due to Concurrent Modification Exception. Attempt: " + numAttempts);
                concurrentModificationException = exception;
            }
            catch (Exception exception) {
                LOG.info("Datastore tansaction thew exception: " + exception.getMessage());
                throw(exception);
            }
        }
        while(numAttempts <= this.maxRetries);

        LOG.info("Datastore transaction failed max number of tries due to Concurrent Modification Exception, not more retries.");
        throw concurrentModificationException;
    }
}
