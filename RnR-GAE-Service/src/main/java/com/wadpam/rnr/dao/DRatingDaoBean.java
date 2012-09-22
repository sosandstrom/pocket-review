package com.wadpam.rnr.dao;

import com.wadpam.rnr.domain.DRating;

/**
 * Implementation of Business Methods related to entity DRating.
 * This (empty) class is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-05T20:54:54.772+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DRatingDaoBean 
	extends GeneratedDRatingDaoImpl
		implements DRatingDao
{
    @Override
    // Find rating done by user on product
    public DRating findByProductIdUsername(String productId, String username) {
        final java.util.Map<String,Object> args = new java.util.HashMap<String,Object>(2);
        args.put(COLUMN_NAME_PRODUCTID, productId);
        args.put(COLUMN_NAME_USERNAME, username);
        return findBy(args);
    }

}
