//
//  PocketReviewer.h
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "Rating.h"

/** @name Creating and starting the Reviewer */

/**
 A lightweight, easy to use and ready to go review and rating service tailored for mobile apps.
 
 Before you can use the SDK you need to register a unique domain name at http://pocket-reviews.appspot.com.
 
 All methods are asynchronous using GCD and will return immediately. The application will be informed about the outcome 
 of a method by providing a completion block.
 */
@interface PocketReviewer : NSObject


/**
 Specify the radius to use during nearby searches.
 */
typedef enum {
  kDefaultRadius = 1,
  kSmallestRadius = 2,
  kMediumRadius = 3,
  kLargestRadius = 4,
} NearbyRadius;


/** @name Start reviewing */

/**
 Get the shared reviewer. 
 
 The reviewer must be started before it can be used.
 @return Shared reviewer
 */
+ (PocketReviewer*)sharedReviewer;


/**
 Start reviewing by configuring the service.
 @param url The URL to the review service
 @param domain The unique domain name. The app developer need to register a unique domain name at http://pocket-reviews.appspot.com.
 @param anonymous If YES all review and rating requests will be anonymous. 
 If NO a unique user id will be generated that will be used in all requests. The user id will be persisted in user preferences. 
 @param error An optional error message
 @return YES if the reviewer was successfuly started
 */
- (BOOL)startReviewingWithServiceUrl:(NSURL*)url domain:(NSString*)domain anonymousUser:(BOOL)anonymous withError:(NSError**)error;


/** @name Ratings */

/**
 Rate an item.
 
 The domain can be configured to only allow non-anonymous users to rate the same item once at http://pocket-reviews.appspot.com.
 @param itemId The unique item being rated
 @param rating The rating value 1-5
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block;


/**
 Rate an item with a specified latitude and longitude.
 
 The domain can be configured to only allow non-anonymous users to rate the same item once at http://pocket-reviews.appspot.com.
 
 Only non-anonymous users will be able to get their own ratings.
 
 Please note that it is the rated items location what should be provided, not the device current location.
 @param itemId The unique item being rated
 @param latitude The latitude of the item being rated
 @param longitude The longitude of the item being rated
 @param rating The rating value 1-5
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId forLatitude:(float)latitude longitude:(float)longitude 
      withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block;
 

/**
 Get the average rating and the number of ratings for a specified item.
 @param itemId The unique item id
 @param block A block that will be executed when the request completes or fails
 */
- (void)averageRatingForItem:(NSString*)itemId completionBlock:(void(^)(Rating*, NSError*))block;


/**
 Get the average rating and the number of ratings for a list of items.
 @param itemIds An array of items
 @param block A block that will be executed when the request completes or fails
  */
- (void)averageRatingForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the top average ratings.
 
 The returned ratings will be sorted on average rating.
 @param maxNumberOfResults The maximum number of results the service should return, e.g. specify 10 to get the top 10 list
 @param block A block that will be executed when the request completes or fails
 @
 */
- (void)topAverageRatings:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get top nearby average ratings using the device location provided by Google.
 
 The latitude and logitude automatically provided by GAE will be used as the device location. 
 The location provided by Google is most likely on city level.
 
 Items must be rated using the items latitude and longitude for this method to return the average ratings, 
 otherwise en empty list will be returned.
 
 The returned ratings will be sorted on average rating.
 @param radius The radius to search within
 @param maxNumberOfResults The maximum number of results the service should return
 @param block A block that will be executed when the request completes or fails
 */
- (void)nearbyTopAverageRatingsWithinRadius:(NearbyRadius)radius maxNumberOfResults:(NSInteger)maxNumberOfResults 
                            completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get nearby items with a minimum average rating using the device location provided by the application.
 
 Items must be rated using the items latitude and longitude for this method to return the average ratings, 
 otherwise en empty list will be returned.
 
 The returned ratings will be sorted on average rating.
 @param latitude The devices latitude
 @param longitude The device longitude
 @param radius The radius to search within
 @param maxNumberOfResults The maximum number of results the service should return
 @param block A block that will be executed when the request completes or fails
 */
- (void)nearbyTopAverageRatingsForLatitude:(float)latitude longitude:(float)longitude withinRadius:(NearbyRadius)radius 
                        maxNumberOfResults:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get my ratings. 
 
 This method will only return ratings if non-anonymous ratings have been used, otherwise nil will be returned
 @param block A block that will be executed when the request completes or fails
 */
- (void)myRatingsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/** @name Reviews */

/**
 Add a review comment to an item.
 
 The domain can be configured to only allow non-anonymous users to review the same item once at http://pocket-reviews.appspot.com.
 
 Only non-anonymous users will be able to get their own reviews and delete them.
 @param itemId The unique item being reviewed
 @param review The review comment
 @param block A block that will be executed when the request completes or fails
 */
- (void)reviewItem:(NSString*)itemId withReview:(NSString*)review completionBlock:(void(^)(NSError*))block;


/**
 Get all reviews for an item.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)reviewsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Delete the review written by the current user.
 
 This method will only work if non-anonymous review was done.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)deleteMyReviewForItem:(NSString*)itemId completionBlock:(void(^)(NSError*))block;


/**
 Get all my reviews.
 
 This method will only return reviwes if non-anonymous reviews have been used, otherwise nil will be returned
 @param block A block that will be executed when the request completes or fails
 */
- (void)myReviewsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/** @name Properties */

/**
 A unique user id. 
 
 If set the id will be included in all review and rating requests.
 
 The domain can be configured to only allow non-anonymous users to rate the same item once at http://pocket-reviews.appspot.com.
 
 The reviewer will generate an unqiue user id the first time it is created and store it in user preferences unless anonymous is used. 
 If the application have a better way of uniquely indentifing users, e.g. through user registration or login, it can set its own unqiue user id here.
 */
@property (nonatomic, retain) NSString* userId;


/**
 If dry run is set to YES, review and rating requests will not be sent to the service. 
 
 The result will be written to the console and default/random data will be returned. Useful during development and debugging.
 */
@property (nonatomic) BOOL dryRun;


/** 
 Future methods
 - (void)likeItem:(NSString*)itemId completionBlock:(void(^)(NSError*))block;
 - (void)likesForItem:(NSString*)itemId completionBlock:(void(^)(NSNumber*, NSError*))block;
 - (void)likesForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block;
 */


// TODO Inconsistency for what data is returned in different REST endpoints. There is only one rating and review response o
// TODO When to retun nil and empty array? Maybe combine get rating and get ratings
// TODO Method that returns a combined average rating and all reviews. Maybe an inner object?
// TODO delete review
// TODO Ids for reviews? rating? like?
// TODO use jsonORM




@end
