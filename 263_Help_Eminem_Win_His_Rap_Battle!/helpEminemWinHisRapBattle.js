var dict = "A  AH0\n" +
           "A(1)  EY1\n" +
           "A'S  EY1 Z\n" +
           "A.  EY1\n" +
           "A.'S  EY1 Z\n" +
           "A.S  EY1 Z\n" +
           "A42128  EY1 F AO1 R T UW1 W AH1 N T UW1 EY1 T\n" +
           "AA  EY2 EY1\n" +
           "AAA  T R IH2 P AH0 L EY1\n" +
           "AABERG  AA1 B ER0 G\n" +
           "AACHEN  AA1 K AH0 N\n" +
           "AACHENER  AA1 K AH0 N ER0\n" +
           "AAH  AA1\n" +
           "AAKER  AA1 K ER0\n" +
           "AALIYAH  AA2 L IY1 AA2\n" +
           "AALSETH  AA1 L S EH0 TH\n" +
           "AAMODT  AA1 M AH0 T\n" +
           "AANCOR  AA1 N K AO2 R\n" +
           "AARDEMA  AA0 R D EH1 M AH0\n" +
           "AARDVARK  AA1 R D V AA2 R K\n" +
           "AARDVARKS  AA1 R D V AA2 R K S\n" +
           "AARGH  AA1 R G\n" +
           "ABSOLUTION  AE2 B S AH0 L UW1 SH AH0 N\n" +
           "ANDALUSIAN  AE2 N D AH0 L UW1 SH AH0 N\n" +
           "SOLUTION  S AH0 L UW1 SH AH0 N\n" +
           "ZURKUHLEN   Z ER0 K Y UW1 L AH0 N";

function buildRhymeEndDict(dictString) {
    var phonemeStrings = dictString.split("\n");
    console.log(phonemeStrings);
    for (var i = 0; i < phonemeStrings.length; i++) {
        var phonemeString = phonemeStrings[i];
        var [word, ...phonemes] = phonemeString.split(" ");
        phonemes = phonemes.filter(function(obj) { return obj != "" })
        console.log(word)
        console.log(phonemes)
    }
}
rhymeDict = buildRhymeEndDict(dict);

var vowels = ["AA", "AE", "AH", "AO", "AW", "AY", "EH", "ER", "EY", "IH", "IY", "OW", "OY", "UH", "UW", "W", "Y"];

var input = "solution";

console.log(dict);

function findRhymeEnd(word) {
    
}
