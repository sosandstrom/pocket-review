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


/** An unique comment id */
@property (nonatomic, retain) NSString *ratingId;

/** The items id */
@property (nonatomic, retain) NSString *itemId;

/** The user who made the comment, can be annonymous */
@property (nonatomic, retain) NSString *username;

/** The actual rating */
@property (nonatomic) NSInteger rating;


@end
