//
//  Comment.m
//
//  Created by Mattias Levin on 8/31/12.
//  Copyright (c) 2012 Wabpam. All rights reserved.
//
//

#import "Comment.h"
#import "ObjectMapper.h"

@implementation Comment


MapKeyToProperty(id, commentId)
@synthesize commentId = commentId_;

MapKeyToProperty(productId, itemId)
@synthesize itemId = itemId_;

MapKeyToProperty(username, username)
@synthesize username = username_;

MapKeyToProperty(comment, comment)
@synthesize comment = comment_;


// Release instance variables
- (void)dealloc {
  [commentId_ release];
  [itemId_ release];
  [username_ release];
  [comment_ release];
  [super dealloc];
}


// Create autoreleased Comment object
+ (Comment*)comment {
  return [[[Comment alloc] init] autorelease];
}


// Custom description
- (NSString*)description {
  return [NSString stringWithFormat:@"Id: %@, item: %@, comment: %@",
          self.commentId, self.itemId, self.comment];
}

@end
