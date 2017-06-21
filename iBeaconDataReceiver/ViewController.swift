//
//  ViewController.swift
//  iBeaconDataReceiver
//
//  Created by 叶思帆 on 12/06/2017.
//  Copyright © 2017 Sifan Ye. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    @IBOutlet var detailLabels: [UILabel]!
    @IBOutlet var proximityLabels: [UILabel]!
    @IBOutlet weak var locationLabel: UILabel!
    
    let locManager = CLLocationManager()
    let region = CLBeaconRegion(proximityUUID: UUID(uuidString: "FDA50693-A4E2-4FB1-AFCF-C6EB07647825")!, major:1, identifier: "FmxyBeacon") //Here: filter out those that doesn't have major == 1
    
    var timer = Timer()
    
    struct beaconAccumuDist{
        let minor: NSNumber
        var accumuDist: [Double]
        
        func equals(to another: beaconAccumuDist) -> Bool{
            return self.minor == another.minor
        }
    }
    
    var accumuDist: [beaconAccumuDist] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        locManager.delegate = self
        if CLLocationManager.authorizationStatus() != CLAuthorizationStatus.authorizedWhenInUse {
            locManager.requestWhenInUseAuthorization()
        }
        locManager.startRangingBeacons(in: region)
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    func locationManager(_ manager: CLLocationManager, didRangeBeacons beacons: [CLBeacon], in inRegion: CLBeaconRegion) {
        let sortedBeacons = beacons.sorted{$0.rssi > $1.rssi}
        
        //httpPost(Beacons: sortedBeacons) For HTTP functionality test
        
        if sortedBeacons.count > 0 && sortedBeacons.count < 3{
            for index in 0...(sortedBeacons.count-1){
                let beacon = sortedBeacons[index]
                if beacon.proximity == CLProximity.unknown{
                    detailLabels[index].text = "No Signal"
                }else{
                    detailLabels[index].text = "Major: \(beacon.major)\nMinor: \(beacon.minor)\nRSSI: \(beacon.rssi)\nAccuracy:\(beacon.accuracy)\n"
                }
                switch beacon.proximity {
                case CLProximity.far:
                    proximityLabels[index].text = "Far"
                case CLProximity.near:
                    proximityLabels[index].text = "Near"
                case CLProximity.immediate:
                    proximityLabels[index].text = "Immediate"
                case CLProximity.unknown:
                    proximityLabels[index].text = "Unknown"
                }
            }
        }else if sortedBeacons.count > 3{
            for index in 0...2{
                let beacon = sortedBeacons[index]
                if beacon.proximity == CLProximity.unknown{
                    detailLabels[index].text = "No Signal"
                }else{
                    detailLabels[index].text = "Major: \(beacon.major)\nMinor: \(beacon.minor)\nRSSI: \(beacon.rssi)\nAccuracy:\(beacon.accuracy)\n"
                }
                switch beacon.proximity {
                case CLProximity.far:
                    proximityLabels[index].text = "Far"
                case CLProximity.near:
                    proximityLabels[index].text = "Near"
                case CLProximity.immediate:
                    proximityLabels[index].text = "Immediate"
                case CLProximity.unknown:
                    proximityLabels[index].text = "Unknown"
                }
            }
            
        }else{
            locationLabel.text = "Unknown"
        }
        if sortedBeacons.count>2{
            httpPost(Beacons: sortedBeacons)
        }
    }
    
    func httpPost(Beacons: Array<CLBeacon>){
        var request = URLRequest(url: URL(string: "http://10.250.111.185:8080/Trilateration/Lookup")!)
        request.httpMethod = "POST"
        //        let postString = "major1=5&minor1=1000&d1=3&major2=10135&minor2=29205&d2=3&major3=18985&minor3=21094&d3=3"
        //        For HTTP functionality test
        let postString = "major1=\(Beacons[0].major)&minor1=\(Beacons[0].minor)&d1=\(Beacons[0].accuracy)&major2=\(Beacons[1].major)&minor2=\(Beacons[1].minor)&d2=\(Beacons[1].accuracy)&major3=\(Beacons[2].major)&minor3=\(Beacons[2].minor)&d3=\(Beacons[2].accuracy)"
        request.httpBody = postString.data(using: String.Encoding.utf8);
        let task = URLSession.shared.dataTask(with: request) { (data: Data?, response: URLResponse?, error: Error?) in
            if error != nil{
                return
            }
            if let data = data, let string = String(data: data, encoding: .utf8) {
                self.locationLabel.text = string
            }
        }
        task.resume()
    }
    
    func runTimer(){
        timer = Timer.scheduledTimer(timeInterval: 0.5, target: self, selector: (#selector(ViewController.timerHandler)), userInfo: nil, repeats: true)
    }
    
    func timerHandler(){
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

