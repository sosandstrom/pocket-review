package com.wadpam.rnr.dao;

import com.wadpam.rnr.domain.DFeedback;
import net.sf.mardao.core.Filter;

/**
 * Implementation of Business Methods related to entity DFeedback.
 * This (empty) class is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-12-26T15:11:12.247+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DFeedbackDaoBean 
	extends GeneratedDFeedbackDaoImpl
		implements DFeedbackDao 
{

    // Delete all user feedback create before the provided timestamp
    public int deleteAllUpdatedBefore(Long timestamp) {

        int numberDeleted;
        if (null == timestamp) {
            Iterable<Long> keys = queryAllKeys();
            numberDeleted = delete(null, keys);
        } else {
            // TODO How to return older then
            Filter filter = null;
            Iterable<DFeedback> dFeedbackOnlyKeys =
                    queryIterable(true, 0, -1, null, null, COLUMN_NAME_UPDATEDDATE, true, null, false, filter);

            numberDeleted = delete(dFeedbackOnlyKeys);
        }

        return numberDeleted;
    }


    // Get all user feedback create after the provided timestamp
    public Iterable<DFeedback> queryUpdatedAfter(Long timestamp) {

        if (null == timestamp) {
            return queryAll();
        } else {
            // TODO How to return newer then
            Filter filter = null;
            return queryIterable(false, 0, -1, null, null, COLUMN_NAME_UPDATEDDATE, false, null, false, filter);
        }
    }

}
