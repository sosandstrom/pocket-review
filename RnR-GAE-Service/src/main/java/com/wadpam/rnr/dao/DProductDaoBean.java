package com.wadpam.rnr.dao;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.search.*;
import com.google.appengine.api.search.Index;
import com.wadpam.rnr.domain.DProduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.wadpam.rnr.service.RnrService;
import net.sf.mardao.api.dao.Expression;
import net.sf.mardao.core.CursorPage;

/**
 * Implementation of Business Methods related to entity DProduct.
 * This (empty) class is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-05T20:54:54.772+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DProductDaoBean 
	extends GeneratedDProductDaoImpl
		implements DProductDao 
{

    private static final String LOCATION_INDEX = "locationIndex";


    // Find most liked products
    @Override
    public CursorPage<DProduct, String> queryMostLiked(int limit, Serializable cursorKey) {
        return queryMost(COLUMN_NAME_LIKECOUNT, limit, cursorKey);
    }

    protected CursorPage<DProduct, String> queryMost(String columnName, int limit, Serializable cursorKey) {
        return queryPage(false, limit, null, null, columnName, false, null, false, cursorKey);
    }

    // Find most thumbs up products
    @Override
    public CursorPage<DProduct, String> queryMostThumbsUp(int limit, Serializable cursorKey) {
        return queryMost(COLUMN_NAME_THUMBSUP, limit, cursorKey);
    }

    // Find most thumbs up products
    @Override
    public CursorPage<DProduct, String> queryMostThumbsDown(int limit, Serializable cursorKey) {
        return queryMost(COLUMN_NAME_THUMBSDOWN, limit, cursorKey);
    }

    // Find most commented products
    @Override
    public CursorPage<DProduct, String> queryMostCommented(int limit, Serializable cursorKey) {
        return queryMost(COLUMN_NAME_COMMENTCOUNT, limit, cursorKey);
    }

    // Find most rated products
    @Override
    public CursorPage<DProduct, String> queryMostRated(int limit, Serializable cursorKey) {
        return queryMost(COLUMN_NAME_RATINGCOUNT, limit, cursorKey);
    }

    // Find products with highest average rating
    @Override
    public CursorPage<DProduct, String> queryTopRated(int limit, Serializable cursorKey) {
        return queryMost(COLUMN_NAME_RATINGAVERAGE, limit, cursorKey);
    }


    // Persist and index a product based in location (needed for location based search)
    @Override
    public String persist(DProduct dProduct) {

        // Index the geo location
        if (null != dProduct.getLocation()) {
            GeoPoint geoPoint = new GeoPoint(dProduct.getLocation().getLatitude(), dProduct.getLocation().getLongitude());
            Document.Builder locationBuilder = Document.newBuilder()
                    .setId(dProduct.getProductId())
                    .addField(Field.newBuilder().setName("location").setGeoPoint(geoPoint))
                    .addField(Field.newBuilder().setName("averageRating").setNumber(dProduct.getRatingAverage().getRating()))
                    .addField(Field.newBuilder().setName("likeCount").setNumber(dProduct.getLikeCount()))
                    .addField(Field.newBuilder().setName("thumbsUp").setNumber(dProduct.getThumbsUp()));

            getLocationIndex().add(locationBuilder.build());
        }

        return super.persist(dProduct);
    }

    // Build location index
    private com.google.appengine.api.search.Index getLocationIndex() {
        IndexSpec indexSpec = IndexSpec.newBuilder()
                .setName(LOCATION_INDEX)
                .setConsistency(Consistency.PER_DOCUMENT)
                .build();
        return SearchServiceFactory.getSearchService().getIndex(indexSpec);
    }

    // Search for nearby places
    @Override
    public CursorPage<DProduct, String> searchForNearby( int pageSize, String cursor, Float latitude, Float longitude,
                                         int radius, RnrService.SortOrder sortOrder) {

        // Build the query string
        String queryString = String.format("distance(location, geopoint(%f, %f)) < %d", latitude, longitude, radius);

        // Sort expression
        SortExpression sortExpression = null;
        switch (sortOrder) {
            case DISTANCE:
                String sortString = String.format("distance(location, geopoint(%f, %f))", latitude, longitude);
                sortExpression = SortExpression.newBuilder()
                        .setExpression(sortString)
                        .setDirection(SortExpression.SortDirection.ASCENDING)
                        .setDefaultValueNumeric(radius + 1)
                        .build();
                break;
            case TOP_RATED:
                sortExpression = SortExpression.newBuilder()
                        .setExpression("averageRating")
                        .setDirection(SortExpression.SortDirection.DESCENDING)
                        .setDefaultValue("")
                        .build();
                break;
            case MOST_LIKED:
                sortExpression = SortExpression.newBuilder()
                        .setExpression("likeCount")
                        .setDirection(SortExpression.SortDirection.DESCENDING)
                        .setDefaultValue("")
                        .build();
                break;
            case MOST_THUMBS_UP:
                sortExpression = SortExpression.newBuilder()
                        .setExpression("thumbsUp")
                        .setDirection(SortExpression.SortDirection.DESCENDING)
                        .setDefaultValue("")
                        .build();
                break;
        }

        // Options
        QueryOptions options = null;
        QueryOptions.Builder builder = QueryOptions.newBuilder()
                .setSortOptions(SortOptions.newBuilder().addSortExpression(sortExpression))
                .setLimit(pageSize);

        if (null != cursor)
            builder.setCursor(com.google.appengine.api.search.Cursor.newBuilder().build(cursor));

        // Build query
        com.google.appengine.api.search.Query query = com.google.appengine.api.search.Query.newBuilder()
                .setOptions(options)
                .build(queryString);

        return searchInIndexWithQuery(query, getLocationIndex());
    }

    // Search in index for a query
    private CursorPage<DProduct, String> searchInIndexWithQuery(com.google.appengine.api.search.Query query, Index index) {

        CursorPage<DProduct, String> cursorPage = null;

        try {
            // Query the index.
            Results<ScoredDocument> results = index.search(query);

            // Collect all the primary keys
            Collection<String> ids = new ArrayList<String>();
            for (ScoredDocument document : results) {
                ids.add(document.getId());
            }

            if (ids.size() != 0) {
                // We got results, get the places from datastore
                Iterator<DProduct> dProductIterator = queryByPrimaryKeys(null, ids).iterator();

                Collection<DProduct> dProducts = new ArrayList<DProduct>(ids.size());
                while (dProductIterator.hasNext()) {
                    dProducts.add(dProductIterator.next());
                }

                // Build a cursor page
                cursorPage = new CursorPage<DProduct, String>();
                cursorPage.setCursorKey(results.getCursor().toWebSafeString());
                cursorPage.setItems(dProducts);

            }
        } catch (SearchException e) {
            if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
                LOG.error("Search index failed");
                // TODO: Error handling missing
            }
            throw new SearchException("Searching index failed");
        }

        return cursorPage;
    }
}
