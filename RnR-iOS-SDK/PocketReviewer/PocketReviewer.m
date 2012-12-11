//
//  PocketReviewer.m
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "PocketReviewer.h"
#import "JSONKit.h"
#import "ObjectMapper.h"
#import "PiwikTrackerUserAgentReader.h"


# pragma mark - Marcos

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

// http request config
#define REQUEST_TIMEOUT 10

// http parameter names
#define ITEM_ID @"itemid"
#define RATING @"rating"
#define COMMENT @"comment"
#define USER_ID @"username"
#define LATITUDE @"latitude"
#define LONGITUDE @"longitude"
#define RADIUS @"radius"
#define MIN_RATING @"minRating"
#define IDS @"ids"
#define MAX_NUMBER_OF_RESULTS @"maxResults"
#define SORT_ORDER @"sort"

// JSON attribute names
#define RATING_SUM @"ratingSum"
#define RATING_COUNT @"ratingCount"
#define ID @"id"

// Append paths and query string
#define APPEND_PATH(path, url) [NSURL URLWithString:[[url URLByAppendingPathComponent:path] absoluteString]]
#define APPEND_QUERY(query, url) [NSURL URLWithString:[NSString stringWithFormat:@"%@?%@", [url absoluteString], query]]


# pragma mark - Private declarations

// Private stuff
@interface PocketReviewer ()

- (void)doRateItem:(NSString*)itemId
      withLatitude:(NSNumber*)latitude
         longitude:(NSNumber*)longitude
            rating:(NSNumber*)rating
           comment:(NSString*)comment
   completionBlock:(void(^)(Rating*, NSError*))block;

- (void)doGetNearbyTopRatedItemsForLatitude:(NSNumber*)latitude
                                  longitude:(NSNumber*)longitude
                                 radius:(NearbyRadius)radius
                         maxNumberOfResults:(NSInteger)maxNumberOfResults
                        completionBlock:(void(^)(NSArray*, NSError*))block;

- (NSInteger)requestWithUrl:(NSURL*)url
                       body:(NSDictionary*)body
                resultClazz:(Class)clazz
            completionBlock:(void(^)(id result, NSError* error))block;

- (NSString*)toStringFromDict:(NSDictionary*)dict;
- (NSInteger)toServerRating:(NSInteger)userRating;
- (NSError*)parsingErrorWithDescription:(NSString*)format, ...;

- (BOOL)checkUrl:(NSURL*)url error:(NSError**)error;
- (BOOL)checkDomain:(NSString*)domain error:(NSError **)error;
- (BOOL)checkAppUser:(NSString*)user error:(NSError **)error;
- (BOOL)checkAppPassword:(NSString*)password error:(NSError **)error;
- (BOOL)checkUserId:(NSString*)userId error:(NSError **)error;
- (BOOL)checkItemId:(NSString*)itemId error:(NSError **)error;
- (BOOL)checkRating:(NSNumber*)rating error:(NSError**)error;
- (BOOL)checkComment:(NSString*)review error:(NSError**)error;
- (BOOL)checkLatitude:(NSNumber*)latitude error:(NSError**)error;
- (BOOL)checkLongitude:(NSNumber*)longitude error:(NSError**)error;
- (BOOL)checkRadius:(NSInteger)radius error:(NSError**)error;
- (BOOL)checkItemIds:(NSArray*)itemIds error:(NSError**)error;
- (BOOL)checkMaxNumberOfResults:(NSInteger)maxNumberOfResults error:(NSError**)error;

@property (nonatomic, retain) NSURL *url;
@property (nonatomic) BOOL anonymous;
@property (nonatomic, retain) NSString *appUser;
@property (nonatomic, retain) NSString *appPassword;
@property (nonatomic) dispatch_queue_t queue;
@property (nonatomic, readonly) NSString* userAgent;

@end


// Implementation
@implementation PocketReviewer


# pragma mark - Synthesize

@synthesize maximumRating = maximumRating_;
@synthesize userId = userId_;
@synthesize url = url_;
@synthesize dryRun = dryRun_;
@synthesize anonymous = anonymous_;
@synthesize appUser = appUser_;
@synthesize appPassword = appPassword_;
@synthesize queue = queue_;
@synthesize userAgent = userAgent_;


# pragma mark - init and dealloc

// Init
- (id)init {
  self = [super init];
  if (self) {        
    // Initialise instance variables
    self.queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    self.maximumRating = 5; // Set detfault rating range to 0 to 5 (inclusive)
  }
  return self;
}


// Release instance variables
- (void)dealloc {
  [userId_ release];
  [url_ release];
  [appUser_ release];
  [appPassword_ release];
  dispatch_release(self.queue);
  [userAgent_ release];
  [super dealloc];
}


# pragma mark - Start the rater

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
- (BOOL)startReviewingWithUrl:(NSURL*)url
                       domain:(NSString*)domain
                      appUser:(NSString*)appUser
                  appPassword:(NSString*)appPassword
                anonymousUser:(BOOL)anonymous
                    withError:(NSError**)error {
  DLOG(@"Start rating");

  // Check paramters
  if (![self checkUrl:url error:error] || 
      ![self checkDomain:domain error:error] ||
      ![self checkAppUser:appUser error:error] ||
      ![self checkAppPassword:appPassword error:error]) {
    return NO;
  }
  
  // Combine the service url with the domain name into the base url for all requests
  self.url = APPEND_PATH(domain, url);

  self.anonymous = anonymous;
  self.appUser = appUser;
  self.appPassword = appPassword;
  
  // Cache the user agent profile already when the service is started
  NSString *userAgent = self.userAgent;
  #pragma unused(userAgent) // Supress compiler warning
  
  return YES;
}


# pragma mark - Items methods


// Get info for an item
- (void)itemWithId:(NSString*)itemId completionBlock:(void(^)(Item*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Get Item info");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (![self checkUrl:self.url error:&error] ||
        ![self checkItemId:itemId error:&error]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        block(nil, error);
      });
      return;
    }
    
    // Build the request path
    DLOG(@"base url %@", [self.url absoluteString]);
    NSURL *requestUrl = APPEND_PATH(@"product", self.url);
    requestUrl = APPEND_PATH(itemId, requestUrl);
    
    // Make the request
    int responseCode = [self requestWithUrl:requestUrl body:nil resultClazz:[Item class] completionBlock:block];
    DLOG(@"Get item info executed with response code %d", responseCode);
    
  });
}


// Get info for a list of items
- (void)itemsWithIds:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Get item info for an array of items");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (![self checkUrl:self.url error:&error] ||
        ![self checkItemIds:itemIds error:&error]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        block(nil, error);
      });
      return;
    }
    
    // Build the query string
    NSMutableArray *itemParameters = [NSMutableArray array];
    [itemIds enumerateObjectsUsingBlock:^(NSString* itemId, NSUInteger idx, BOOL *stop) {
      [itemParameters addObject:[NSString stringWithFormat:@"%@=%@", IDS, itemId]];
    }];
    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"product", self.url);
    requestUrl = APPEND_QUERY([itemParameters componentsJoinedByString:@"&"], requestUrl);
    
    // Make the request
    int responseCode = [self requestWithUrl:requestUrl body:nil resultClazz:[Item class] completionBlock:block];
    DLOG(@"Get info for a list of items executed with response code %d", responseCode);
    
  });
}


// Get most rated items
- (void)mostRatedItems:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
  
}


// Get top rated items
- (void)topRatedItems:(NSInteger)maxNumberOfResults completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


// Get nearby top rated items
- (void)nearbyTopRatedItemsWithRadius:(NearbyRadius)radius
         maxNumberOfResults:(NSInteger)maxNumberOfResults
            completionBlock:(void(^)(NSArray*, NSError*))block {
  
  [self doGetNearbyTopRatedItemsForLatitude:nil
                                  longitude:nil
                                     radius:radius
                         maxNumberOfResults:maxNumberOfResults
                            completionBlock:block];
}


// Get nearby top rated items
- (void)nearbyTopRatedItemsForLatitude:(float)latitude
                             longitude:(float)longitude
                                radius:(NearbyRadius)radius
                    maxNumberOfResults:(NSInteger)maxNumberOfResults
                       completionBlock:(void(^)(NSArray*, NSError*))block {
  
  [self doGetNearbyTopRatedItemsForLatitude:[NSNumber numberWithFloat:latitude]
                                  longitude:[NSNumber numberWithFloat:longitude]
                                     radius:radius
                         maxNumberOfResults:maxNumberOfResults
                            completionBlock:block];
}


// Internal get near by top rated items
- (void)doGetNearbyTopRatedItemsForLatitude:(NSNumber*)latitude
                                  longitude:(NSNumber*)longitude
                                     radius:(NearbyRadius)radius
                         maxNumberOfResults:(NSInteger)maxNumberOfResults
                            completionBlock:(void(^)(NSArray*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Get nearby top rated items");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (![self checkUrl:self.url error:&error] ||
        ![self checkLatitude:latitude error:&error] ||
        ![self checkLongitude:longitude error:&error] ||
        ![self checkMaxNumberOfResults:maxNumberOfResults error:&error]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        block(nil, error);
      });
      return;
    }
    
    // Convert the radius, do not send any value if default radius is specified
    NSNumber *searchRadius = (radius == kDefaultRadius) ? nil : [NSNumber numberWithInteger:radius];
    
    // Collect query string
    NSMutableDictionary *query = [NSMutableDictionary dictionary];
    if (latitude) [query setObject:latitude forKey:LATITUDE];
    if (longitude) [query setObject:longitude forKey:LONGITUDE];
    if (searchRadius) [query setObject:[NSNumber numberWithInteger:radius] forKey:RADIUS];
    [query setObject:[NSNumber numberWithInteger:maxNumberOfResults] forKey:MAX_NUMBER_OF_RESULTS];
    [query setObject:[NSNumber numberWithInteger:0] forKey:SORT_ORDER];

    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"product/nearby", self.url);
    requestUrl = APPEND_QUERY([self toStringFromDict:query], requestUrl);
    
    // Make the request
    int responseCode = [self requestWithUrl:requestUrl body:nil resultClazz:[Item class] completionBlock:block];
    DLOG(@"Get ratings nearby request executed with response code %d", responseCode);
    
  });
}



// Get the items I have rated
- (void)myRatedItemsWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  
  DLOG(@"Get items I have rated");
  
  dispatch_async(self.queue, ^{
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (![self checkUrl:self.url error:&error] ||
        ![self checkUserId:self.userId error:&error]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        block(nil, error);
      });
      return;
    }
    
    // Build the query string
    NSMutableDictionary *query = [NSMutableDictionary dictionary];
    [query setObject:self.userId forKey:USER_ID];
    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"product/rated", self.url);
    requestUrl = APPEND_QUERY([self toStringFromDict:query], requestUrl);
    
    // Make the request
    int responseCode = [self requestWithUrl:requestUrl body:nil resultClazz:[Item class] completionBlock:block];
    DLOG(@"Get my ratied items executed with response code %d", responseCode);    
  });
}


// Get items I have liked
- (void)myLikedItemsWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


// Get the items I have commented
- (void)myCommentedItemsWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


# pragma mark - Rating methods

// Rate an item
- (void)rateItem:(NSString*)itemId
      withRating:(NSInteger)rating
         comment:(NSString*)comment
 completionBlock:(void(^)(Rating*, NSError*))block {
  
  [self doRateItem:itemId
      withLatitude:nil
         longitude:nil
            rating:[NSNumber numberWithInteger:rating]
           comment:comment
   completionBlock:block];
}


// Rate an item with latitude and longitude
- (void)rateItem:(NSString*)itemId
    withLatitude:(float)latitude
       longitude:(float)longitude
          rating:(NSInteger)rating
         comment:(NSString*)comment
 completionBlock:(void(^)(Rating*, NSError*))block {
  
  [self doRateItem:itemId
      withLatitude:[NSNumber numberWithFloat:latitude]                  
         longitude:[NSNumber numberWithFloat:longitude]
            rating:[NSNumber numberWithInteger:rating]
           comment:comment
   completionBlock:block];
}


// Internal rating method
- (void)doRateItem:(NSString*)itemId withLatitude:(NSNumber*)latitude
                longitude:(NSNumber*)longitude rating:(NSNumber*)rating
                  comment:(NSString*)comment completionBlock:(void(^)(Rating*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Rate an item");
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (![self checkUrl:self.url error:&error] || 
        ![self checkItemId:itemId error:&error] || 
        ![self checkLatitude:latitude error:&error] || 
        ![self checkLongitude:longitude error:&error] ||
        ![self checkRating:rating error:&error]||
        ![self checkComment:comment error:&error]) {
      dispatch_async(dispatch_get_main_queue(), ^{ 
        block(nil, error);
      });
      return;
    }
    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"rating", self.url);
    
    // Collect body paramters
    NSMutableDictionary *body = [NSMutableDictionary dictionary];
    if (itemId) [body setObject:itemId forKey:ITEM_ID];
    if (self.userId) [body setObject:self.userId forKey:USER_ID];
    if (rating) [body setObject:[NSNumber numberWithInteger:[self toServerRating:[rating integerValue]]] forKey:RATING];
    if (comment) [body setObject:comment forKey:COMMENT];
    if (latitude) [body setObject:latitude forKey:LATITUDE];
    if (longitude) [body setObject:longitude forKey:LONGITUDE];
    
    // Make the request
    int responseCode = [self requestWithUrl:requestUrl body:body resultClazz:[Rating class] completionBlock:block];
    DLOG(@"Rating request executed with response code %d", responseCode);
  });
}


// Get the individual ratings for an item
- (void)ratingsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Get ratings for item %@", itemId);
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (![self checkUrl:self.url error:&error] ||
        ![self checkItemId:itemId error:&error]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        block(nil, error);
      });
      return;
    }
    
    // Build the query string
    NSMutableDictionary *query = [NSMutableDictionary dictionary];
    [query setObject:itemId forKey:ITEM_ID];
    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"rating", self.url);
    requestUrl = APPEND_QUERY([self toStringFromDict:query], requestUrl);
    
    // Make the request
    int responseCode = [self requestWithUrl:requestUrl body:nil resultClazz:[Rating class] completionBlock:block];
    DLOG(@"Get ratings for item request executed with response code %d", responseCode);
  });
}

  
// Get a historgram
- (void)ratingHistogramForItem:(NSString*)itemId interval:(int)interval completionBlock:(void(^)(Histogram*, NSError*))block {
  
  // Use GCD
  dispatch_async(self.queue, ^{
    DLOG(@"Get histogram for item %@", itemId);
    
    // Track errors
    NSError *error = nil;
    
    // Check parameters
    if (![self checkUrl:self.url error:&error] ||
        ![self checkItemId:itemId error:&error]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        block(nil, error);
      });
      return;
    }
    
    // Build the query string
    NSMutableDictionary *query = [NSMutableDictionary dictionary];
    [query setObject:itemId forKey:ITEM_ID];

    
    // Build the request path
    NSURL *requestUrl = APPEND_PATH(@"rating/histogram", self.url);
    requestUrl = APPEND_QUERY([self toStringFromDict:query], requestUrl);
    
    // Make the request
    int responseCode = [self requestWithUrl:requestUrl body:nil resultClazz:[Histogram class] completionBlock:block];
    DLOG(@"Get histogram request executed with response code %d", responseCode);
  });
}



# pragma mark - Like methods

// Like an item
- (void)likeItem:(NSString*)itemId completionBlock:(void(^)(NSError*))block {
  // TODO
}


// Get the individual ratings for an item
- (void)likesForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


# pragma mark - Comment methods

// Add a comment to an item
- (void)commentItem:(NSString*)itemId withComment:(NSString*)comment completionBlock:(void(^)(NSError*))block {
  // TODO
}


// Get all individual comments for an item
- (void)commentsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


# pragma mark - Favorites methods

// Add item to favorites
- (void)addItemToFavorites:(NSString*)itemId completionBlock:(void(^)(NSError*))block {
  // TODO
}


// Remove item from favorites
- (void)removeItemFromFavorites:(NSString*)itemId completionBlock:(void(^)(NSError*))block {
  // TODO
}


// Get my favorite items
- (void)favoritesWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


# pragma mark - Getters and setters


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


#define USER_AGENT @"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3"
// Getter for the user agent
- (NSString*)userAgent {
  static PiwikTrackerUserAgentReader *userAgentReader = nil;
  
  if (!userAgent_) {
    
    if (!userAgentReader) {
      userAgentReader = [[PiwikTrackerUserAgentReader alloc] init];
      [userAgentReader userAgentStringWithCallbackBlock:^(NSString *userAgent) {
        userAgent_ = userAgent;
        [userAgent_ retain];
        [userAgentReader release];
      }];
    }
    
    return USER_AGENT;
  }
  
  return userAgent_;
}


# pragma mark - Helper methods

// Send a request to the backend service
- (NSInteger)requestWithUrl:(NSURL*)url body:(NSDictionary*)body resultClazz:(Class)clazz completionBlock:(void(^)(id result, NSError* error))block; {
    
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
    httpRequest.cachePolicy = NSURLRequestReloadIgnoringLocalAndRemoteCacheData;
    httpRequest.timeoutInterval = REQUEST_TIMEOUT;
    
    // Set User-Agent header
    [httpRequest setValue:self.userAgent forHTTPHeaderField:@"User-Agent"];
    
    // If POST add body
    if ([httpRequest.HTTPMethod isEqualToString:@"POST"])
      [httpRequest setHTTPBody:[[self toStringFromDict:body] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // Make the request
    DLOG(@"Sending backend request %@", httpRequest);
    NSURLResponse *response = nil;
    NSError *error = nil;
    NSData *data = [NSURLConnection sendSynchronousRequest:httpRequest returningResponse:&response error:&error];
    
    // Handle the http response
    int responseCode = [(NSHTTPURLResponse*)response statusCode];
    if (responseCode == 200) {
      // Parse the JSON
      id ratingsJSON = [data objectFromJSONData];
      
      // Map JSON into domain object
      id result = [ratingsJSON mapToClass:clazz withError:&error];
      if (result)
        // Run the completion block
        dispatch_async(dispatch_get_main_queue(), ^{ 
          block(result, nil);
        });
      else
        // Mapping failed, Run the completion block with error
        dispatch_async(dispatch_get_main_queue(), ^{ 
          block(nil, [self parsingErrorWithDescription:@"Not possible to map the parsed JSON, got error %@", [error userInfo]]);
        });
      
    } else {
      // Run the complition block with error
      dispatch_async(dispatch_get_main_queue(), ^{ 
        block(nil, [self parsingErrorWithDescription:@"Network request failed with respose code %d and error message %@", 
                    responseCode, [error userInfo]]);
      });
    }

    // Return the http response code
    return responseCode;
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


// Convert the user rating into the range supported by the backend service 0..100
- (NSInteger)toServerRating:(NSInteger)userRating {
  return userRating * (100 / self.maximumRating);
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


# pragma mark - Validation of input parameters

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

- (BOOL)checkAppUser:(NSString*)user error:(NSError**)error {
  if (!user || [user length] == 0) {
    *error = [self parsingErrorWithDescription:@"Application user name can not be nil or empty"];
    return NO;
  } else
    return YES;
}

- (BOOL)checkAppPassword:(NSString*)password error:(NSError**)error {
  if (!password || [password length] == 0) {
    *error = [self parsingErrorWithDescription:@"Application password can not be nil or empty"];
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
  if (rating != nil && [rating integerValue] < 1 && [rating integerValue] > self.maximumRating) {
    *error = [self parsingErrorWithDescription:[NSString stringWithFormat:@"Rating value must be between 1..%d", self.maximumRating]];
    return NO;
  } else
    return YES;
}

- (BOOL)checkComment:(NSString*)comment error:(NSError**)error {
  if (comment && [comment length] == 0) {
    *error = [self parsingErrorWithDescription:@"Comment can not be an empty string"];
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
