//
//  NewsDetailVC.swift
//  SeanLee_TFG_ios_2202
//
//  Created by Sean Shinil Lee on 2/26/22.
//

import UIKit

class NewsDetailVC: UIViewController {

    @IBOutlet weak var mTimestamp: UILabel!
    @IBOutlet weak var mTitle: UILabel!
    @IBOutlet weak var mDescription: UILabel!
    
    var mNews: News?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        mTimestamp.text = mNews?.mTimestamp
        mTitle.text = mNews?.mTitle
        mDescription.text = mNews?.mDescription
    }
}
