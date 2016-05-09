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

function findRhymes() {
    var input = $("#word").val();
    let inputPhonemes = phonemeDict[input.toUpperCase()];

    inputEnding = findRhymeEnding(inputPhonemes);
    if (inputEnding in rhymeEndDict) {
        rhymeList = [];
        var words = rhymeEndDict[inputEnding];
        for (var i = 0; i < words.length; i++) {
            word = words[i];
            let matches = matchingEndings(inputPhonemes, phonemeDict[word]);
            matches.push(word);
            matches.push(phonemeDict[word]);
            rhymeList.push(matches);
        }
        rhymeList = rhymeList.sort( (match1, match2) => match2[0] - match1[0] );
        showMatches(rhymeList);
    }
}

function showMatches(rhymeList) {
    clearTableRows();
    var table = document.getElementById("rhymeTable");

    for (var i = 0; i < rhymeList.length; i++) {
        var newRow = table.rows[1].cloneNode(true);
        var len = table.rows.length;
        newRow.cells[0].innerHTML = rhymeList[i][0];
        newRow.cells[1].innerHTML = rhymeList[i][2];
        newRow.cells[2].innerHTML = rhymeList[i][3];
        table.appendChild(newRow);
    }
}

function clearTableRows() {
    var table = document.getElementById("rhymeTable");
    var tableRows = table.getElementsByTagName('tr');
    var rowCount = tableRows.length;

    for (var i = rowCount - 1; i > 1; i--) {
        table.removeChild(tableRows[i]);
    }
}

// Sample test code
$(document).ready(function() {
    var input = "solution";

    let inputPhonemes = phonemeDict[input.toUpperCase()];

    inputEnding = findRhymeEnding(inputPhonemes);
    if (inputEnding in rhymeEndDict) {
        rhymeList = [];
        var words = rhymeEndDict[inputEnding];
        for (var i = 0; i < words.length; i++) {
            word = words[i];
            let matches = matchingEndings(inputPhonemes, phonemeDict[word]);
            matches.push(word);
            matches.push(phonemeDict[word]);
            rhymeList.push(matches);
        }
        rhymeList = rhymeList.sort( (match1, match2) => match2[0] - match1[0] );
        showMatches(rhymeList);
    }
});
