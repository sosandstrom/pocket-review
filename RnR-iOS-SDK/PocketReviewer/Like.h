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
@interface Like : NSObject


/**
 Create a new Likes object
 @return A new Like object
 */
+ (Like*)likes;


/** An unique link id */
@property (nonatomic, retain) NSString *likeId;

/** The items id */
@property (nonatomic, retain) NSString *itemId;

/** The user who made the like, can be annonymous */
@property (nonatomic, retain) NSString *username;



@end
