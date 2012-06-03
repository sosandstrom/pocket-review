//
//  PocketReviewer.m
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "PocketReviewer.h"
#import "JSONKit.h"


// Macros

// Uncomment the line below to get debug statements
#define PRINT_DEBUG
// Debug macros
#ifdef PRINT_DEBUG
//#  define DLOG(fmt, ...) NSLog( (@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__);
#  define DLOG(fmt, ...) NSLog( (@"" fmt), ##__VA_ARGS__);
#else
#  define DLOG(...)
#endif
// ALog always displays output regardless of the DEBUG setting
#define ALOG(fmt, ...) NSLog( (@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__);

// The service accept ratings from 0..100. This API only accept from 0..5
#define RATING_SCALE 20

// http request config
#define USER_AGENT @"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3"
#define REQUEST_TIMEOUT 10

// http parameter names
#define ITEM_ID @"itemid"
#define RATING @"rating"
#define USER_ID @"username"
#define LATITUDE @"latitude"
#define LONGITUDE @"longitude"
#define RADIUS @"radius"
#define MIN_RATING @"minRating"
#define IDS @"ids"

// JSON attribute names
#define RATING_SUM @"ratingSum"
#define RATING_COUNT @"ratingCount"
#define ID @"id"

// Parameter validation rules
#define CHECK_URL(url) ((url == nil) ? ([self parsingErrorWithDescription:@"Service URL can not been nil, can not start reviewer"]) : (nil))
#define CHECK_DOMAIN(domain) ((domain == nil) ? ([self parsingErrorWithDescription:@"Application domain can not be nil"]) : (nil))
#define CHECK_ITEM_ID(itemId) ((itemId == nil) ? ([self parsingErrorWithDescription:@"Item id can not be nil"]) : (nil))
#define CHECK_USER_ID(userId) ((userId == nil) ? ([self parsingErrorWithDescription:@"User id can not be nil"]) : (nil))
#define CHECK_RATING(rating) (([rating integerValue] < 0 || [rating integerValue] > 5) ? \
                              ([self parsingErrorWithDescription:@"Rating value must be between 0 and 5"]) : (nil))
#define CHECK_LATITUDE(latitude) (([latitude floatValue] < -90.0f || [latitude floatValue] > 90.0f) ? \
                              ([self parsingErrorWithDescription:@"User id can not be nil"]) : (nil))
#define CHECK_LONGITUDE(longitude) (([longitude floatValue] < -180.0f || [longitude floatValue] > 180.0f) ? \
                              ([self parsingErrorWithDescription:@"User id can not be nil"]) : (nil))
#define CHECK_NEARBY_RADIUS(radius) (([radius integerValue] < 2 || [radius integerValue] > 4) ?  \
                              ([self parsingErrorWithDescription:@"Radius is invalid, must use any of the provided enum values"]) : (nil))

// Append paths and query string
#define APPEND_PATH(path, url) [NSURL URLWithString:[[url URLByAppendingPathComponent:path] absoluteString]]
#define APPEND_QUERY(query, url) [NSURL URLWithString:[NSString stringWithFormat:@"%@?%@", [url absoluteString], query]]


// Private stuff
@interface PocketReviewer ()

- (void)doRateItem:(NSString*)itemId forLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude 
              withRating:(NSNumber*)rating completionBlock:(void(^)(NSError*))block;
- (void)doNearbyItemsForLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude withinRadius:(NSNumber*)radius 
                         minimumRating:(NSNumber*)minimumRating completionBlock:(void(^)(NSArray*, NSError*))block;

- (NSInteger)serviceRequestWithUrl:(NSURL*)url body:(NSDictionary*)body responseData:(NSData**)data error:(NSError**)error;
- (NSString*)toStringFromDict:(NSDictionary*)dict;
- (NSError*)parsingErrorWithDescription:(NSString*)format, ...;

@property (nonatomic, retain) NSURL *url;
@property (nonatomic) BOOL anonymous;
@property (nonatomic) dispatch_queue_t queue;

@end


// Implementation
@implementation PocketReviewer


@synthesize userId = userId_;
@synthesize url = url_;
@synthesize dryRun = dryRun_;
@synthesize anonymous = anonymous_;
@synthesize queue = queue_;


// Release instance variables
- (void)dealloc {
  [userId_ release];
  [url_ release];
  dispatch_release(self.queue);
  [super dealloc];
}


// Init
- (id)init {
  self = [super init];
  if (self) {        
    //cInitialise instance variables
    self.queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
  }
  return self;
}


// Create a shared rater
+ (PocketReviewer*)sharedReviewer {
  // Singleton
  static PocketReviewer* rater = nil;
  
  if (!rater) {
    // Create rater
    rater = [[PocketReviewer alloc] init];
  }
  
  return rater;
}


// Start rating
- (BOOL)startReviewingWithServiceUrl:(NSURL*)url domain:(NSString*)domain anonymous:(BOOL)anonymous withError:(NSError**)error {
  DLOG(@"Start rating");

  // Check paramters
  NSError *validationError = nil;
  validationError = CHECK_URL(url);
  validationError = CHECK_DOMAIN(domain);
  if (validationError) {
    error = &validationError;
    return NO;
  }
  
  // Combine the service url with the domain name into the base url for all requests
  self.url = APPEND_PATH(domain, url);

  self.anonymous = anonymous;
  
  return YES;
}


// Rate an item
- (void)rateItem:(NSString*)itemId withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block {
  [self doRateItem:itemId forLatitude:nil longitude:nil withRating:[NSNumber numberWithInteger:rating] completionBlock:block];
}


// Rate an item with latitude and longitude
- (void)rateItem:(NSString*)itemId forLatitude:(float)latitude longitude:(float)longitude 
      withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block {
  
  [self doRateItem:itemId forLatitude:[NSNumber numberWithFloat:latitude] longitude:[NSNumber numberWithFloat:longitude] 
          withRating:[NSNumber numberWithInteger:rating] completionBlock:block];
}


// Internal rating method
- (void)doRateItem:(NSString*)itemId forLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude 
          withRating:(NSNumber*)rating completionBlock:(void(^)(NSError*))block {
  
  // Use GDC
  dispatch_async(self.queue, ^{
    DLOG(@"Rate an item");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    error = CHECK_URL(self.url);
    error = CHECK_ITEM_ID(itemId);
    error = CHECK_LATITUDE(latitude);
    error = CHECK_LONGITUDE(longitude);
    error = CHECK_RATING(rating);
    if (error) {
      block(error);
      return;
    }
    
    // Rescale the rating value to fit the backend api
    NSNumber *scaledRating = [NSNumber numberWithInteger:[rating integerValue] * RATING_SCALE];
    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"rating", self.url);
    requestUrl = APPEND_PATH(itemId, requestUrl);
    
    // Collect body paramters
    NSMutableDictionary *body = [NSMutableDictionary dictionary];
    if (self.userId) [body setObject:self.userId forKey:USER_ID];
    if (rating) [body setObject:scaledRating forKey:RATING];
    if (latitude) [body setObject:latitude forKey:LATITUDE];
    if (longitude) [body setObject:longitude forKey:LONGITUDE];
    
    // Make the request
    NSData *data = nil;
    int responseCode = [self serviceRequestWithUrl:requestUrl body:body responseData:&data error:&error];
    DLOG(@"Rating request executed with response code %d", responseCode);
    
    // Handle the http response code
    if (responseCode == 200) {
      // Run the completion block
      block(nil);
    } else {
      // Run the complition block with error
      block([self parsingErrorWithDescription:@"HTTP rate request failed with respose code %d and error message %@", responseCode, [error userInfo]]);
    }
  
  });
}


// Get the rating for an item
- (void)ratingForItem:(NSString*)itemId completionBlock:(void(^)(Rating*, NSError*))block {
  
  // Use GDC
  dispatch_async(self.queue, ^{
    DLOG(@"Get a rating");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    error = CHECK_URL(self.url);
    error = CHECK_ITEM_ID(itemId);
    if (error) {
      block(nil, error);
      return;
    }
    
    // Build the request path
    DLOG(@"base url %@", [self.url absoluteString]);
    NSURL *requestUrl = APPEND_PATH(@"rating", self.url);
    requestUrl = APPEND_PATH(itemId, requestUrl);
    
    // Make the request
    NSData *data = nil;
    int responseCode = [self serviceRequestWithUrl:requestUrl body:nil responseData:&data error:&error];
    DLOG(@"Get rating request executed with response code %d", responseCode);
      
    // Handle the http response
    if (responseCode == 200) {
      // Parse the JSON
      NSDictionary *ratingDict = [data objectFromJSONData];
      Rating *rating = [Rating rating];
      rating.totalSumOfRatings = [(NSNumber*)[ratingDict valueForKey:RATING_SUM] integerValue] / RATING_SCALE;
      rating.numberOfRatings = [(NSNumber*)[ratingDict valueForKey:RATING_COUNT] integerValue];
      rating.itemId = [ratingDict valueForKey:ID];

      // Run the completion block
      block(rating, nil);
    } else {
      // Run the completion block with error
      block(nil, [self parsingErrorWithDescription:@"HTTP get rating request failed with respose code %d and error message %@", 
                  responseCode, [error userInfo]]);
    }
    
  });
}


// Get raings for a list of items
- (void)ratingForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block {
  
  // Use GDC
  dispatch_async(self.queue, ^{    
    DLOG(@"Get rating for an array of items");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    error = CHECK_URL(self.url);
    error = CHECK_ITEM_ID(itemIds);
    if (error) {
      block(nil, error); 
      return;
    }
    
    // Build the query string
    NSMutableArray *itemParameters = [NSMutableArray array];
    [itemIds enumerateObjectsUsingBlock:^(NSString* itemId, NSUInteger idx, BOOL *stop) {
      [itemParameters addObject:[NSString stringWithFormat:@"%@=%@", IDS, itemId]];
    }];
    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"rating", self.url);
    requestUrl = APPEND_QUERY([itemParameters componentsJoinedByString:@"&"], requestUrl);
    
    // Make the request
    NSData *data = nil;
    int responseCode = [self serviceRequestWithUrl:requestUrl body:nil responseData:&data error:&error];
    DLOG(@"Get ratings request executed with response code %d", responseCode);
    
    // Handle the http response
    if (responseCode == 200) {
      // Parse the JSON
      NSArray *ratingsArray = [data objectFromJSONData];
      
      // Iterat over each rating dict and create a Rating object
      NSMutableArray *ratings = [NSMutableArray array];
      for (NSDictionary *ratingDict in ratingsArray) {
        Rating *rating = [Rating rating];
        rating.totalSumOfRatings = [(NSNumber*)[ratingDict valueForKey:RATING_SUM] integerValue] / RATING_SCALE;
        rating.numberOfRatings = [(NSNumber*)[ratingDict valueForKey:RATING_COUNT] integerValue];
        rating.itemId = [ratingDict valueForKey:ID];
        
        // Add the rating to the result array
        [ratings addObject:rating];
      }
      
      // Run the completion block
      block(ratings, nil);
    } else {
      // Run the completion block with error
      block(nil, [self parsingErrorWithDescription:@"HTTP get rating request failed with respose code %d and error message %@", responseCode, [error userInfo]]);
    }
    
  });
}


// Get my ratings
- (void)myRatingsWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  DLOG(@"Get my ratings");
  
  // Track errors
  NSError *error = nil;
  
  // Check parameters
  error = CHECK_URL(self.url);
  error = CHECK_USER_ID(self.userId);
  if (error) {
    block(nil, error);
    return;
  }
  
  // TODO
}



// Get nearby items using Google provided latitude and langitude
- (void)nearbyItemsWithinRadius:(NearbyRadius)radius minimumRating:(NSInteger)minimumRating completionBlock:(void(^)(NSArray*, NSError*))block {
  [self doNearbyItemsForLatitude:nil longitude:nil withinRadius:[NSNumber numberWithInteger:radius]
                   minimumRating:[NSNumber numberWithInteger:minimumRating] completionBlock:block];
  
}


// Get nearby items 
- (void)nearbyItemsForLatitude:(float)latitude longitude:(float)longitude withinRadius:(NearbyRadius)radius 
                 minimumRating:(NSInteger)minimumRating completionBlock:(void(^)(NSArray*, NSError*))block {
  [self doNearbyItemsForLatitude:[NSNumber numberWithFloat:latitude] longitude:[NSNumber numberWithFloat:longitude] 
                    withinRadius:[NSNumber numberWithInteger:radius] minimumRating:[NSNumber numberWithInteger:minimumRating] completionBlock:block];
  
}


- (void)doNearbyItemsForLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude withinRadius:(NSNumber*)radius 
                   minimumRating:(NSNumber*)minimumRating completionBlock:(void(^)(NSArray*, NSError*))block {
  
  // Use GDC
  dispatch_async(self.queue, ^{    
    DLOG(@"Get nearby items");
    
    // Track errors
    NSError *error = nil;
    
    // Convert the radius, do not send any value if default radius is specified
    NSNumber *searchRadius = ([radius integerValue] == kDefaultRadius) ? nil : radius;
    
    // Check parameters
    error = CHECK_URL(self.url);
    error = CHECK_LATITUDE(latitude);
    error = CHECK_LONGITUDE(longitude);
    error = CHECK_RATING(minimumRating);
    //error = CHECK_NEARBY_RADIUS(searchRadius);
    if (error) {
      block(nil, error);
      return;
    }
    
    // Rescale the rating value to fit the backend api
    NSNumber *scaledRating = [NSNumber numberWithInteger:[minimumRating integerValue] * RATING_SCALE];
    
    // Collect body paramters
    NSMutableDictionary *body = [NSMutableDictionary dictionary];
    if (latitude) [body setObject:latitude forKey:LATITUDE];
    if (longitude) [body setObject:longitude forKey:LONGITUDE];
    if (searchRadius) [body setObject:radius forKey:RADIUS];
    if (minimumRating) [body setObject:scaledRating forKey:MIN_RATING];      
      
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"nearby", self.url);
    
    // Make the request
    NSData *data = nil;
    int responseCode = [self serviceRequestWithUrl:requestUrl body:body responseData:&data error:&error];
    DLOG(@"Get ratings request executed with response code %d", responseCode);
    
    // Handle the http response
    if (responseCode == 200) {
      // Parse the JSON
      NSArray *ratingsArray = [data objectFromJSONData];
      
      // Iterat over each rating dict and create a Rating object
      NSMutableArray *ratings = [NSMutableArray array];
      for (NSDictionary *ratingDict in ratingsArray) {
        Rating *rating = [Rating rating];
        rating.totalSumOfRatings = [(NSNumber*)[ratingDict valueForKey:RATING_SUM] integerValue] / RATING_SCALE;
        rating.numberOfRatings = [(NSNumber*)[ratingDict valueForKey:RATING_COUNT] integerValue];
        rating.itemId = [ratingDict valueForKey:ID];
        
        // Add the rating to the result array
        [ratings addObject:rating];
      }
      
      // Run the completion block
      block(ratings, nil);
    } else {
      // Run the completion block with error
      block(nil, [self parsingErrorWithDescription:@"HTTP get rating request failed with respose code %d and error message %@", responseCode, [error userInfo]]);
    }
    
  });
}


// Add a review
- (void)reviewItem:(NSString*)itemId withReview:(NSString*)review completionBlock:(void(^)(NSError*))block {
  // TODO
}


// Get all reviews for an item
- (void)reviewsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


// Get all my reviews
- (void)myReviewsWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


// Setter and getter for user id
- (void)setUserId:(NSString *)userId {
  if (userId != userId_) {
    [userId_ release];
    userId_ = userId;
    [userId_ retain];
    // Save the id to user preferences
    [[NSUserDefaults standardUserDefaults] setObject:userId_ forKey:@"PocketReview.userId"];
    [[NSUserDefaults standardUserDefaults] synchronize];
  }
}


- (NSString*)userId {

  if (self.anonymous)
    return nil;
  
  if (!userId_) {
    // Get the user id from user preferences
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];    
    userId_ = [[userDefaults stringForKey:@"PocketReview.userId"] retain];
    
    if (!userId_) {
      // No value, create one and store in user defaults
      CFUUIDRef uuid = CFUUIDCreate(NULL);
      userId_ = (NSString*)CFUUIDCreateString(NULL, uuid);
      DLOG(@"User defaults does not contain a user id, create a new %@", userId_);
      CFRelease(uuid);
      
      [[NSUserDefaults standardUserDefaults] setObject:userId_ forKey:@"PocketReview.userId"];
      [[NSUserDefaults standardUserDefaults] synchronize];
    }
  }
  
  return userId_;
}


// Send a request to the backend service
- (NSInteger)serviceRequestWithUrl:(NSURL*)url body:(NSDictionary*)body responseData:(NSData**)data error:(NSError**)error {
  
  // Check if dry run
  if (self.dryRun) {
    NSMutableString *prettyURL = [NSMutableString stringWithString:[url description]];
    
    // If we have a POST body add that to the output as well
    if (body)
      [prettyURL appendFormat:@"?%@", [self toStringFromDict:body]];
    
    // Format the output for easy reading
    [prettyURL replaceOccurrencesOfString:@"?" withString:@"\n  " options:NSLiteralSearch 
                                    range:NSMakeRange(0, [prettyURL length])];
    [prettyURL replaceOccurrencesOfString:@"&" withString:@"\n  " options:NSLiteralSearch 
                                    range:NSMakeRange(0, [prettyURL length])];
    ALOG(@"Dry run request:\n%@", prettyURL);
    
    // Always return success
    return 200;
    
  } else {
    // Create the http request
    NSMutableURLRequest *httpRequest = [NSMutableURLRequest requestWithURL:url];

    // Decide http methods
    if (body)
      httpRequest.HTTPMethod = @"POST";
    else
      httpRequest.HTTPMethod = @"GET";
      
     // Cache policy and timeout value 
    [httpRequest setCachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData];
    [httpRequest setTimeoutInterval:REQUEST_TIMEOUT];
    
    // Set User-Agent header
    [httpRequest setValue:USER_AGENT forHTTPHeaderField:@"User-Agent"];
    
    // If POST add body
    if ([httpRequest.HTTPMethod isEqualToString:@"POST"])
      [httpRequest setHTTPBody:[[self toStringFromDict:body] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // Make the request
    NSURLResponse *response = nil;
    DLOG(@"Sending backend request %@", httpRequest);
    *data = [NSURLConnection sendSynchronousRequest:httpRequest returningResponse:&response error:error];
    
    // Return the http response code
    return [(NSHTTPURLResponse*)response statusCode];
  }
  
}


// Create a paramter string from a NSDictionary                                        
- (NSString*)toStringFromDict:(NSDictionary*)dict {
  // Create the parameter list
  NSMutableArray *params = [NSMutableArray array];
  [dict enumerateKeysAndObjectsUsingBlock:^(id key, id value, BOOL *stop) {
      [params addObject:[NSString stringWithFormat:@"%@=%@", key, value]];
  }];
  
  return [params componentsJoinedByString:@"&"];
}


// Format an error message
- (NSError*)parsingErrorWithDescription:(NSString*)format, ... {   
  // Create a formatted string from input parameters
  va_list varArgsList;
  va_start(varArgsList, format);
  NSString *formatString = [[[NSString alloc] initWithFormat:format arguments:varArgsList] autorelease];
  va_end(varArgsList);
  
  ALOG(@"Error with message: %@", formatString);
  
  // Create the error and store the state
  NSDictionary *errorInfo = [NSDictionary dictionaryWithObjectsAndKeys: 
                             NSLocalizedDescriptionKey, formatString,
                             nil];
  return [NSError errorWithDomain:@"com.wadpam.PocketReviews.ErrorDomain" code:1 userInfo:errorInfo];
}


@end
