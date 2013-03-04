package com.wadpam.rnr.web;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.open.web.BaseConverter;
import com.wadpam.rnr.dao.DQuestionDao;
import com.wadpam.rnr.dao.DQuestionDaoBean;
import com.wadpam.rnr.domain.*;
import com.wadpam.rnr.json.*;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.core.domain.AbstractLongEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * This class implement various methods for converting from domain object to JSON objects.
 * @author mattiaslevin
 */
public class Converter extends BaseConverter {

    static final Logger LOG = LoggerFactory.getLogger(Converter.class);

    // Need this to convert to and from datastore key
    private DQuestionDao questionDao = new DQuestionDaoBean();


    @Override
    public JBaseObject convertBase(Object from) {
        if (null == from) {
            return null;
        }

        JBaseObject to;
        if (from instanceof DRating) {
            to = convert((DRating) from);
        } else if (from instanceof DLike) {
            to = convert((DLike) from);
        } else if (from instanceof DThumbs) {
            to = convert((DThumbs) from);
        } else if (from instanceof DComment) {
            to = convert((DComment) from);
        } else if (from instanceof DFavorites) {
            to = convert((DFavorites) from);
        } else if (from instanceof DFeedback) {
            to = convert((DFeedback) from);
        } else if (from instanceof DQuestion) {
            to = convert((DQuestion) from);
        }else {
            throw new UnsupportedOperationException(String.format("No converter for:%s" + from.getClass().getSimpleName()));
        }

        return to;
    }

    // Convert product
    public JProduct convert(DProduct from, String baseUri) {
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
        to.setRatingsURL(UriComponentsBuilder.fromUriString(baseUri).
                path("/rating").queryParam("productId", "{id}").buildAndExpand(to.getId()).toUriString());
        to.setLikesURL(UriComponentsBuilder.fromUriString(baseUri).
                path("/like").queryParam("productId", "{id}").buildAndExpand(to.getId()).toUriString());
        to.setCommentsURL(UriComponentsBuilder.fromUriString(baseUri).
                path("/comment").queryParam("productId", "{id}").buildAndExpand(to.getId()).toUriString());
        to.setThumbsURL(UriComponentsBuilder.fromUriString(baseUri).
                path("/thumbs").queryParam("productId", "{id}").buildAndExpand(to.getId()).toUriString());

        return to;
    }

    // Convert rating
    public JRating convert(DRating from) {
        if (null == from) {
            return null;
        }

        final JRating to = new JRating();
        convert(from, to);

        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());
        to.setRating(from.getRating().getRating());
        to.setComment(from.getComment());

        return to;
    }

    // Convert like
    public JLike convert(DLike from) {
        if (null == from) {
            return null;
        }

        final JLike to = new JLike();
        convert(from, to);

        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());

        return to;
    }

    // Convert thumbs
    public JThumbs convert(DThumbs from) {
        if (null == from) {
            return null;
        }

        final JThumbs to = new JThumbs();
        convert(from, to);

        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());
        to.setValue(from.getValue().intValue());

        return to;
    }

    // Convert comment
    public JComment convert(DComment from) {
        if (null == from) {
            return null;
        }

        final JComment to = new JComment();
        convert(from, to);

        to.setProductId(from.getProductId());
        to.setUsername(from.getUsername());
        to.setComment(from.getComment());

        return to;
    }

    // Convert favorite
    public JFavorites convert(DFavorites from) {
        if (null == from) {
            return null;
        }
        final JFavorites to = new JFavorites();
        convert(from, to);

        to.setUsername(from.getUsername());
        to.setProductIds(from.getProductIds());

        return to;
    }

    // Convert user feedback
    public JFeedback convert(DFeedback from) {
        if (null == from) {
            return null;
        }

        final JFeedback to = new JFeedback();
        convert(from, to);
        to.setTitle(from.getTitle());
        to.setFeedback(from.getFeedback());
        to.setReferenceId(from.getReferenceId());
        to.setCategory(from.getCategory());
        to.setDeviceModel(from.getDeviceModel());
        to.setDeviceOS(from.getDeviceOS());
        to.setDeviceOSVersion(from.getDeviceOSVersion());
        to.setUsername(from.getUsername());
        to.setUserContact(from.getUserContact());
        if (null != from.getLocation()) {
            to.setLocation(convert(from.getLocation()));
        }

        return to;
    }

    // Convert question
    public JQuestion convert(DQuestion from) {
        if (null == from) {
            return null;
        }

        final JQuestion to = new JQuestion();
        convert(from, to);
        // The id need to take the parent into account
        // Convert the datastore key to a string
        to.setId(toString((Key) questionDao.getPrimaryKey(from)));
        to.setProductId(from.getProductId());
        to.setOpUsername(from.getOpUsername());
        to.setQuestion(from.getQuestion());
        to.setTagetUsername(from.getTagetUsername());
        to.setAnswer(from.getAnswer());
        return to;
    }

    // Convert to answer
    // A JQuestion object but will only some properties set
    public JQuestion convertToAnswer(DQuestion from) {
        if (null == from) {
            return null;
        }

        final JQuestion to = new JQuestion();
        convert(from, to);
        // The id need to take the parent into account
        // Convert the datastore key to a string
        to.setId(toString((Key) questionDao.getPrimaryKey(from)));
        to.setTagetUsername(from.getTagetUsername());
        to.setAnswer(from.getAnswer());
        return to;
    }

    // Convert collection of products
    public Collection<JProduct> convert(Collection<DProduct> from, String baseUri) {
        if (null == from) {
            return null;
        }

        final Collection<JProduct> to = new ArrayList<JProduct>(from.size());

        for(DProduct wf : from) {
            to.add(convert(wf, baseUri));
        }

        return to;
    }

    // Convert product iterable
    public Collection<JProduct> convert(Iterable<DProduct> iterable, String baseUri) {
        Iterator<DProduct> iterator = iterable.iterator();

        Collection<JProduct> to = new ArrayList<JProduct>();
        while (iterator.hasNext())
            to.add(convert(iterator.next(), baseUri));

        return to;
    }

    // Convert collection
    public Collection<?> convert(Collection<?> from) {
        if (null == from)
            return new ArrayList<JBaseObject>();

        final Collection<JBaseObject> returnValue = new ArrayList<JBaseObject>();

        JBaseObject to;
        for (Object o : from) {
            to = convertBase(o);
            returnValue.add(to);
        }

        return returnValue;
    }

    public Object convertToAnswers(Iterable<DQuestion> iterable) {
        // This is a special type of JQuestion, we only like to set some properties in the response JSON

        Iterator<DQuestion> iterator = iterable.iterator();

        Collection<JQuestion> to = new ArrayList<JQuestion>();
        while (iterator.hasNext())
            to.add(convertToAnswer(iterator.next()));

        return to;
    }

    // Convert pages
    public JCursorPage<?> convert(CursorPage<?, ?> from) {
        final JCursorPage<JBaseObject> to = new JCursorPage<JBaseObject>();

        to.setPageSize(from.getRequestedPageSize());
        to.setCursorKey(from.getCursorKey());
        to.setItems((Collection<JBaseObject>)convert(from.getItems()));

        return to;
    }


}
