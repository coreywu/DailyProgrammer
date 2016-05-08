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

$(document).ready(function() {
    $("#cmudict").load(function() {
        let cmudict = window.frames[0].document.body.innerHTML;

        /*
        let phonemeDict = buildPhonemeDict(cmudict);
        let phonemeDictCode = compilePhonemeDictCode(phonemeDict);
        console.log(phonemeDictCode);
        */

        let rhymeDict = buildRhymeEndDict(cmudict);
    });
});

function buildPhonemeDict(dictString) {
    var phonemeStrings = dictString.split("\n");
    var phonemeDict = new Map();
    for (var i = 0; i < phonemeStrings.length; i++) {
        var phonemeString = phonemeStrings[i];
        var [word, ...phonemes] = phonemeString.split(" ");
        phonemes = phonemes.filter( phoneme => phoneme != "" );

        phonemeDict.set(word, phonemes);
    }
    console.log(phonemeDict);
    return phonemeDict;
}

function compilePhonemeDictCode(phonemeDict) {
    var code = "let phonemeDict = {";
    let entries = Array.from(phonemeDict.entries());
    for (var i = 0; i < entries.length; i++) {
        let word = entries[i][0];
        let phonemes = entries[i][1];
        code += '"' + word +'": [';
        for (var j = 0; j < phonemes.length; j++) {
            let phoneme = phonemes[j];
            code += '"' + phoneme + '"';
            if (j != phonemes.length - 1) {
                code += ", ";
            }
        }
        code += "]";
        if (i != entries.length - 1) {
            code += ", ";
        }
    };
    code += "};";
    return code;
}

function buildRhymeEndDict(dictString) {
    var phonemeStrings = dictString.split("\n");
    var rhymeDict = new Map();
    for (var i = 0; i < phonemeStrings.length; i++) {
        var phonemeString = phonemeStrings[i];
        var [word, ...phonemes] = phonemeString.split(" ");
        if (word == "") {
            break;
        }

        phonemes = phonemes.filter( phoneme => phoneme != "" );
        var rhymeEnding = findRhymeEnding(phonemes);

        if (rhymeDict.has(rhymeEnding)) {
            var set = rhymeDict.get(rhymeEnding);
            set.add({ "word": word, "phonemes": phonemes });
        } else {
            rhymeDict.set(rhymeEnding, new Set([{ "word": word, "phonemes": phonemes }]));
        }
    }
    console.log(rhymeDict);
    return rhymeDict;
}

var vowels = ["AA", "AE", "AH", "AO", "AW", "AY", "EH", "ER", "EY", "IH", "IY", "OW", "OY", "UH", "UW", "W", "Y"];

/* Returns the last vowel sound and the phonemes after that sound for a given list of phonemes */
function findRhymeEnding(phonemesArg) {
    let phonemes = new Array(...phonemesArg)
    return phonemes.slice(0, phonemes.reverse().findIndex(
        phoneme => vowels.indexOf(splitStressIndicator(phoneme)[0]) != -1 
    ) + 1).reverse().reduce( (prev, curr) => prev + curr );
}

/* Returns the phoneme separated with the stress indicator number */
function splitStressIndicator(phoneme) {
    for (var i = 1; i < phoneme.length; i++) {
        if (!Number.isNaN(phoneme.charAt(i))) {
            return [phoneme.slice(0, i + 1), phoneme.slice(i + 1)];
        }
    }
    return [phoneme, ""];
}

/* Returns the number of matching phonemes in a rhyming pair and a list of those phonemes */
function matchingEndings(phonemesArg1, phonemesArg2) {
    let phonemes1 = new Array(...phonemesArg1).reverse();
    let phonemes2 = new Array(...phonemesArg2).reverse();

    for (var i = 0; i < Math.min(phonemes1.length, phonemes2.length); i++) {
        if (phonemes1[i] != phonemes2[i]) {
            return [i, phonemes1.splice(0, i).reverse()];
        }
    }

    if (phonemes1.length < phonemes2.length) {
        return [phonemes1.length, phonemes1.reverse()];
    } else {
        return [phonemes2.length, phonemes2.reverse()];
    }
}

/*
let phonemeDict = buildPhonemeDict(dict);
let rhymeDict = buildRhymeEndDict(dict);
*/

// Sample test code

/*
var input = "solution";

let inputPhonemes = phonemeDict.get(input.toUpperCase());

inputEnding = findRhymeEnding(inputPhonemes);
if (rhymeDict.has(inputEnding)) {
    rhymeList = [];
    var set = rhymeDict.get(inputEnding);
    for (let item of set) {
        let matches = matchingEndings(inputPhonemes, item["phonemes"]);
        console.log("Matches: " + matches[0] + " : " + matches[1]);
        rhymeList.push(matches);
    }
    rhymeList.sort( (match1, match2) => match1[0] < match2[0] );
}
*/
