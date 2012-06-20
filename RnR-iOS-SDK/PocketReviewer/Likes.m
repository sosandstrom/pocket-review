//
//  Likes.m
//
//  Created by Mattias Levin on 6/19/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "Likes.h"


@implementation Likes


@synthesize itemId = itemId_;
@synthesize numberOfLikes = numberOfLikes_;


// Class method for creating a Likes object
+ (Likes*)likes {
  return [[[Likes alloc] init] autorelease];
}


// Free memory
- (void) dealloc {
  self.itemId = nil;
  [super dealloc];
}


// Custom description
- (NSString*)description {
  return [NSString stringWithFormat:@"Id: %@, number of likes: %d", self.itemId, self.numberOfLikes];
}


@end
