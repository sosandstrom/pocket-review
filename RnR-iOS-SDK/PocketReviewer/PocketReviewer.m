//
//  PocketReviewer.m
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "PocketReviewer.h"
#import "JSONKit.h"
#import "ObjectMapper.h"


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
#define REVIEW @"review"
#define USER_ID @"username"
#define LATITUDE @"latitude"
#define LONGITUDE @"longitude"
#define RADIUS @"radius"
#define MIN_RATING @"minRating"
#define IDS @"ids"
#define MAX_NUMBER_OF_RESULTS @"maxResults"

// JSON attribute names
#define RATING_SUM @"ratingSum"
#define RATING_COUNT @"ratingCount"
#define ID @"id"

// Parameter validation rules
#define CHECK_URL(url, err) [self checkUrl:url error:err]
#define CHECK_DOMAIN(domain, err) [self checkDomain:domain error:err]
#define CHECK_ITEM_ID(itemId, err) [self checkItemId:itemId error:err]
#define CHECK_USER_ID(userId, err) [self checkUserId:userId error:err]
#define CHECK_RATING(rating, err) [self checkRating:rating error:err]
#define CHECK_REVIEW(review, err) [self checkReview:review error:err]
#define CHECK_LATITUDE(latitude, err) [self checkLatitude:latitude error:err]
#define CHECK_LONGITUDE(longitude, err) [self checkLongitude:latitude error:err]
#define CHECK_RADIUS(rating, err) [self checkRadius:radius error:err]
#define CHECK_ITEM_IDS(itemIds, err) [self checkItemIds:itemIds error:err]
#define CHECK_MAX_NUMBER_OF_RESULTS(maxNumberOfResults, err) [self checkMaxNumberOfResults:maxNumberOfResults error:err]


// Append paths and query string
#define APPEND_PATH(path, url) [NSURL URLWithString:[[url URLByAppendingPathComponent:path] absoluteString]]
#define APPEND_QUERY(query, url) [NSURL URLWithString:[NSString stringWithFormat:@"%@?%@", [url absoluteString], query]]


// Private stuff
@interface PocketReviewer ()

- (void)doRateAndReviewItem:(NSString*)itemId forLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude 
                     rating:(NSNumber*)rating review:(NSString*)review completionBlock:(void(^)(NSError*))block;
- (void)doNearbyAverageRatingsForLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude withinRadius:(NearbyRadius)radius 
                       maxNumberOfResults:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block;

- (NSInteger)serviceRequestWithUrl:(NSURL*)url body:(NSDictionary*)body responseData:(NSData**)data error:(NSError**)error;
- (NSString*)toStringFromDict:(NSDictionary*)dict;
- (NSError*)parsingErrorWithDescription:(NSString*)format, ...;

- (BOOL)checkUrl:(NSURL*)url error:(NSError**)error;
- (BOOL)checkDomain:(NSString*)domain error:(NSError **)error;
- (BOOL)checkUserId:(NSString*)userId error:(NSError **)error;
- (BOOL)checkItemId:(NSString*)itemId error:(NSError **)error;
- (BOOL)checkRating:(NSNumber*)rating error:(NSError**)error;
- (BOOL)checkReview:(NSString*)review error:(NSError**)error;
- (BOOL)checkLatitude:(NSNumber*)latitude error:(NSError**)error;
- (BOOL)checkLongitude:(NSNumber*)longitude error:(NSError**)error;
- (BOOL)checkRadius:(NSInteger)radius error:(NSError**)error;
- (BOOL)checkItemIds:(NSArray*)itemIds error:(NSError**)error;
- (BOOL)checkMaxNumberOfResults:(NSInteger)maxNumberOfResults error:(NSError**)error;

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
    // Initialise instance variables
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
- (BOOL)startReviewingWithServiceUrl:(NSURL*)url domain:(NSString*)domain anonymousUser:(BOOL)anonymous withError:(NSError**)error {
  DLOG(@"Start rating");

  // Check paramters
  if (!CHECK_URL(url, error) || 
      !CHECK_DOMAIN(domain, error)) {
    return NO;
  }
  
  // Combine the service url with the domain name into the base url for all requests
  self.url = APPEND_PATH(domain, url);

  self.anonymous = anonymous;
  
  return YES;
}


// Rate an item
- (void)rateItem:(NSString*)itemId withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block {
  [self doRateAndReviewItem:itemId forLatitude:nil longitude:nil rating:[NSNumber numberWithInteger:rating] 
                     review:nil completionBlock:block];
}


// Rate an item with latitude and longitude
- (void)rateItem:(NSString*)itemId forLatitude:(float)latitude longitude:(float)longitude 
      withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block {
  
  [self doRateAndReviewItem:itemId forLatitude:[NSNumber numberWithFloat:latitude] 
                  longitude:[NSNumber numberWithFloat:longitude] rating:[NSNumber numberWithInteger:rating] 
                     review:nil completionBlock:block];
}


// Internal rating and review method
- (void)doRateAndReviewItem:(NSString*)itemId forLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude 
                     rating:(NSNumber*)rating review:(NSString*)review completionBlock:(void(^)(NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Rate and review an item");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (!CHECK_URL(self.url, &error) || 
        !CHECK_ITEM_ID(itemId, &error) || 
        !CHECK_LATITUDE(latitude, &error) || 
        !CHECK_LONGITUDE(longitude, &error) ||
        !CHECK_RATING(rating, &error) ||
        !CHECK_REVIEW(review, &error)) {
      block(error);
      return;
    }
    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"rating", self.url);
    requestUrl = APPEND_PATH(itemId, requestUrl);
    
    // Collect body paramters
    NSMutableDictionary *body = [NSMutableDictionary dictionary];
    if (self.userId) [body setObject:self.userId forKey:USER_ID];
    if (rating) [body setObject:[NSNumber numberWithInteger:[rating integerValue] * RATING_SCALE] forKey:RATING];
    if (review) [body setObject:review forKey:REVIEW];
    if (latitude) [body setObject:latitude forKey:LATITUDE];
    if (longitude) [body setObject:longitude forKey:LONGITUDE];
    
    // Make the request
    NSData *data = nil;
    int responseCode = [self serviceRequestWithUrl:requestUrl body:body responseData:&data error:&error];
    DLOG(@"Rating and review request executed with response code %d", responseCode);
    
    // Handle the http response code
    if (responseCode == 200) {
      // Run the completion block
      block(nil);
    } else {
      // Run the complition block with error
      block([self parsingErrorWithDescription:@"HTTP rating and review request failed with respose code %d and error message %@", 
             responseCode, [error userInfo]]);
    }
  
  });
}


// Get the rating for an item
- (void)averageRatingForItem:(NSString*)itemId completionBlock:(void(^)(Rating*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Get a rating");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (!CHECK_URL(self.url, &error) || 
        !CHECK_ITEM_ID(itemId, &error)) {
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
      NSDictionary *ratingJSON = [data objectFromJSONData];
      
      // Map JSON into domain object
      Rating *rating = [ratingJSON mapToClass:[Rating class] withError:&error];
      if (rating)
        // Run the completion block
        block(rating, nil);
      else
        // Mapping failed, Run the completion block with error
        block(nil, [self parsingErrorWithDescription:@"Not possible to map the parsed JSON, got error %@", [error userInfo]]);
      
    } else {
      // Run the completion block with error
      block(nil, [self parsingErrorWithDescription:@"HTTP get rating request failed with respose code %d and error message %@", 
                  responseCode, [error userInfo]]);
    }
    
  });
}


// Get raings for a list of items
- (void)averageRatingForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{    
    DLOG(@"Get rating for an array of items");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (!CHECK_URL(self.url, &error) || 
        !CHECK_ITEM_IDS(itemIds, &error)) {
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
      NSArray *ratingsJSON = [data objectFromJSONData];
      
      // Map JSON into domain object
      NSArray *ratings = [ratingsJSON mapToClass:[Rating class] withError:&error];
      if (ratings)
        // Run the completion block
        block(ratings, nil);
      else
        // Mapping failed, Run the completion block with error
        block(nil, [self parsingErrorWithDescription:@"Not possible to map the parsed JSON, got error %@", [error userInfo]]);

    } else {
      // Run the completion block with error
      block(nil, [self parsingErrorWithDescription:@"HTTP get rating request failed with respose code %d and error message %@", 
                  responseCode, [error userInfo]]);
    }
    
  });
}


// Get my ratings
- (void)myRatingsWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  DLOG(@"Get my ratings");
  
  // Track errors
  NSError *error = nil;
  
  // Check parameters
  if (!CHECK_URL(self.url, &error) || 
      !CHECK_USER_ID(self.userId, &error)) {
    block(nil, error);
    return;
  }
  
  // TODO
}


// Get top average ratings
- (void)topAverageRatings:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}



// Get nearby items using Google provided latitude and langitude
- (void)nearbyTopAverageRatingsWithinRadius:(NearbyRadius)radius maxNumberOfResults:(NSInteger)maxNumberOfResults 
                            completionBlock:(void(^)(NSArray*, NSError*))block {
  [self doNearbyAverageRatingsForLatitude:nil longitude:nil withinRadius:radius maxNumberOfResults:maxNumberOfResults 
                 completionBlock:block];
  
}


// Get nearby items 
- (void)nearbyTopAverageRatingsForLatitude:(float)latitude longitude:(float)longitude withinRadius:(NearbyRadius)radius 
                        maxNumberOfResults:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block {
  [self doNearbyAverageRatingsForLatitude:[NSNumber numberWithFloat:latitude] longitude:[NSNumber numberWithFloat:longitude] 
                    withinRadius:radius maxNumberOfResults:maxNumberOfResults completionBlock:block];
  
}


- (void)doNearbyAverageRatingsForLatitude:(NSNumber*)latitude longitude:(NSNumber*)longitude withinRadius:(NearbyRadius)radius 
                       maxNumberOfResults:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{    
    DLOG(@"Get nearby items");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (!CHECK_URL(self.url, &error) || 
        !CHECK_LATITUDE(latitude, &error) || 
        !CHECK_LONGITUDE(longitude, &error) ||
        !CHECK_MAX_NUMBER_OF_RESULTS(maxNumberOfResults, &error)) {
      block(nil, error);
      return;
    }
    
    // Convert the radius, do not send any value if default radius is specified
    NSNumber *searchRadius = (radius == kDefaultRadius) ? nil : [NSNumber numberWithInteger:radius];
    
    // Collect query string
    NSMutableDictionary *query = [NSMutableDictionary dictionary];
    if (latitude) [query setObject:latitude forKey:LATITUDE];
    if (longitude) [query setObject:longitude forKey:LONGITUDE];
    if (searchRadius) [query setObject:[NSNumber numberWithInteger:radius] forKey:RADIUS];
    //[query setObject:[NSNumber numberWithInteger:maxNumberOfResults] forKey:MAX_NUMBER_OF_RESULTS]; // TODO uncomment when backend has been updated
      
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"rating", self.url);
    requestUrl = APPEND_PATH(@"nearby", requestUrl);
    requestUrl = APPEND_QUERY([self toStringFromDict:query], requestUrl);
    
    // Make the request
    NSData *data = nil;
    int responseCode = [self serviceRequestWithUrl:requestUrl body:nil responseData:&data error:&error];
    DLOG(@"Get ratings request executed with response code %d", responseCode);
    
    // Handle the http response
    if (responseCode == 200) {
      // Parse the JSON
      NSArray *ratingsJSON = [data objectFromJSONData];
      
      // Map JSON into domain object
      NSArray *ratings = [ratingsJSON mapToClass:[Rating class] withError:&error];
      if (ratings)
        // Run the completion block
        block(ratings, nil);
      else
        // Mapping failed, Run the completion block with error
        block(nil, [self parsingErrorWithDescription:@"Not possible to map the parsed JSON, got error %@", [error userInfo]]);
      
    } else {
      // Run the completion block with error
      block(nil, [self parsingErrorWithDescription:@"HTTP get rating request failed with respose code %d and error message %@", 
                  responseCode, [error userInfo]]);
    }
    
  });
}


// Add a review
- (void)reviewItem:(NSString*)itemId withReview:(NSString*)review completionBlock:(void(^)(NSError*))block {
  [self doRateAndReviewItem:itemId forLatitude:nil longitude:nil rating:nil review:review completionBlock:block];
}


// Get all reviews for an item
- (void)reviewsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


// Delete the review written by the current user
- (void)deleteMyReviewForItem:(NSString*)itemId completionBlock:(void(^)(NSError*))block {
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


// Create a parameter string from a NSDictionary                                        
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


// Validation of parameters
- (BOOL)checkUrl:(NSURL*)url error:(NSError**)error {
  if (!url) {
    *error = [self parsingErrorWithDescription:@"Service URL can not been nil, can not start reviewer"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkDomain:(NSString*)domain error:(NSError**)error {
  if (!domain || [domain length] == 0) {
    *error = [self parsingErrorWithDescription:@"Application domain can not be nil or empty"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkItemId:(NSString*)itemId error:(NSError**)error {
  if (!itemId || [itemId length] == 0) {
    *error = [self parsingErrorWithDescription:@"Item id can not be nil or empty"];
    return NO;
  } else
    return YES;}

- (BOOL)checkUserId:(NSString*)userId error:(NSError**)error {
  if (!userId || [userId length] == 0) {
    *error = [self parsingErrorWithDescription:@"User id can not be nil or empty"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkRating:(NSNumber*)rating error:(NSError**)error {
  if (rating != nil && [rating integerValue] < 1 && [rating integerValue] > 5) {
    *error = [self parsingErrorWithDescription:@"Rating value must be between 1..5"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkReview:(NSString*)review error:(NSError**)error {
  if (review && [review length] == 0) {
    *error = [self parsingErrorWithDescription:@"Review can not be an empty string"];
    return NO;
  } else
    return YES;
}


- (BOOL)checkLatitude:(NSNumber*)latitude error:(NSError**)error {
  if (!latitude && [latitude floatValue] < -90.0f && [latitude floatValue] > 90.0f) {
    *error = [self parsingErrorWithDescription:@"Latitude must be between -90..90"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkLongitude:(NSNumber*)longitude error:(NSError**)error {
  if (!longitude && [longitude floatValue] < -180.0f && [longitude floatValue] > 180.0f) {
    *error = [self parsingErrorWithDescription:@"Latitude must be between -180..180"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkRadius:(NSInteger)radius error:(NSError**)error {
  if (radius < 1 && radius > 4) {
    *error = [self parsingErrorWithDescription:@"Radius must be one of the provided enums"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkItemIds:(NSArray*)itemIds error:(NSError**)error {
  if (!itemIds || [itemIds count] == 0) {
    *error = [self parsingErrorWithDescription:@"List of item ids can not be nil or empty"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkMaxNumberOfResults:(NSInteger)maxNumberOfResults error:(NSError**)error {
  if (maxNumberOfResults < 1) {
    *error = [self parsingErrorWithDescription:@"Maximum number of returned results must be more then 0"];
    return NO;
  } else
    return YES;
}

          
@end
