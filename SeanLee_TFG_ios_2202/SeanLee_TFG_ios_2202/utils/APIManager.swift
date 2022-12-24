//
//  APIManager.swift
//  SeanLee_TFG_ios_2202
//
//  Created by Sean Shinil Lee on 2/2/22.
//

import Foundation

public class APIManager{
    
    func getNewsFromAPI(){
        
        let headers = [
            "x-rapidapi-host": "livescore6.p.rapidapi.com",
            "x-rapidapi-key": "87092e5811msh686251dc948c110p12d3cejsn68a77a1b3734"
        ]

        let request = NSMutableURLRequest(url: NSURL(string: "https://livescore6.p.rapidapi.com/news/v2/list-by-sport?category=2021020913320920836&page=1")! as URL,
                                                cachePolicy: .useProtocolCachePolicy,
                                            timeoutInterval: 10.0)
        request.httpMethod = "GET"
        request.allHTTPHeaderFields = headers

        let session = URLSession.shared
        let dataTask = session.dataTask(with: request as URLRequest, completionHandler: { (data, response, error) -> Void in
            if (error != nil) {
                print(error)
            } else {
                let httpResponse = response as? HTTPURLResponse
                print(httpResponse)
            }
        })

        dataTask.resume()
        

//        do{
//            let result = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as Any
//        }catch{
//            print("something went wrong")
//        }
    }
    
    func convertData(){
        
    }
    
    struct Response: Codable{
        let results: NewsResult
        let status: String
    }
    
    struct NewsResult: Codable{
        let title: String
        let date: String
        let description: String
    }
    
}
