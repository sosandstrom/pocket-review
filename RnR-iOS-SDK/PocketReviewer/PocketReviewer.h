//
//  PocketReviewer.h
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "Item.h"
#import "Rating.h"
#import "Histogram.h"
#import "Like.h"


/**
 A lightweight, easy to use and ready to go review and rating service tailored for mobile apps.
 
 Before you can use the SDK you need to register a unique domain name at [http://pocket-reviews.appspot.com](http://pocket-reviews.appspot.com).
 
 The follwing features are provided
 
 * Ratings - Rate an item and write an optional review comment
 * Likes - Like an item
 * Comments - Write a comment for am item
 * Favorites - Add an item to my favorites
 
 All methods are asynchronous using GCD and will return immediately. The application will be informed about the outcome 
 of a method by providing a completion block.
 
 ###Items id
 Each method must include a unique item id provided by the application. It is the applications responsibilty to provide 
 this id and ensure uniqueness and consistency through requests. 
 
 ###Anonymous users
 The application can perform all functions either anonymoulsy or providing a unique user identifier. The SDK provide 
 functionality for automatically generating a unique user identifier that will persist through application starts and
 upgrades.
 
 ###Location tagged
 The application can optionally supply a position (latitude and longitude) of the product being rated. This will allow 
 the application to perform nearby operations on items.
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
 @param appUser The appUser is generated when registering the app with the backend
 @param appPassword The appUser is the generated when registering the app with the backend
 @param anonymous If YES all review and rating requests will be anonymous. 
 If NO a unique user id will be generated that will be used in all requests. The user id will be persisted in user preferences. 
 @param error An optional error message
 @return YES if the reviewer was successfuly started
 */
- (BOOL)startReviewingWithUrl:(NSURL*)url
                       domain:(NSString*)domain                   
                      appUser:(NSString*)appUser
                  appPassword:(NSString*)appPassword
                anonymousUser:(BOOL)anonymous
                    withError:(NSError**)error;


/** @name Item information */

/**
 Get the item summary for a specific item.
 
 The item summary contain information such as average rating, number of ratings, number of likes etc.
 @param itemId The unique item id
 @param block A block that will be executed when the request completes or fails
 */
- (void)itemWithId:(NSString*)itemId completionBlock:(void(^)(Item*, NSError*))block;


/**
 Get the item summary for a list of items.
 @param itemIds An array of items
 @param block A block that will be executed when the request completes or fails
 */
- (void)itemsWithIds:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the most rated items.
 
 The returned items will be sorted on average rating.
 @param maxNumberOfResults The maximum number of results to return
 @param block A block that will be executed when the request completes or fails
 */
- (void)mostRatedItems:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the top rated items.
 
 The returned ratings will be sorted on average rating.
 @param maxNumberOfResults The maximum number of results to return
 @param block A block that will be executed when the request completes or fails
 */
- (void)topRatedItems:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get nearby top rated items using the device location provided by Google.
 
 The latitude and logitude automatically provided by GAE will be used as the device location.
 The location provided by Google is most likely on city level.
 
 Items must be rated using the items latitude and longitude for this method to return anything.
 
 The returned items will be sorted on average rating.
 @param radius The radius to search within
 @param maxNumberOfResults The maximum number of results the service should return
 @param block A block that will be executed when the request completes or fails
 */
- (void)nearbyTopRatedItemsWithRadius:(NearbyRadius)radius
         maxNumberOfResults:(NSInteger)maxNumberOfResults
            completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get nearby top rated items using the device location provided by the application.
 
 Items must be rated using the items latitude and longitude for this method to return anything.
 
 The returned items will be sorted on average rating.
 @param latitude The devices latitude
 @param longitude The device longitude
 @param radius The radius to search within
 @param maxNumberOfResults The maximum number of results the service should return
 @param block A block that will be executed when the request completes or fails
 */
- (void)nearbyTopRatedItemsForLatitude:(float)latitude
                             longitude:(float)longitude
                                radius:(NearbyRadius)radius
                    maxNumberOfResults:(NSInteger)maxNumberOfResults
                       completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get all items rated my me.
 
 This method will only return items if non-anonymous ratings have been used, otherwise nil will be returned
 @param block A block that will be executed when the request completes or fails
 */
- (void)myRatedItemsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get all items liked by me.
 
 This method will only return items if non-anonymous likes have been used, otherwise nil will be returned.
 @param block A block that will be executed when the request completes or fails
 */
- (void)myLikedItemsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get all items commented by me.
 
 This method will only return items if non-anonymous comments have been used, otherwise nil will be returned.
 @param block A block that will be executed when the request completes or fails
 */
- (void)myCommentedItemsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/** @name Ratings */


/**
 Rate an item.
 
 The domain can be configured to only allow non-anonymous users to rate the same item once at http://pocket-reviews.appspot.com.
 @param itemId The unique item being rated
 @param rating The rating value. Default value is 1-5 unless another range is set explicitly
 @param comment An optional comment associated with the rating
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId
      withRating:(NSInteger)rating
         comment:(NSString*)comment
 completionBlock:(void(^)(Rating*, NSError*))block;


/**
 Rate an item with a specified latitude and longitude.
 
 Please note that it is the rated items location what should be provided, not the device current location.

 The domain can be configured to only allow non-anonymous users to rate the same item once at http://pocket-reviews.appspot.com.
 @param itemId The unique item being rated
 @param latitude The latitude of the item being rated
 @param longitude The longitude of the item being rated
 @param rating The rating value. Default value is 1-5 unless another range is set explicitly
 @param comment An optional comment associated with the rating
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId
    withLatitude:(float)latitude
       longitude:(float)longitude
          rating:(NSInteger)rating
         comment:(NSString*)comment
 completionBlock:(void(^)(Rating*, NSError*))block;

/**
 Get all ratings for a specific item.
 
 This method will return all individual ratings for a specific items.
 It you prefer to get the calculated average ratings query the item resource instead.
 @param itemId The unique item id
 @param block A block that will be executed when the request completes or fails
 */
- (void)ratingsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get the histogram of all rating for a sepcific items.
 @param itemId The unique item id
 @param interval The interval used in the returned histogram
 @param block A block that will be executed when the request completes or fails 
 */
- (void)ratingHistogramForItem:(NSString*)itemId interval:(int)interval completionBlock:(void(^)(Histogram*, NSError*))block;


/** @name Likes */

/**
 Like an item.
 
 The domain can be configured to only allow non-anonymous users to like the same item once at http://pocket-reviews.appspot.com.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)likeItem:(NSString*)itemId completionBlock:(void(^)(NSError*))block;


/**
 Get all individual likes for an item.
 @param itemId The unique item id
 @param block A block that will be executed when the request completes or fails
 */
- (void)likesForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block;


/** @name Comments */

/**
 Add a comment to an item.
 
 Only non-anonymous users will be able to get their own comments and delete them.
 @param itemId The unique item being commented
 @param comment The comment
 @param block A block that will be executed when the request completes or fails
 */
- (void)commentItem:(NSString*)itemId withComment:(NSString*)comment completionBlock:(void(^)(NSError*))block;


/**
 Get all comments for an item.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)commentsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block;


/** @name Favorites */

/**
 Add an item to my favorites.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)addItemToFavorites:(NSString*)itemId completionBlock:(void(^)(NSError*))block;


/**
 Remove an item to my favorites.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)removeItemFromFavorites:(NSString*)itemId completionBlock:(void(^)(NSError*))block;


/**
 Get my favorites.
 @param block A block that will be executed when the request completes or fails
 */
- (void)favoritesWithCompletionBlock:(void(^)(NSArray*, NSError*))block;
   

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
