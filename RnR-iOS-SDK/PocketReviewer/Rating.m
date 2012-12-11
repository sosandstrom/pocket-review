//
//  Rating.m
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "Rating.h"
#import "ObjectMapper.h"
#import "PocketReviewer.h"


// Implementation
@implementation Rating


MapKeyToProperty(id, ratingId)
@synthesize ratingId = ratingId_;

MapKeyToProperty(productId, itemId)
@synthesize itemId = itemId_;

MapKeyToProperty(username, username)
@synthesize username = username_;

MapKeyToProperty(rating, rating)
@synthesize rating = rating_;




// Release instance variables
- (void)dealloc {
  [ratingId_ release];
  [itemId_ release];
  [username_ release];
  [super dealloc];
}


// Create autoreleased Rating object
+ (Rating*)rating {
  return [[[Rating alloc] init] autorelease];
}


// Custom description
- (NSString*)description {
  return [NSString stringWithFormat:@"Id: %@, item id: %@, rating: %d", 
          self.ratingId, self.itemId, self.rating];
}


@end
