//
//  ViewController.swift
//  SeanLee_TFG_ios_2202
//
//  Created by Sean Shinil Lee on 2/2/22.
//

import UIKit
import Firebase
import FirebaseAuth

class LoginVC: UIViewController {
    
    @IBOutlet weak var tf_email: UITextField!
    @IBOutlet weak var tf_password: UITextField!
    @IBOutlet weak var btn_login: UIButton!
    
    @IBOutlet weak var label_err_email: UILabel!
    @IBOutlet weak var label_err_pwd: UILabel!
    
    var mUserEmail: String!

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        if Auth.auth().currentUser != nil{ // user logged in
            toMainVC()
        }
    }
    
    @IBAction func loginTap(_ sender: UIButton) {
        if let email = tf_email.text, let pwd = tf_password.text{
            Auth.auth().signIn(withEmail: email, password: pwd) { (_user, _error) in
                
                if _error == nil{
                    
                    if let user = _user{
                        self.mUserEmail = user.user.email
                        self.toMainVC()
                    }
                    
                }else{
                    
                    let alert = UIAlertController.configureAlertController( message: _error!.localizedDescription)
                    self.present(alert, animated: true)
                    
                    //toggleErrMsg(_toggleEmail: true, _togglePwd: true)
                }
            }
        }
    }
    
    let CASE_NONE: Int = -1
    let CASE_ID: Int = 0
    let CASE_PWD: Int = 1
    
    let ERR_ID: String = "Incorrect Email"
    let ERR_PWD: String = "Incorrect Password"
    
    func toggleErrMsg(_toggleEmail: Bool, _togglePwd: Bool){
        
        var label_err_id = ""
        var label_err_pwd = ""
        
        if(_toggleEmail){ label_err_id = ERR_ID }
        if(_togglePwd){ label_err_pwd = ERR_PWD }
        
        self.label_err_email.text = label_err_id
        self.label_err_pwd.text = label_err_pwd
    }

    func toMainVC(){
        performSegue(withIdentifier: "toLoadVC", sender: nil)
    }
    
    
}

extension UIAlertController {
    
     static func configureAlertController(with title: String = "Login Error", message: String) -> (UIAlertController){
         
         let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
         let action = UIAlertAction(title: "ОК",  style: .default) {(action) in}
         alertController.addAction(action)
    
         return alertController
     }
 }


