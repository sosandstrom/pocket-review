//
//  Rating.m
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "Rating.h"

// Implementation
@implementation Rating


@synthesize itemId = itemId_;
@synthesize averageRating = averageRating_;
@synthesize numberOfRatings = numberOfRatings_;


// Release instance variables
- (void)dealloc {
  [itemId_ release];
  [super dealloc];
}


// Create autoreleased Rating object
+ (Rating*)rating {
  return [[[Rating alloc] init] autorelease];
}


@end
