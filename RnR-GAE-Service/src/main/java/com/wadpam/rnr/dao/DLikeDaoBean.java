package com.wadpam.rnr.dao;

import com.google.appengine.api.datastore.Query;
import com.wadpam.rnr.domain.DLike;
import net.sf.mardao.core.Filter;

import java.util.*;

/**
 * Implementation of Business Methods related to entity DLike.
 * This (empty) class is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-05T20:54:54.772+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DLikeDaoBean 
	extends GeneratedDLikeDaoImpl
		implements DLikeDao {

    public static final int MAX_IN_OPERATION_FILTER_PARAM = 30;

    @Override
    // Find likes done by user on product
    public DLike findByProductIdUsername(String productId, String username) {
        final Filter filter1 = createEqualsFilter(COLUMN_NAME_PRODUCTID, productId);
        final Filter filter2 = createEqualsFilter(COLUMN_NAME_USERNAME, username);

        return findUniqueBy(filter1, filter2);
    }


    @Override
    // Find like key done by user on product
    public Long findKeyByProductIdUsername(String productId, String username) {
        final Filter filter1 = createEqualsFilter(COLUMN_NAME_PRODUCTID, productId);
        final Filter filter2 = createEqualsFilter(COLUMN_NAME_USERNAME, username);

        return findUniqueKeyBy(filter1, filter2);
    }


    // Find likes done by user on a list of products
    @Override
    public Iterable<DLike> findByProductIdsUsername(Collection<String> productIds, String username) {

        // Create filters
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(createEqualsFilter(COLUMN_NAME_USERNAME, username));
        filters.add(new Filter(COLUMN_NAME_PRODUCTID, Query.FilterOperator.IN, productIds));

        Iterable<DLike> iterable = queryIterable(false, 0, -1, null, null,
                null, true,
                null, false,
                filters.toArray(new Filter[filters.size()]));

        return iterable;
    }

    // Find likes done by user on a list of products
    @Override
    public Iterable<Long> findKeysByProductIdsUsername(Collection<String> productIds, String username) {

        // Create filters
        Collection<Filter> filters = new ArrayList<Filter>();
        filters.add(createEqualsFilter(COLUMN_NAME_USERNAME, username));
        filters.add(new Filter(COLUMN_NAME_PRODUCTID, Query.FilterOperator.IN, productIds));

        Iterable<Long> iterable = queryIterableKeys(-1, 1000, null, null,
                COLUMN_NAME_USERNAME, true,
                null, false,
                filters.toArray(new Filter[filters.size()]));

        return iterable;
    }

    
    /**
         * {@inheritDoc}
         */
        @Override
        public Iterable<DLike> queryByProductIds(Collection<String> productIds) {
            final List<DLike> dLikes = new ArrayList<DLike>();
            final List<String> productIdList = new ArrayList<String>(productIds);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(">> Total product IDs: {}, Max elements of collection param: {}", productIdList.size(), MAX_IN_OPERATION_FILTER_PARAM);
            }

            int fromIndex = 0;
            while(fromIndex < productIds.size()) {
                int toIndex = fromIndex + MAX_IN_OPERATION_FILTER_PARAM;
                if (toIndex > productIds.size()) {
                    toIndex = productIds.size();
                }

                List<String> subProductIds = productIdList.subList(fromIndex, toIndex);
                Filter filterProductIds = new Filter(COLUMN_NAME_PRODUCTID, Query.FilterOperator.IN, subProductIds);
                List<DLike> subDLikes = (List<DLike>) queryIterable(false, 0, -1, null, null, null, false, null, false, filterProductIds);
                dLikes.addAll(subDLikes);

                if (LOG.isDebugEnabled()) {
                    LOG.debug(">> fromIndex: {}, toIndex: {}, Found DLikes: {}", new Object[]{fromIndex, toIndex, subDLikes.size()});
                }

                fromIndex = toIndex;
            }
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(">> Total DLikes: {}",  dLikes.size());
            }

            return dLikes; 
        }
      

}
