//
//  PocketReviewer.h
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "Rating.h"


/**
 A lightweight and ready to go review and rating service tailored for mobile apps. 
 All methods are asynchronous using GCD and will return immediately. The application will be informed about the outcome of a method by providing a completion block.
 */
@interface PocketReviewer : NSObject


/**
 Get the shared reviewer. 
 The reviewer must be started before it can be used.
 @return Shared reviewer
 */
+ (PocketReviewer*)sharedReviewer;


/**
 Start reviewing by configuring the service. The reviewer needs to know the url and domain of the service.
 @param url The URL to the review service
 @param domain The unique domain name
 @param anonymous If YES all review and ratings will be anonymous. If NO a unique user id will be generated that will be used in all requests. The user id will be persisted in user preferences. 
 @param error An optional error message
 @return YES if the reviewer was successfuly started
 */
- (BOOL)startReviewingWithServiceUrl:(NSURL*)url domain:(NSString*)domain anonymous:(BOOL)anonymous withError:(NSError**)error;


/**
 Rate a specific item.
 Non-anonymous users will only be able to rate the same item once.
 @param itemId The unique item being rated
 @param rating The rating value 1-5
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block;


/**
 Rate a specific item with a specified latitud and longitude.
 Non-anonymous users will only be able to rate the same item once.
 @param itemId The unique item being rated
 @param latitude The latitude of the item being rated
 @param longitude The longitude of the item being rated
 @param rating The rating value 1-5
 @param block A block that will be executed when the request completes or fails
 */
- (void)rateItem:(NSString*)itemId forLatitude:(float)latitude andLongitude:(float)longitude withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block;
 

/**
 Get the average and the number of ratings for a specified item.
 @param itemId The unique item id
 @param block A block that will be executed when the request completes or fails
 */
- (void)ratingForItem:(NSString*)itemId completionBlock:(void(^)(Rating*, NSError*))block;


/**
 Get the average and the number of ratings for a list of items.
 @param itemIds An array of items
 @param block A block that will be executed when the request completes or fails
  */
- (void)ratingForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get my ratings. 
 This method will only return ratings if non-anonymous rating have been used, otherwise nil will be returned
 @param block A block that will be executed when the request completes or fails
 */
- (void)myRatingsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Add a review comment to an item.
 @param itemId The unique item being reviewed
 @param review The review comment
 @param block A block that will be executed when the request completes or fails
 */
- (void)reviewItem:(NSString*)itemId withReview:(NSString*)review completionBlock:(void(^)(NSError*))block;


/**
 Get all review comments for an item.
 @param itemId The unique item
 @param block A block that will be executed when the request completes or fails
 */
- (void)reviewsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block;


/**
 Get my review comments. 
 This method will only return reviwes if non-anonymous reviews have been used, otherwise nil will be returned
 @param block A block that will be executed when the request completes or fails
 */
- (void)myReviewsWithCompletionBlock:(void(^)(NSArray*, NSError*))block;


/**
 A unique user id. 
 If set the id will be included in all review and ratings requests. A users will only be able to rate and review the same item once. 
 The reviewer will generate an unqiue user id the first time it is created and store it in user preferences unless anonymous is used. If the application have a better why of uniquely indentifing users, e.g. through user registration or login, it can set its own unqiue user id value here.
 */
@property (nonatomic, retain) NSString* userId;


/**
 If dry run is set to YES, review and rating requests will not be sent to the service. The result will be written to the console and default/random data will be returned. Useful during development and debugging.
 */
@property (nonatomic) BOOL dryRun;


/** 
 Future methods
 - (void)nearbyTopRatingsForLatitude:(double)latitude andLongitude:(double)longitude withRadius:(double)radius completionBlock:(void(^)(NSArray*))block;
 - (void)likeItem:(NSString*)itemId completionBlock:(void(^)(NSError*))block;
 - (void)likesForItem:(NSString*)itemId completionBlock:(void(^)(NSNumber*, NSError*))block;
 - (void)likesForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block;
 */


@end
