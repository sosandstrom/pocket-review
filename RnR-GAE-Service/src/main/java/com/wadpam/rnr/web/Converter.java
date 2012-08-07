package com.wadpam.rnr.web;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.wadpam.rnr.domain.*;
import com.wadpam.rnr.json.*;
import net.sf.mardao.api.domain.AEDPrimaryKeyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implement various methods for converting from domain object to JSON objects
 * @author mlv
 */
public class Converter {

    static final Logger LOG = LoggerFactory.getLogger(Converter.class);


    // Various convert methods for converting between domain to json objects
    protected static JProduct convert(DProduct from, HttpServletRequest request) {
        if (null == from) {
            return null;
        }
        final JProduct to = new JProduct();
        convert(from, to);

        to.setId(from.getProductId());
        to.setLocation(convert(from.getLocation()));
        to.setRatingCount(from.getRatingCount());
        to.setRatingSum(from.getRatingSum());
        to.setRatingAverage(from.getRatingAverage().getRating());
        to.setLikeCount(from.getLikeCount());
        to.setCommentCount(from.getCommentCount());

        // Figure out the base url
        String baseUrl = null;
        Pattern pattern = Pattern.compile("(^.*)/product");
        Matcher matcher = pattern.matcher(request.getRequestURL().toString());
        if (matcher.find()) {
            baseUrl = matcher.group(1);

            // Set links
            to.setRatingsURL(baseUrl + "/rating?productId=" + to.getId());
            to.setLikesURL(baseUrl + "/like?productId=" + to.getId());
            to.setCommentsURL(baseUrl + "/comment?productId=" + to.getId());
        }

        return to;
    }

    protected static JRating convert(DRating from, HttpServletRequest request) {
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

    protected static JLike convert(DLike from, HttpServletRequest request) {
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

    protected static JComment convert(DComment from, HttpServletRequest request) {
        if (null == from) {
            return null;
        }
        final JComment to = new JComment();
        convert(from, to);

        to.setId(from.getId().toString());
        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());
        to.setComment(from.getComment());

        return to;
    }

    protected static JFavorites convert(DFavorites from, HttpServletRequest request) {
        if (null == from) {
            return null;
        }
        final JFavorites to = new JFavorites();
        convert(from, to);

        to.setUsername(from.getUsername());
        to.setProductIds(from.getProductIds());

        return to;
    }

    protected static JLocation convert(GeoPt from) {
        if (null == from) {
            return null;
        }

        JLocation to = new JLocation(from.getLatitude(), from.getLongitude());

        return to;
    }

    protected static <T extends AEDPrimaryKeyEntity> JBaseObject convert(T from, HttpServletRequest request) {
        if (null == from) {
            return null;
        }

        JBaseObject returnValue;
        if (from instanceof DProduct) {
            returnValue = convert((DProduct) from, request);
        }
        else if (from instanceof DRating) {
            returnValue = convert((DRating) from, request);
        }
        else if (from instanceof DLike) {
            returnValue = convert((DLike) from, request);
        }
        else if (from instanceof DComment) {
            returnValue = convert((DComment) from, request);
        }
        else if (from instanceof DFavorites) {
            returnValue = convert((DFavorites) from, request);
        }
        else {
            throw new UnsupportedOperationException("No converter for " + from.getKind());
        }

        return returnValue;
    }

    protected static <T extends AEDPrimaryKeyEntity> Collection<?> convert(Collection<T> from,
                                                                           HttpServletRequest request) {
        if (null == from) {
            return null;
        }

        final Collection<Object> to = new ArrayList<Object>(from.size());

        for(T wf : from) {
            to.add(convert(wf, request));
        }

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
