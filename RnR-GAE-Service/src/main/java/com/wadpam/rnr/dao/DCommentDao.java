package com.wadpam.rnr.dao;


import com.google.appengine.api.datastore.Key;
import com.wadpam.rnr.domain.DComment;
import net.sf.mardao.core.CursorPage;

import java.io.Serializable;
import java.util.Collection;

/**
 * Business Methods interface for entity DComment.
 * This interface is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-05T20:54:54.772+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface DCommentDao extends GeneratedDCommentDao {

    /**
     * The a datastore key.
     * @param id The id of the comment
     * @return a datastore ket
     */
    public Key createKey(Long id);

}