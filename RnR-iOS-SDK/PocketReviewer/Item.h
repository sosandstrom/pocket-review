//
//  Item.h
//  PocketReviewsDemo
//
//  Created by Mattias Levin on 8/30/12.
//
//

#import <Foundation/Foundation.h>

/**
 Item information.
 */
@interface Item : NSObject


/**
 Return an autoreleased Item object'
 @return a new Item object
 */
+ (Item*)item;


/**
 The unique item id.
 */
@property (nonatomic, retain) NSString *itemId;


/**
 The items latitue.
 */
@property (nonatomic) float latitude;


/**
 The items longitude.
 */
@property (nonatomic) float longitude;

/**
 The total number of ratings.
 */
@property (nonatomic) NSInteger numberOfRatings;


/**
 The average rating.
 */
@property (nonatomic) float averageRating;


/**
 The total number of likes.
 */
@property (nonatomic) NSInteger numberOflikes;


/**
 The total number of comments.
 */
@property (nonatomic) NSInteger numberOfComments;

@end
