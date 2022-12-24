//
//  NewsVC.swift
//  SeanLee_TFG_ios_2202
//
//  Created by Sean Shinil Lee on 2/26/22.
//

import UIKit

class NewsVC: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var mTableView: UITableView!
    
    var mCollection: [News]?

    override func viewDidLoad() {
        super.viewDidLoad()
        
        print("in NewsVC viewDidLoad")

        mTableView.delegate = self
        mTableView.dataSource = self
//    
//        mTableView.reloadData()
    }

    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return mCollection!.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let news = mCollection![indexPath.row]
        if let cell = tableView.dequeueReusableCell(withIdentifier: "NewsCell") as? NewsCell{
            cell.configCell(_news: news)
            return cell
        }
        else{
            return NewsCell()
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "toNewsDetailVC", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let destination = segue.destination as! NewsDetailVC
        destination.mNews = mCollection![mTableView.indexPathForSelectedRow!.row]
    }
}
