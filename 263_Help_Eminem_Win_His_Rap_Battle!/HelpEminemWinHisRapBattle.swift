let filePronouncingDict = "cmudict-0.7b"
let filePhonemeDesc = "cmudict-0.7b.phones"

if let dir = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.AllDomainsMask, true).first {
        let path = NSURL(fileURLWithPath: dir).URLByAppendingPathComponent(filePronouncingDict)

        //reading
        do {
            let pronouncingDictText = try NSString(contentsOfURL: path, encoding: NSUTF8StringEncoding)
        }
        catch {/* error handling here */}

        let path = NSURL(fileURLWithPath: dir).URLByAppendingPathComponent(filePhonemeDesc)

        //reading
        do {
            let phonemeDescText = try NSString(contentsOfURL: path, encoding: NSUTF8StringEncoding)
        }
        catch {/* error handling here */}
    }
