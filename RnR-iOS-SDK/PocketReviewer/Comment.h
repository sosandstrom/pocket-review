//
//  Comment.h
//
//  Created by Mattias Levin on 8/31/12.
//  Copyright (c) 2012 Wabpam. All rights reserved.
//
//

#import <Foundation/Foundation.h>


/**
 A comment in an item.
 */
@interface Comment : NSObject

/** 
 Create and autorelease comment object
 @return a new Comment object
 */
+ (Comment*)comment;

/** An unique comment id */
@property (nonatomic, retain) NSString *commentId;

/** The items id */
@property (nonatomic, retain) NSString *itemId;

/** The user who made the comment, can be annonymous */
@property (nonatomic, retain) NSString *username;

/** The actual comment */
@property (nonatomic, retain) NSString *comment;


@end
