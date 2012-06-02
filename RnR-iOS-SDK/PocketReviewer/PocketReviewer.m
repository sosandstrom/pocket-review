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

// Defines
#define USER_AGENT @"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3"
#define REQUEST_TIMEOUT 10

#define RATING @"rating"
#define USERID @"username"
#define LATITUDE @"latitude"
#define LONGITUDE @"longitude"

#define AVERAGE @"average"
#define RATING_COUNT @"ratingCount"
#define ID @"id"


// Private stuff
@interface PocketReviewer ()

- (void)dispatchRateItem:(NSString*)itemId forLatitude:(NSNumber*)latitude andLongitude:(NSNumber*)longitude 
              withRating:(NSNumber*)rating completionBlock:(void(^)(NSError*))block;;
- (NSString*)toStringFromDict:(NSDictionary*)dict;
- (NSError*)parsingErrorWithDescription:(NSString*)format, ...;

@property (nonatomic, retain) NSURL *url;
@property (nonatomic, retain) NSString *domain;
@property (nonatomic) BOOL anonymous;
@property (nonatomic) dispatch_queue_t queue;

@end


// Implementation
@implementation PocketReviewer


@synthesize userId = userId_;
@synthesize url = url_;
@synthesize dryRun = dryRun_;
@synthesize domain = domain_;
@synthesize anonymous = anonymous_;
@synthesize queue = queue_;


// Release instance variables
- (void)dealloc {
  [userId_ release];
  [url_ release];
  [domain_ release];
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
  
  // Check the url
  if (!url) {
    ALOG(@"URL can not be nil");
    *error = [self parsingErrorWithDescription:@"URL can not be nil"];
    return NO;
  }
  self.url = url;
  
  // Check the domain
  if (!domain) {
    ALOG(@"Domain can not be nil");
    *error = [self parsingErrorWithDescription:@"Domain can not be nil"];
    return NO;
  }
  self.domain = domain;
    
  self.anonymous = anonymous;
  
  return YES;
}


// Rate an item
- (void)rateItem:(NSString*)itemId withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block {
  [self dispatchRateItem:itemId forLatitude:nil andLongitude:nil withRating:[NSNumber numberWithInteger:rating] completionBlock:block];
}


// Rate an item with latitude and longitude
- (void)rateItem:(NSString*)itemId forLatitude:(float)latitude andLongitude:(float)longitude 
      withRating:(NSInteger)rating completionBlock:(void(^)(NSError*))block {
  [self dispatchRateItem:itemId forLatitude:[NSNumber numberWithFloat:latitude] andLongitude:[NSNumber numberWithFloat:longitude] 
          withRating:[NSNumber numberWithInteger:rating] completionBlock:block];
}


// Internal rating method
- (void)dispatchRateItem:(NSString*)itemId forLatitude:(NSNumber*)latitude andLongitude:(NSNumber*)longitude 
          withRating:(NSNumber*)rating completionBlock:(void(^)(NSError*))block {
  
  // Use GDC
  dispatch_async(self.queue, ^{
    DLOG(@"Rate an item");
    
    // Track errors
    NSError *error = nil;
    
    // Check that the rating is configured and started
    if (!self.url) {
      ALOG(@"The rator is not started yet");
      error = [self parsingErrorWithDescription:@"The rator is not started yet"];
    }
    
    // Check the item
    if (!itemId) {
      ALOG(@"The item id can not be nil");
      error = [self parsingErrorWithDescription:@"The item id can not be nil"];
    }
    
    // Check the rating
    if ([rating integerValue] < 0 || [rating integerValue] > 5) {
      ALOG(@"The rating value must be between 0 and 5");
      error = [self parsingErrorWithDescription:@"The rating value must be between 0 and 5"];
    }
    
    // Check latitude
    if ([latitude floatValue] < -90.0f || [latitude floatValue] > 90.0f) {
      ALOG(@"The latitude must be between -90.0 and 90.0");
      error = [self parsingErrorWithDescription:@"The latitude must be between -90.0 and 90.0"];
    }  
    
    // Check longitude
    if ([longitude floatValue] < -180.0f || [longitude floatValue] > 180.0f) {
      ALOG(@"The longitude must be between -90.0 and 90.0");
      error = [self parsingErrorWithDescription:@"The longitude must be between -180.0 and 180.0"];
    } 
    
    if (error) {
      // Run the complition block with error
      block(error);
      return;
    }
    
    // Build the request path
    NSURL *rateUrl = [self.url URLByAppendingPathComponent:[NSString stringWithFormat:@"%@/rating/%@", self.domain, itemId]];
    
    // Collect request paramters
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    if (self.userId) [dict setObject:self.userId forKey:USERID];
    if (rating) [dict setObject:rating forKey:RATING];
    if (latitude) [dict setObject:latitude forKey:LATITUDE];
    if (longitude) [dict setObject:longitude forKey:LONGITUDE];
    
    // Check if dry run
    if (self.dryRun) {
      NSMutableString *prettyURL = [NSMutableString stringWithString:[rateUrl description]];
      [prettyURL appendFormat:@"?%@", [self toStringFromDict:dict]];
      [prettyURL replaceOccurrencesOfString:@"?" withString:@"\n  " options:NSLiteralSearch 
                                      range:NSMakeRange(0, [prettyURL length])];
      [prettyURL replaceOccurrencesOfString:@"&" withString:@"\n  " options:NSLiteralSearch 
                                      range:NSMakeRange(0, [prettyURL length])];
      ALOG(@"Dry run request:\n%@", prettyURL);
    } else {
      // Create the http request
      NSMutableURLRequest *httpRequest = [NSMutableURLRequest requestWithURL:rateUrl];
      [httpRequest setHTTPMethod:@"POST"];
      [httpRequest setCachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData];
      [httpRequest setTimeoutInterval:REQUEST_TIMEOUT];
      
      // Set User-Agent header
      [httpRequest setValue:USER_AGENT forHTTPHeaderField:@"User-Agent"];
      
      // POST parameters
      [httpRequest setHTTPBody:[[self toStringFromDict:dict] dataUsingEncoding:NSUTF8StringEncoding]];
      
      // Make the requests
      NSURLResponse *response = nil;
      DLOG(@"Sending rate request %@", httpRequest);
      [NSURLConnection sendSynchronousRequest:httpRequest returningResponse:&response error:&error];
      
      // Handle the http response
      if ([(NSHTTPURLResponse*)response statusCode] == 200) {
        DLOG(@"Response code 200");
        // Run the completion block
        block(nil);
      } else {
        ALOG(@"HTTP rate request to service failed with respose code %d and error message %@", 
             [(NSHTTPURLResponse*)response statusCode], [error userInfo]);
        error = [self parsingErrorWithDescription:@"HTTP rate request to service failed with respose code %d and error message %@", 
                 [(NSHTTPURLResponse*)response statusCode], [error userInfo]];
        // Run the complition block with error
        block(error);
      }
    }
    
  });
}


// Get a rating for an item
- (void)ratingForItem:(NSString*)itemId completionBlock:(void(^)(Rating*, NSError*))block {
  
  // Use GDC
  dispatch_async(self.queue, ^{
    DLOG(@"Get a rating");
    
    // Track errors
    NSError *error = nil;
    
    // Check that the rating is configured and started
    if (!self.url) {
      ALOG(@"The rator is not started yet");
      error = [self parsingErrorWithDescription:@"The rator is not started yet"];
    }
    
    // Check the item
    if (!itemId) {
      ALOG(@"The item id can not be nil");
      error = [self parsingErrorWithDescription:@"The item id can not be nil"];
    }
    
    if (error) {
      // Run the completion block with error
      block(nil, error);
      return;
    }
    
    // Build the request path
    NSURL *requestUrl = [self.url URLByAppendingPathComponent:[NSString stringWithFormat:@"%@/rating/%@", self.domain, itemId]];
    
    // Check if dry run
    if (self.dryRun) {
      NSMutableString *prettyURL = [NSMutableString stringWithString:[requestUrl description]];
      [prettyURL replaceOccurrencesOfString:@"?" withString:@"\n  " options:NSLiteralSearch 
                                      range:NSMakeRange(0, [prettyURL length])];
      [prettyURL replaceOccurrencesOfString:@"&" withString:@"\n  " options:NSLiteralSearch 
                                      range:NSMakeRange(0, [prettyURL length])];
      ALOG(@"Dry run request:\n%@", prettyURL);
    } else {
      // Create the http request
      NSMutableURLRequest *httpRequest = [NSMutableURLRequest requestWithURL:requestUrl];
      [httpRequest setHTTPMethod:@"GET"];
      [httpRequest setCachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData];
      [httpRequest setTimeoutInterval:REQUEST_TIMEOUT];
      
      // Set User-Agent header
      [httpRequest setValue:USER_AGENT forHTTPHeaderField:@"User-Agent"];
      
      // Make the requests
      NSURLResponse *response = nil;
      DLOG(@"Sending get rating request %@", httpRequest);
      NSData *jsonResponse = [NSURLConnection sendSynchronousRequest:httpRequest returningResponse:&response error:&error];
      
      // Handle the http response
      if ([(NSHTTPURLResponse*)response statusCode] == 200) {
        DLOG(@"Response code 200");
        // Parse the JSON
        NSDictionary *ratingObj = [jsonResponse objectFromJSONData];
        Rating *rating = [Rating rating];
        rating.averageRating = [(NSNumber*)[ratingObj valueForKey:AVERAGE] floatValue];
        rating.numberOfRatings = [(NSNumber*)[ratingObj valueForKey:RATING_COUNT] integerValue];
        rating.itemId = [ratingObj valueForKey:ID];
        
        // Run the completion block
        block(rating, nil);
      } else {
        ALOG(@"HTTP get rating request to service failed with respose code %d and error message %@", 
             [(NSHTTPURLResponse*)response statusCode], [error userInfo]);
        error = [self parsingErrorWithDescription:@"HTTP get rating request to service failed with respose code %d and error message %@", 
                 [(NSHTTPURLResponse*)response statusCode], [error userInfo]];
        // Run the completion block with error
        block(nil, error);
      }
    }
    
  });
}


// Get raings for an array of items
- (void)ratingForItems:(NSArray*)itemIds completionBlock:(void(^)(NSArray*, NSError*))block {
  DLOG(@"Get rating for an array of items");
  
  // Track errors
  NSError *error = nil;
  
  // Check that the rating is configured and started
  if (!self.url) {
    ALOG(@"The rator is not started yet");
    error = [self parsingErrorWithDescription:@"The rator is not started yet"];
  }
  
  // Check the item
  if (!itemIds) {
    ALOG(@"The item id array can not be nil");
    error = [self parsingErrorWithDescription:@"The item id array can not be nil"];
  }
  
  if (!error) {
    // Run the fail block
    block(nil, error);
    return;
  }
  
  // TODO
}


// Get my ratings
- (void)myRatingsWithCompletionBlock:(void(^)(NSArray*, NSError*))block {
  DLOG(@"Get my ratings");
  
  // Track errors
  NSError *error = nil;
  
  // Check that the rating is configured and started
  if (!self.url) {
    ALOG(@"The rator is not started yet");
    error = [self parsingErrorWithDescription:@"The rator is not started yet"];
  }
  
  if (!self.userId) {
    ALOG(@"The user id can not be nil");
    error = [self parsingErrorWithDescription:@"The user id can not be nil"];
  }
  
  if (!error) {
    // Run the fail block
    block(nil, error);
    return;
  }
  
  // TODO
}


// Add a review comment
- (void)reviewItem:(NSString*)itemId withReview:(NSString*)review completionBlock:(void(^)(NSError*))block {
  // TODO
}


// Get all review comments for an item
- (void)reviewsForItem:(NSString*)itemId completionBlock:(void(^)(NSArray*, NSError*))block {
  // TODO
}


// Get all my review comments
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


// Create a paramter string from a NSDictionary                                        
- (NSString*)toStringFromDict:(NSDictionary*)dict {
  // Create the parameter list
  NSMutableArray *params = [NSMutableArray array];
  [dict enumerateKeysAndObjectsUsingBlock:^(id key, id value, BOOL *stop) {
      [params addObject:[NSString stringWithFormat:@"%@=%@", key, value]];
  }];
  
  return [params componentsJoinedByString:@"&"];
}


// Format an error message and raise and expection
- (NSError*)parsingErrorWithDescription:(NSString*)format, ... {   
  // Create a formatted string from input parameters
  va_list varArgsList;
  va_start(varArgsList, format);
  NSString *formatString = [[[NSString alloc] initWithFormat:format arguments:varArgsList] autorelease];
  va_end(varArgsList);
  
  ALOG(@"Parsing error with message: %@", formatString);
  
  // Create the error and store the state
  NSDictionary *errorInfo = [NSDictionary dictionaryWithObjectsAndKeys: 
                             NSLocalizedDescriptionKey, formatString,
                             nil];
  return [NSError errorWithDomain:@"levin.mattias.PocketReview.ErrorDomain" code:1 userInfo:errorInfo];
}


@end
