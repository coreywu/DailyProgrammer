$(document).ready(function() {
    $("#cmudict").load(function() {
        let cmudict = window.frames[0].document.body.innerHTML;

        let phonemeDict = buildPhonemeDict(cmudict);
        let phonemeDictCode = compilePhonemeDictCode(phonemeDict);
        console.log(phonemeDictCode);

        let rhymeDict = buildRhymeEndDict(cmudict);
        console.log(rhymeDict);
        let rhymeDictCode = compileRhymeEndDictCode(rhymeDict);
        console.log(rhymeDictCode);
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
    return phonemeDict;
}

function compilePhonemeDictCode(phonemeDict) {
    var code = "let phonemeDict = {";
    let entries = Array.from(phonemeDict.entries());
    for (var i = 0; i < entries.length; i++) {
        let word = entries[i][0];
        let phonemes = entries[i][1];
        code += '"' + word + '": [';
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
            set.add(word);
        } else {
            rhymeDict.set(rhymeEnding, new Set([word]));
        }
    }
    return rhymeDict;
}

function compileRhymeEndDictCode(rhymeDict) {
    var code = "let rhymeDict = {";
    let entries = Array.from(rhymeDict.entries());
    for (var i = 0; i < entries.length; i++) {
        let phoneme = entries[i][0];
        let words = entries[i][1];
        code += '"' + phoneme +'": [';
        //for (var j = 0; j < words.length; j++) {
        var j = 0;
        words.forEach( word => {
            code += '"' + word + '"';
            if (j != words.size - 1) {
                code += ", ";
            }
            j++;
        }
        );
        code += "]";
        if (i != entries.length - 1) {
            code += ", ";
        }
    };
    code += "};";
    return code;
}
