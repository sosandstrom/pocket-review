//
//  Likes.m
//
//  Created by Mattias Levin on 6/19/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "Like.h"
#import "ObjectMapper.h"


@implementation Like


MapKeyToProperty(id, likeId)
@synthesize likeId = likeId_;

MapKeyToProperty(productId, itemId)
@synthesize itemId = itemId_;

MapKeyToProperty(username, username)
@synthesize username = username_;



// Class method for creating a Likes object
+ (Like*)likes {
  return [[[Like alloc] init] autorelease];
}


// Free memory
- (void) dealloc {
  [likeId_ release];
  [itemId_ release];
  [username_ release];
  [super dealloc];
}


// Custom description
- (NSString*)description {
  return [NSString stringWithFormat:@"Id: %@, item id: %@", self.likeId, self.itemId];
}


@end
