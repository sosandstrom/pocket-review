//
//  ViewController.m
//  PocketReviewsDemo
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import "ViewController.h"
#import "PocketReviewer.h"

@interface ViewController ()

@end

@interface ViewController ()
@property (nonatomic, retain) PocketReviewer *reviewer;
@end


@implementation ViewController
@synthesize A001Segment;
@synthesize A002Segment;
@synthesize A003Segment;
@synthesize A001Label;
@synthesize A002Label;
@synthesize A003Label;

@synthesize reviewer = reviewer_;

- (void)viewDidLoad
{
  [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
  
  NSLog(@"Start rating");
  self.reviewer = [PocketReviewer sharedReviewer];
  NSURL *serviceURL = [NSURL URLWithString:@"http://localhost:8888/api/"];
  BOOL result = [self.reviewer startReviewingWithUrl:serviceURL domain:@"dev" appUser:@"user"
                                         appPassword:@"password" anonymousUser:YES withError:nil];
  NSAssert(result, @"Failed to start rating");
  
}

- (void)viewDidUnload
{
  [self setA001Segment:nil];
  [self setA002Segment:nil];
  [self setA003Segment:nil];
  [self setA001Label:nil];
  [self setA002Label:nil];
  [self setA003Label:nil];
  [super viewDidUnload];
  // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  } else {
    return YES;
  }
}


- (IBAction)myRatings:(id)sender {
  
  [self.reviewer myRatedItemsWithCompletionBlock:^(NSArray* items, NSError* error) {
    if (!error) {
      NSLog(@"Get my rated items was successful with %d results returned", [items count]);
      NSLog(@"Ratings %@", items);
    } else 
      NSLog(@"Get my rated items failed");
  }];

}


- (IBAction)rateItem:(UIButton *)sender {
  NSString *item = [NSString stringWithFormat:@"A00%d", sender.tag];
  NSLog(@"Rate item %@", item);
  
  UISegmentedControl *segment = nil;
  UILabel *label = nil;
  switch (sender.tag) {
    case 1:
      segment = self.A001Segment;
      label = self.A001Label;
      break;
    case 2:
      segment = self.A002Segment;
      label = self.A002Label;
      break;
    case 3:
      segment = self.A003Segment;
      label = self.A003Label;
      break;
    default:
      break;
  }
  
  NSInteger rating = segment.selectedSegmentIndex + 1;
  [self.reviewer rateItem:item withRating:rating comment:nil completionBlock:^(Rating* newRating, NSError* error) {
    if (!error) {
      NSLog(@"Rating was successful");
    }
    
    else 
      NSLog(@"Rating failed with error %@", [error userInfo]);
  }];
  
}


- (IBAction)item:(UIButton *)sender {
  NSString *item = [NSString stringWithFormat:@"A00%d", sender.tag];
  NSLog(@"Rate item %@", item);
  
  UILabel *label = nil;
  switch (sender.tag) {
    case 1:
      label = self.A001Label;
      break;
    case 2:
      label = self.A002Label;
      break;
    case 3:
      label = self.A003Label;
      break;
    default:
      break;
  }
  
  [self.reviewer itemWithId:item completionBlock:^(Item* item, NSError* error) {
    if (!error) {
      NSLog(@"Get item was successful");
      label.text = [NSString stringWithFormat:@"%.1f (%d)", item.averageRating, item.numberOfRatings];
    } else 
      NSLog(@"Get item failed with error %@", [error userInfo]);
  }];
  
}

- (IBAction)allItems:(id)sender {
  NSLog(@"Get all items");
  [self.reviewer itemsWithIds:[NSArray arrayWithObjects:@"A001", @"A002", @"A003", nil] completionBlock:^(NSArray* items, NSError* error) {
    if (!error) {
      NSLog(@"Get items for a list of items was successful");
      for (Item *item in items) {
        if ([item.itemId isEqualToString:@"A001"])
          self.A001Label.text = [NSString stringWithFormat:@"%.1f (%d)", item.averageRating, item.numberOfRatings];
        else if ([item.itemId isEqualToString:@"A002"])
          self.A002Label.text = [NSString stringWithFormat:@"%.1f (%d)", item.averageRating, item.numberOfRatings];
        else if ([item.itemId isEqualToString:@"A003"])
          self.A003Label.text = [NSString stringWithFormat:@"%.1f (%d)", item.averageRating, item.numberOfRatings];
      }
    }
  }];
}


- (IBAction)nearbyItems:(id)sender {
  NSLog(@"Get nearby items");
  
  [self.reviewer nearbyTopRatedItemsWithRadius:kDefaultRadius maxNumberOfResults:10 completionBlock:^(NSArray* items, NSError* error) {
    if (!error) {
      NSLog(@"Get nearby items was successful with %d results returned", [items count]);
      NSLog(@"Items %@", items);
    } else 
      NSLog(@"Get nearby items failed");
  }];
}


- (void)dealloc {
  [A001Segment release];
  [A002Segment release];
  [A003Segment release];
  [A001Label release];
  [A002Label release];
  [A003Label release];
  [reviewer_ release];
  [super dealloc];
}
@end
