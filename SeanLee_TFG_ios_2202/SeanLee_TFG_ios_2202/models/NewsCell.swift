//
//  NewsCell.swift
//  SeanLee_TFG_ios_2202
//
//  Created by Sean Shinil Lee on 2/26/22.
//

import UIKit

class NewsCell: UITableViewCell {

    @IBOutlet weak var mTimestamp: UILabel!
    @IBOutlet weak var mTitle: UILabel!
    @IBOutlet weak var mDescription: UILabel!
    
    var mNews: News!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func configCell(_news: News){
        
        self.mNews = _news
        
        self.mTitle.text = _news.mTitle
        self.mTimestamp.text = _news.mTimestamp
        self.mDescription.text = _news.mSubtitle
        
    }
    
}
