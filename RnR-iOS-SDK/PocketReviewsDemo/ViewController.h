//
//  ViewController.h
//  PocketReviewsDemo
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController

@property (retain, nonatomic) IBOutlet UISegmentedControl *A001Segment;
@property (retain, nonatomic) IBOutlet UISegmentedControl *A002Segment;
@property (retain, nonatomic) IBOutlet UISegmentedControl *A003Segment;


@property (retain, nonatomic) IBOutlet UILabel *A001Label;
@property (retain, nonatomic) IBOutlet UILabel *A002Label;
@property (retain, nonatomic) IBOutlet UILabel *A003Label;

- (IBAction)myRatings:(id)sender;

- (IBAction)rateItem:(UIButton *)sender;

- (IBAction)item:(UIButton *)sender;

- (IBAction)allItems:(id)sender;

- (IBAction)nearbyItems:(id)sender;

@end
