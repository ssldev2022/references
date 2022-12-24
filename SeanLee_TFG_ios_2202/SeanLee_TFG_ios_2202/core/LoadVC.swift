//
//  LoadVC.swift
//  SeanLee_TFG_ios_2202
//
//  Created by Sean Shinil Lee on 2/27/22.
//

import UIKit

class LoadVC: UIViewController {

    var mNewsCollection = [News]()

        override func viewDidLoad(){
            super.viewDidLoad()
        
            
            fetchNews()
        }
        
        override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
                let destination = segue.destination as! NewsVC
                destination.mCollection = mNewsCollection
            }
        
        func fetchNews(){
            let headers = [
                "x-rapidapi-host": "livescore6.p.rapidapi.com",
                "x-rapidapi-key": "87092e5811msh686251dc948c110p12d3cejsn68a77a1b3734"
            ]

            let request = NSMutableURLRequest(url: NSURL(string: "https://livescore6.p.rapidapi.com/news/v2/list-by-sport?category=2021020913320920836&page=1")! as URL,
                                                    cachePolicy: .useProtocolCachePolicy,
                                                timeoutInterval: 100.0)
            request.httpMethod = "GET"
            request.allHTTPHeaderFields = headers
            
            var fetchedData: NSDictionary = [:]

            let session = URLSession.shared
            let dataTask = session.dataTask(with: request as URLRequest, completionHandler: { [self] (data, response, error) -> Void in
                if (error != nil) {
                    print(error as Any)
                } else {
                    print("step 1")
                    
                    do{
                        let jsonData = try JSONSerialization.jsonObject(with: data!, options: []) as! NSDictionary
                        fetchedData = jsonData
                    
                        mNewsCollection = convertJson(_data: fetchedData)
                        DispatchQueue.main.async {
                            performSegue(withIdentifier: "toNewsVC", sender: nil)
                        }
                    }catch{
                        print("in catch")
                    }
                    
                }
            })

            dataTask.resume()
        }
        
        func convertJson(_data: NSDictionary) -> [News]{
            
            var convertedNews = [News]()
            
            if _data != nil{
                
                var title = ""
                var subtitle = ""
                var timestamp = ""
                var description = ""
                
                if let outerData = _data["data"] as? [[String: Any]]{
                    
                    for item in outerData {
                        
                        title = item["title"] as! String
                        subtitle = ""
                        
                        if item["subtitle"] != nil{
                            subtitle = item["subtitle"] as? String ?? ""
                        }
                        timestamp = item["title"] as! String
                    
                        for body in item["body"] as! [[String: Any]] {
                            let innerData = body["data"] as! [String:Any]
                                
                            if let content = innerData["content"] as? String{
                                description += content
                            }
                            
                        }
                        convertedNews.append(News(_author: "liverscore.com", _timestamp: timestamp, _title: title, _subtitle: subtitle, _description: description))
                    }
                }
            }
            return convertedNews
        }

}
