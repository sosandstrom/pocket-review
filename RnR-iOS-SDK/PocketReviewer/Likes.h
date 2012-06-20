//
//  Likes.h
//
//  Created by Mattias Levin on 6/19/12.
//  Copyright (c) 2012 Wabpam. All rights reserved.
//

#import <Foundation/Foundation.h>


/**
 The likes for an item.
 */
@interface Likes : NSObject


/**
 Create a new Likes object
 @return A new likes object
 */
+ (Likes*)likes;


/**
 The unique item id.
 */
@property (nonatomic, retain) NSString* itemId;

/**
 The total number of likes for the item.
 */
@property (nonatomic) NSInteger numberOfLikes;


@end
