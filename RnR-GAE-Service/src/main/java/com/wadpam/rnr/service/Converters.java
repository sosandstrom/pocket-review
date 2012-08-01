package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.domain.DProduct;
import com.wadpam.rnr.domain.DRating;
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
 * Date: 7/28/12
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Converters {

    static final Logger LOG = LoggerFactory.getLogger(Converters.class);


    // Various convert methods for converting between domain to json objects
    protected static JProductV15 convert(DProduct from) {
        if (null == from) {
            return null;
        }
        final JProductV15 to = new JProductV15();
        convert(from, to);

        to.setId(from.getProductId());
        to.setLocation(convert(from.getLocation()));
        to.setRatingCount(from.getRatingCount());
        to.setRatingSum(from.getRatingSum());
        to.setRatingsURL(""); // TODO: where and how to set this value
        to.setLikeCount(from.getLikeCount());
        to.setLikesURL(""); // TODO: where and how to set this value
        to.setCommentCount(from.getCommentCount());
        to.setCommentsURL(""); // TODO: where and how to set this value

        return to;
    }

    protected static JRating convert(DRating from) {
        if (null == from) {
            return null;
        }
        final JRating to = new JRating();
        convert(from, to);

        to.setId(from.getId().toString());
        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());
        to.setRating(from.getRating().getRating());
        to.setComment(from.getComment());

        return to;
    }

    protected static JLike convert(DLike from) {
        if (null == from) {
            return null;
        }
        final JLike to = new JLike();
        convert(from, to);

        to.setId(from.getId().toString());
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

    protected static <T extends AEDPrimaryKeyEntity> void convert(T from, JBaseObject to) {
        if (null == from || null == to) {
            return;
        }

        to.setId(null != from.getSimpleKey() ? from.getSimpleKey().toString() : null);
        to.setCreatedDate(toLong(from.getCreatedDate()));
        to.setUpdatedDate(toLong(from.getUpdatedDate()));
    }

    protected static <T extends AEDPrimaryKeyEntity> JBaseObject convert(T from) {
        if (null == from) {
            return null;
        }

        JBaseObject returnValue;
        if (from instanceof DProduct) {
            returnValue = convert((DProduct) from);
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

    private static Long toLong(Key from) {
        if (null == from) {
            return null;
        }
        return from.getId();
    }

    private static Long toLong(Date from) {
        if (null == from) {
            return null;
        }
        return from.getTime();
    }

    private static Collection<Long> toLongs(Collection<String> from) {
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

    private static String toString(Key from) {
        if (null == from) {
            return null;
        }
        return Long.toString(from.getId());
    }

    private static Collection<String> toString(Collection<Long> from) {
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
