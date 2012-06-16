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

- (IBAction)statRating:(id)sender {
  NSLog(@"Start rating");
  self.reviewer = [PocketReviewer sharedReviewer];
  NSURL *serviceURL = [NSURL URLWithString:@"http://pocket-reviews.appspot.com/api/"];
  BOOL result = [self.reviewer startReviewingWithServiceUrl:serviceURL domain:@"dev" anonymousUser:YES withError:nil];
  NSAssert(result, @"Failed to start rating");
}


- (IBAction)rateItem:(UIButton *)sender {
  NSString *item = [NSString stringWithFormat:@"A00%d", sender.tag];
  NSLog(@"Rate item %@", item);
  
  UISegmentedControl *segment = nil;
  switch (sender.tag) {
    case 1:
      segment = self.A001Segment;
      break;
    case 2:
      segment = self.A002Segment;
      break;
    case 3:
      segment = self.A003Segment;
      break;
    default:
      break;
  }
  
  NSInteger rating = segment.selectedSegmentIndex + 1;
  [self.reviewer rateItem:item withRating:rating completionBlock:^(NSError* error) {
    if (!error)
      NSLog(@"Rate method was successful");
    else 
      NSLog(@"Rate method failed with error %@", [error userInfo]);
  }];
  
}


- (IBAction)rating:(UIButton *)sender {  
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
  
  [self.reviewer averageRatingForItem:item completionBlock:^(Rating* rating, NSError* error) {
    if (!error) {
      NSLog(@"Get rating method was successful");
      label.text = [NSString stringWithFormat:@"%.1f (%d)", rating.averageRating, rating.numberOfRatings];
      [label setNeedsDisplay];
    } else 
      NSLog(@"Get rating method failed with error %@", [error userInfo]);
  }];
  
}

- (IBAction)allRatings:(id)sender {
  NSLog(@"Get all ratings");
  [self.reviewer averageRatingForItems:[NSArray arrayWithObjects:@"A001", @"A002", @"A003", nil] completionBlock:^(NSArray* ratings, NSError* error) {
    if (!error) {
      NSLog(@"Get ratings for a list of items was successful");
      for (Rating *rating in ratings) {
        if ([rating.itemId isEqualToString:@"A001"])
          self.A001Label.text = [NSString stringWithFormat:@"%.1f (%d)", rating.averageRating, rating.numberOfRatings];
        else if ([rating.itemId isEqualToString:@"A002"])
          self.A002Label.text = [NSString stringWithFormat:@"%.1f (%d)", rating.averageRating, rating.numberOfRatings];
        else if ([rating.itemId isEqualToString:@"A003"])
          self.A003Label.text = [NSString stringWithFormat:@"%.1f (%d)", rating.averageRating, rating.numberOfRatings];
      }
      [self.A001Label setNeedsDisplay];
      [self.A002Label setNeedsDisplay];
      [self.A003Label setNeedsDisplay];
    }
  }];
}


- (IBAction)nearbyRatings:(id)sender {
  NSLog(@"Get nearby ratings");
  
  [self.reviewer nearbyTopAverageRatingsWithinRadius:kDefaultRadius maxNumberOfResults:10 completionBlock:^(NSArray* ratings, NSError* error) {
    if (!error) {
      NSLog(@"Get nearby ratins for items was successful");
      NSLog(@"Ratings %@", ratings);
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
