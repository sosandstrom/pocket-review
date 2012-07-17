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


MAP_KEY_TO_PROPERY(id, itemId)
@synthesize itemId = itemId_;

MAP_KEY_TO_PROPERY(latitude, latitude)
@synthesize latitude = latitude_;

MAP_KEY_TO_PROPERY(longitude, longitude)
@synthesize longitude = longitude_;

MAP_KEY_TO_PROPERY(ratingCount, numberOfRatings)
@synthesize numberOfRatings = numberOfRatings_;

MAP_KEY_TO_PROPERY(average, averageRating)
MAP_KEY_TO_BLOCK(average, ^(NSNumber *serverAverage) {
  //NSLog(@"Convert average %@", serverAverage);
  double clientAverage = [serverAverage doubleValue];
  return [NSNumber numberWithFloat:((float)clientAverage / (100 / [PocketReviewer sharedReviewer].maximumRating))];
};)
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


// Custom description
- (NSString*)description {
  return [NSString stringWithFormat:@"Id: %@, averate ratng: %f, number of ratings: %d", 
          self.itemId, self.averageRating, self.numberOfRatings];
}


@end
