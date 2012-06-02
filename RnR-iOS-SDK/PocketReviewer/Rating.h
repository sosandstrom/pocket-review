//
//  Rating.h
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import <Foundation/Foundation.h>


/**
 The rating of an item.
 */
@interface Rating : NSObject


/**
 Create a new Rating object.
 @return A Rating object
 */
+ (Rating*)rating;


/**
 The average rating of the item.
 */
@property (nonatomic) float averageRating;


/**
 The total number of ratings for the item.
 */
@property (nonatomic) NSInteger numberOfRatings;


/**
 The unique item id.
 */
@property (nonatomic, retain) NSString *itemId;


@end
