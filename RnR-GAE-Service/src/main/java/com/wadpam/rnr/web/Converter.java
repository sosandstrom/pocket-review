package com.wadpam.rnr.web;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JLocation;
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
 * @author mattiaslevin
 */
public class Converter {

    static final Logger LOG = LoggerFactory.getLogger(Converter.class);


    // Various convert methods for converting between domain to json objects
    protected static JProduct convert(DProduct from, String baseUrl) {
        if (null == from) {
            return null;
        }
        final JProduct to = new JProduct();
        convert(from, to);

        to.setId(from.getProductId());
        to.setLocation(convert(from.getLocation()));
        to.setRatingCount(from.getRatingCount());
        to.setRatingSum(from.getRatingSum());
        to.setRatingAverage(from.getRatingAverage() != null ? from.getRatingAverage().getRating() : -1);
        to.setLikeCount(from.getLikeCount());
        to.setThumbsUp(from.getThumbsUp());
        to.setThumbsDown(from.getThumbsDown());
        to.setCommentCount(from.getCommentCount());

        // Set links
        to.setRatingsURL(baseUrl + "/rating?productId=" + to.getId());
        to.setLikesURL(baseUrl + "/like?productId=" + to.getId());
        to.setCommentsURL(baseUrl + "/comment?productId=" + to.getId());

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

    protected static JThumbs convert(DThumbs from) {
        if (null == from) {
            return null;
        }
        final JThumbs to = new JThumbs();
        convert(from, to);

        to.setId(from.getId().toString());
        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());
        to.setValue(from.getValue().intValue());

        return to;
    }

    protected static JComment convert(DComment from) {
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

    protected static JFavorites convert(DFavorites from) {
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

    protected static JApp convert(DApp from) {
        if (null == from) {
            return null;
        }
        final JApp to = new JApp();
        convert(from, to);

        to.setDomain(from.getDomainName());

        Collection<String> emails = new ArrayList<String>(from.getAppAdmins().size());
        for (Email email : from.getAppAdmins())
            emails.add(email.getEmail());
        to.setAppAdmins(emails);

        to.setApiUser(from.getApiUser());
        to.setApiPassword(from.getApiPassword());
        to.setDescription(from.getDescription());

        return to;
    }

    protected static JAppAdmin convert(DAppAdmin from) {
        if (null == from) {
            return null;
        }
        final JAppAdmin to = new JAppAdmin();
        convert(from, to);

        to.setAdminId(from.getAdminId());
        to.setEmail(from.getEmail().getEmail());
        to.setName(from.getName());
        to.setAccountStatus(from.getAccountStatus());
        to.setMaxNumberOfApps(from.getMaxNumberOfApps());

        return to;
    }

    protected static <T extends AEDPrimaryKeyEntity> JBaseObject convert(T from) {
        if (null == from) {
            return null;
        }

        JBaseObject returnValue;
        if (from instanceof DRating) {
            returnValue = convert((DRating) from);
        }
        else if (from instanceof DLike) {
            returnValue = convert((DLike) from);
        }
        else if (from instanceof DThumbs) {
            returnValue = convert((DThumbs) from);
        }
        else if (from instanceof DComment) {
            returnValue = convert((DComment) from);
        }
        else if (from instanceof DFavorites) {
            returnValue = convert((DFavorites) from);
        }
        else if (from instanceof DApp) {
            returnValue = convert((DApp) from);
        }
        else if (from instanceof DAppAdmin) {
            returnValue = convert((DAppAdmin) from);
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

    protected static Collection<JProduct> convert(Collection<DProduct> from, String baseUrl) {
        if (null == from) {
            return null;
        }

        final Collection<JProduct> to = new ArrayList<JProduct>(from.size());

        for(DProduct wf : from) {
            to.add(convert(wf, baseUrl));
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
