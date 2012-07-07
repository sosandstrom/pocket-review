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


ANNOTATE_PROPERTY_FOR_KEY(itemId, id)
@synthesize itemId = itemId_;

ANNOTATE_PROPERTY_FOR_KEY(latitude, latitude)
@synthesize latitude = latitude_;

ANNOTATE_PROPERTY_FOR_KEY(longitude, longitude)
@synthesize longitude = longitude_;

ANNOTATE_PROPERTY_FOR_KEY(numberOfRatings, ratingCount)
@synthesize numberOfRatings = numberOfRatings_;

ANNOTATE_PROPERTY_FOR_KEY(averageRating, average)
@synthesize averageRating = averageRating_;


// Release instance variables
- (void)dealloc {
  [itemId_ release];
  [super dealloc];
}


// Create autoreleased Rating object
+ (Rating*)rating {
  return [[[Rating alloc] init] autorelease];
}


// Getter for average rating
- (float) averageRating {
  return (float)averageRating_ / (100 / [PocketReviewer sharedReviewer].maximumRating);
}


// Custom description
- (NSString*)description {
  return [NSString stringWithFormat:@"Id: %@, averate ratng: %f, number of ratings: %d", 
          self.itemId, self.averageRating, self.numberOfRatings];
}


@end
