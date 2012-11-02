package com.wadpam.rnr.dao;

import com.google.appengine.api.datastore.Email;
import com.wadpam.rnr.domain.DApp;


/**
 * Business Methods interface for entity DApp.
 * This interface is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-14T21:35:41.306+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface DAppDao extends GeneratedDAppDao {


    /**
     * Find all apps for a specific app admin
     * @param email the app admin email
     * @return a list of apps
     */
    public Iterable<DApp> queryByAdminEmail(Email email);
}
