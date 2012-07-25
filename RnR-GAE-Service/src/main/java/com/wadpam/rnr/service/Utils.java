package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.domain.DResult;
import com.wadpam.rnr.json.*;
import net.sf.mardao.api.domain.AEDPrimaryKeyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/24/12
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    static final Logger LOG = LoggerFactory.getLogger(Utils.class);


    // Various convert methods for converting between domain to json objects
    public static JResult convert(DResult from) {
        if (null == from) {
            return null;
        }
        final JResult to = new JResult();
        convert(from, to);

        to.setProductId(from.getProductId());
        to.setRatingCount(from.getRatingCount());
        to.setRatingSum(from.getRatingSum());
        to.setNumberOfLikes(from.getNumberOfLikes());

        // TODO: Set location here

        return to;
    }

    public static JRating convert(DRating from) {
        if (null == from) {
            return null;
        }
        final JRating to = new JRating();
        convert(from, to);

        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());
        to.setLocation(convert(from.getLocation()));  // TODO: Remove location
        to.setRating(from.getRating().getRating());

        return to;
    }

    public static JLike convert(DLike from) {
        if (null == from) {
            return null;
        }
        final JLike to = new JLike();
        convert(from, to);

        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());

        return to;
    }

    protected static JLocation convert(GeoPt from) {
        if (null == from) {
            return null;
        }

        JLocation to = new JLocation(from.getLatitude(), from.getLongitude());

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
        else if (from instanceof DLike) {
            returnValue = convert((DLike) from);
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

}
