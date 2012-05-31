/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Rating;
import com.wadpam.rnr.dao.DRatingDao;
import com.wadpam.rnr.dao.DResultDao;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.domain.DResult;
import com.wadpam.rnr.json.JBaseObject;
import com.wadpam.rnr.json.JRating;
import com.wadpam.rnr.json.JResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import net.sf.mardao.api.domain.AEDPrimaryKeyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author os
 */
public class RnrService {
    static final Logger LOG = LoggerFactory.getLogger(RnrService.class);

    private DRatingDao ratingDao;
    private DResultDao resultDao;
    
    /**
     * @param productId 
     * @return the average rating for specified productId
     */
    public JResult addRating(String productId, String username,
            Float latitude, Float longitude, int rating) {
        final GeoPt location = (null != latitude && null != longitude) ? 
                new GeoPt(latitude, longitude) : null;
        
        DRating dr = null;
        int existing = -1;
        
        // specified user can only rate once
        if (null != username) {
            dr = ratingDao.findByProductIdUsername(productId, username);
        }
        
        // create new?
        final boolean create = null == dr;
        if (create) {
            dr = new DRating();
            dr.setProductId(productId);
            dr.setUsername(username);
        }
        else {
            // store existing rating to subtract below
            existing = dr.getRating().getRating();
        }
        
        // upsert
        dr.setRating(new Rating(rating));
        dr.setLocation(location);
        ratingDao.persist(dr);
        
        // update average result
        DResult result = resultDao.findByPrimaryKey(productId);
        if (null == result) {
            result = new DResult();
            result.setProductId(productId);
            result.setRatingCount(1L);
            result.setRatingSum((long)rating);
        }
        else {
            // result exists, and existing rating for user
            if (-1 < existing) {
                result.setRatingSum(result.getRatingSum() - existing + rating);
                // no need to update ratingCount!
            }
            else {
                // result exists, no existing rating for user
                result.setRatingSum(result.getRatingSum() + rating);
                result.setRatingCount(result.getRatingCount() + 1);
            }
            
        }
        resultDao.persist(result);
        
        return convert(result);
    }
    
    public static JResult convert(DResult from) {
        final JResult to = new JResult();
        convert(from, to);
        
        return to;
    }

    public static JRating convert(DRating from) {
        final JRating to = new JRating();
        convert(from, to);
        
        return to;
    }

    public static <T extends AEDPrimaryKeyEntity> void convert(T from, JBaseObject to) {
        if (null == from || null == to) {
            return;
        }

        to.setId(null != from.getSimpleKey() ? from.getSimpleKey().toString() : null);
        to.setCreatedDate(toLong(from.getCreatedDate()));
        to.setUpdatedDate(toLong(from.getUpdatedDate()));
    }

    public static <T extends AEDPrimaryKeyEntity> JBaseObject convert(T from) {
        if (null == from) {
            return null;
        }

        JBaseObject returnValue;
        if (from instanceof DResult) {
            returnValue = convert((DResult) from);
        }
        else if (from instanceof DRating) {
            returnValue = convert((DRating) from);
         }
        else {
            throw new UnsupportedOperationException("No converter for " + from.getKind());
        }

        return returnValue;
    }

    protected static <T extends AEDPrimaryKeyEntity> Collection<?> convert(Collection<T> from) {
        if (null == from) {
            return null;
        }

        final Collection<Object> to = new ArrayList<Object>(from.size());

        for(T wf : from) {
            to.add(convert(wf));
        }

        return to;
    }

    /**
     * @param productId 
     * @return the average rating for specified productId
     */
    public JResult getAverage(String productId) {
        final DResult result = resultDao.findByPrimaryKey(productId);
        
        return convert(result);
    }
    
    public static Long toLong(Key from) {
        if (null == from) {
            return null;
        }
        return from.getId();
    }

    public static Long toLong(Date from) {
        if (null == from) {
            return null;
        }
        return from.getTime();
    }

    public static Collection<Long> toLongs(Collection<String> from) {
        if (null == from) {
            return null;
        }

        final Collection<Long> to = new ArrayList<Long>();

        for(String s : from) {
            try {
                to.add(Long.parseLong(s));
            }
            catch (NumberFormatException sometimes) {
                LOG.warn("Trying to convert non-numeric id: {}", s);
            }
        }

        return to;
    }

    public static String toString(Key from) {
        if (null == from) {
            return null;
        }
        return Long.toString(from.getId());
    }

    public static Collection<String> toString(Collection<Long> from) {
        if (null == from) {
            return null;
        }

        final Collection<String> to = new ArrayList<String>(from.size());

        for(Long l : from) {
            to.add(l.toString());
        }

        return to;
    }

    public void setRatingDao(DRatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public void setResultDao(DResultDao resultDao) {
        this.resultDao = resultDao;
    }
    
}
