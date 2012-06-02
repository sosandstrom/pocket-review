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
  BOOL result = [self.reviewer startReviewingWithServiceUrl:serviceURL domain:@"dev" anonymous:YES withError:nil];
  NSAssert(result, @"Failed to start rating");
}


- (IBAction)rateItem:(UIButton *)sender {
  NSString *item = [NSString stringWithFormat:@"A00%d", sender.tag];
  NSLog(@"Rate item %@", item);
  
  UISegmentedControl *segment = nil;
  switch (sender.tag) {
    case 1:
      segment = A001Segment;
      break;
    case 2:
      segment = A002Segment;
      break;
    case 3:
      segment = A003Segment;
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
      label = A001Label;
      break;
    case 2:
      label = A002Label;
      break;
    case 3:
      label = A003Label;
      break;
    default:
      break;
  }
  
  [self.reviewer ratingForItem:item completionBlock:^(Rating* rating, NSError* error) {
    if (!error) {
      NSLog(@"Get rating method was successful");
      label.text = [NSString stringWithFormat:@"%.1f (%d)", rating.averageRating, rating.numberOfRatings];
      [label setNeedsDisplay];
    } else 
      NSLog(@"Get rating method failed with error %@", [error userInfo]);
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
