//
//  Item.m
//  PocketReviewsDemo
//
//  Created by Mattias Levin on 8/30/12.
//
//

#import "Item.h"
#import "ObjectMapper.h"
#import "PocketReviewer.h"

@implementation Item


MapKeyToProperty(id, itemId)
@synthesize itemId = itemId_;

MapKeyToProperty(latitude, latitude)
@synthesize latitude = latitude_;

MapKeyToProperty(longitude, longitude)
@synthesize longitude = longitude_;



MapKeyToProperty(average, averageRating)
MapKeyToBlock(average, ^(NSNumber *serverAverage) {
  //NSLog(@"Convert average %@", serverAverage);
  double clientAverage = [serverAverage doubleValue];
  return [NSNumber numberWithFloat:((float)clientAverage / (100 / [PocketReviewer sharedReviewer].maximumRating))];
};)
@synthesize averageRating = averageRating_;

MapKeyToProperty(ratingCount, numberOfRatings)
@synthesize numberOfRatings = numberOfRatings_;

@synthesize numberOflikes = numberOfLikes_;

@synthesize numberOfComments = numberOfComments_;


// Release instance variables
- (void)dealloc {
  [itemId_ release];
  [super dealloc];
}


// Create autoreleased Rating object
+ (Item*)item {
  return [[[Item alloc] init] autorelease];
}


// Custom description
- (NSString*)description {
  return [NSString stringWithFormat:@"Id: %@, averge rating: %f, number of rating: %d",
          self.itemId, self.averageRating, self.numberOfRatings];
}




@end
