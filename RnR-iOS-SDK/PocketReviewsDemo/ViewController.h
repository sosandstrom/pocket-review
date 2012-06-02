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

- (IBAction)statRating:(id)sender;

- (IBAction)rateItem:(UIButton *)sender;

- (IBAction)rating:(UIButton *)sender;

@end
