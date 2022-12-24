//
//  Article.swift
//  SeanLee_TFG_ios_2202
//
//  Created by Sean Shinil Lee on 2/26/22.
//

import Foundation

public struct News{
    
    let mAuthor: String
    let mTimestamp: String
    let mTitle: String
    let mSubtitle: String
    let mDescription: String
    
    var getAuthorName: String{ return mAuthor }
    var getSubtitle: String{ return mSubtitle }
    var getTimestamp: String{ return mTimestamp }
    var getTitle: String{ return mTitle }
    var getDescription: String{ return mDescription }
    
    init(_author: String, _timestamp: String, _title: String, _subtitle: String, _description: String){
        
        self.mAuthor = _author
        self.mTimestamp = String(_timestamp.split(separator: "T")[0])
        self.mTitle = _title
        self.mSubtitle = _subtitle
        self.mDescription = _description
        
    }
    
    func timestampToTimeGap(_timestamp: String) -> String{
        return String(_timestamp.split(separator: "T")[0])
    }

    
}

enum ApiError: Error{
    case noDataAvailable
    case cannotProcessData
}

public struct ApiRespose: Decodable{
    var data: [Article]
}

public struct Article: Decodable{
    
    var title: String
    var subtitle: String?
    var body: [BodyDetail]
    var published_at: String
}

public struct BodyDetail: Decodable{
    var data: InnerData
}

public struct InnerData: Decodable{
    var content: String?
}


