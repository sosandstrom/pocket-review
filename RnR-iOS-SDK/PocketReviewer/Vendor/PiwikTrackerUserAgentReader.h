//
//  PiwikTrackerUserAgentReader.h
//
//  Created by Mattias Levin on 5/14/12.
//  Copyright (c) 2012 Mattias Levin. All rights reserved.
//

#import <Foundation/Foundation.h>


/**
 Get the device user agent profile by launching a web view and reading the http header.
 No content is actaully loaded in the web view.
 */
@interface PiwikTrackerUserAgentReader : NSObject <UIWebViewDelegate>

- (void)userAgentStringWithCallbackBlock:(void (^)(NSString*))block;

@end
