//
//  PocketReviewer.h
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "Rating.h"
#import "Likes.h"


/**
 A lightweight, easy to use and ready to go review and rating service tailored for mobile apps.
 
 Before you can use the SDK you need to register a unique domain name at [http://pocket-reviews.appspot.com](http://pocket-reviews.appspot.com).
 
 The follwing features are provided
 
 * Ratings - Rate a product
 * Reviews - Write a review for a product
 * Likes - Like a product
 * Favorites - Add a product to my favorites
 
 All methods are asynchronous using GCD and will return immediately. The application will be informed about the outcome 
 of a method by providing a completion block.
 
 ###Item id
 Each method must include a unique item/product id provided by the application. It is applications responsibilty to provide 
 this id and ensure uniqueness and consistency through requests. 
 
 ###Anonymous users
 The application can perform all functions either anonymoulsy or providing a unique user identifier. The SDK provide 
 functionality for automatically generating a unique user identifier that will persist through application starts.
 
 ###Location tagged
 The application can optionally supply a position (latitude and longitude) of the item being rated. This will allow 
 the application to perform nearby operations of stored data.
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
 @param rating The rating value. Default value is 1-5 unless another range is set explicitly
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId withRating:(NSInteger)rating completionBlock:(void(^)(Rating*, NSError*))block;


/**
 Rate an item with a specified latitude and longitude.
 
 The domain can be configured to only allow non-anonymous users to rate the same item once at http://pocket-reviews.appspot.com.
 
 Only non-anonymous users will be able to get their own ratings.
 
 Please note that it is the rated items location what should be provided, not the device current location.
 @param itemId The unique item being rated
 @param latitude The latitude of the item being rated
 @param longitude The longitude of the item being rated
 @param rating The rating value. Default value is 1-5 unless another range is set explicitly
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId withLatitude:(float)latitude andLongitude:(float)longitude 
      withRating:(NSInteger)rating completionBlock:(void(^)(Rating*, NSError*))block;
 

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
 Get all individual ratings for an item.
 @param itemId The unique item id
 @param block A block that will be executed when the request completes or fails
 */
- (void)ratingsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the top average ratings.
 
 The returned ratings will be sorted on average rating.
 @param maxNumberOfResults The maximum number of results the service should return, e.g. specify 10 to get the top 10 list
 @param block A block that will be executed when the request completes or fails
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
- (void)topNearbyAverageRatingsWithinRadius:(NearbyRadius)radius maxNumberOfResults:(NSInteger)maxNumberOfResults 
                            completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get top nearby average ratings using the device location provided by the application.
 
 Items must be rated using the items latitude and longitude for this method to return the average ratings, 
 otherwise en empty list will be returned.
 
 The returned ratings will be sorted on average rating.
 @param latitude The devices latitude
 @param longitude The device longitude
 @param radius The radius to search within
 @param maxNumberOfResults The maximum number of results the service should return
 @param block A block that will be executed when the request completes or fails
 */
- (void)topNearbyAverageRatingsForLatitude:(float)latitude andLongitude:(float)longitude withinRadius:(NearbyRadius)radius 
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
 
 This method will only return reviews if non-anonymous reviews have been used, otherwise nil will be returned.
 @param block A block that will be executed when the request completes or fails
 */
- (void)myReviewsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/** @name Likes */

/**
 Like an item.
 
 The domain can be configured to only allow non-anonymous users to like the same item once at http://pocket-reviews.appspot.com.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)likeItem:(NSString*)itemId completionBlock:(void(^)(NSError*))block;


/**
 Like an item with a specified latitude and longitude.
 
 The domain can be configured to only allow non-anonymous users to like the same item once at http://pocket-reviews.appspot.com.
 
 Only non-anonymous users will be able to get their own likes.
 
 Please note that it is the liked items location what should be provided, not the device current location.
 @param itemId The unique item being rated
 @param latitude The latitude of the item being rated
 @param longitude The longitude of the item being rated
 @param block A block that will be executed when the request completes or fails
 */
- (void)likeItem:(NSString *)itemId withLatitude:(float)latitude andLongitude:(float)longitude completionBlock:(void (^)(NSError *))block;


/**
 Get the total number of likes for an item.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)numberOfLikesForItem:(NSString*)itemId completionBlock:(void(^)(Likes*, NSError*))block;


/**
 Get the total number of likes for a list of items.
 @param itemIds A list if items
 @param block A block that will be executed when the request completes or fails
 */
- (void)numberOfLikesForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get all individual likes for an item.
 @param itemId The unique item id
 @param block A block that will be executed when the request completes or fails
 */
- (void)likesForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the a list of items with the highest number of likes.
 @param maxNumberOfResults The maximum number of results the service should return, e.g. specify 10 to get the top 10 list
 @param block A block that will be executed when the request completes or fails
 */
- (void)mostLikedItems:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the top nearby items with the highest number of likes using the device location provided by Google.
 
 The latitude and logitude automatically provided by GAE will be used as the device location. 
 The location provided by Google is most likely on city level.
 
 Items must be liked using the items latitude and longitude for this method to return the number of likes, 
 otherwise en empty list will be returned.
 
 The returned ratings will be sorted on average rating.
 @param radius The radius to search within
 @param maxNumberOfResults The maximum number of results the service should return
 @param block A block that will be executed when the request completes or fails
 */
- (void)mostLikedNearbyItemsWithinRadius:(NearbyRadius)radius maxNumberOfResults:(NSInteger)maxNumberOfResults 
                         completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the top nearby items with the highest number of likes using the device location provided by the application.
 
 Items must be liked using the items latitude and longitude for this method to return the number of likes, 
 otherwise en empty list will be returned.
 
 The returned ratings will be sorted on average rating.
 @param latitude The devices latitude
 @param longitude The device longitude
 @param radius The radius to search within
 @param maxNumberOfResults The maximum number of results the service should return
 @param block A block that will be executed when the request completes or fails
 */
- (void)mostLikedNearbyItemsForLatitude:(float)latitude andLongitude:(float)longitude withinRadius:(NearbyRadius)radius 
                     maxNumberOfResults:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;

/**
 Get all my Likes.
 
 This method will only return Likes if non-anonymous likes have been used, otherwise nil will be retuned.
 @param block A block that will be executed when the request completes or fails
 */
- (void)myLikesWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/** @name Favorites */

/**
 Add an item to my favorites.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)addItemToMyFavorite:(NSString*)itemId completionBlock:(void(^)(NSError*))block;


/**
 Remove an item to my favorites.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)removeItemsFromMyFavorites:(NSString*)itemId completionBlock:(void(^)(NSError*))block;


/* TODO: Should we support this or not
- (void)nearbyFavoriteItemsWithinRadius:(NearbyRadius)radius maxNumberOfResults:(NSInteger)maxNumberOfResults 
                        completionBlock:(void(^)(NSArray*, NSError*))block;


- (void)nearbyFavoriteItemsForLatitude:(float)latitude andLongitude:(float)longitude withinRadius:(NearbyRadius)radius 
                     maxNumberOfResults:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;
*/


/**
 Get my favorites.
 @param block A block that will be executed when the request completes or fails
 */
- (void)myFavoritesWithCompletionBlock:(void(^)(NSArray*, NSError*))block;
   

/** @name Properties */


/**
 Maximum rating.
 
 The maximal rating an item can get in this application context. 
 
 Default value is 5 meaning that an item can have a rating between 1 and 5 (inclusive). Other common value would be 10, allowing a rating between 1 and 10 (inclusive). The SDK will normalize the rating received from and sent to the backend service taking the maxumum rating into account.
 */
@property (nonatomic) NSInteger maximumRating;


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


@end
