<?hh 

include 'TrainingForSummitingEverestStrict.php';

function main(): void {
    $input = "0 8 4 12 2 10 6 14 1 9 5 13 3 11 7 15";
    echo trainingForSummitingEverest($input) . "\n";

    $challenge_input1 = "1 2 2 5 9 5 4 4 1 6";
    echo trainingForSummitingEverest($challenge_input1) . "\n";

    $challenge_input2 = "4 9 4 9 9 8 2 9 0 1";
    echo trainingForSummitingEverest($challenge_input2) . "\n";

    $challenge_input3 = "0 5 4 6 9 1 7 6 7 8";
    echo trainingForSummitingEverest($challenge_input3) . "\n";

    $challenge_input4 = "1 2 20 13 6 15 16 0 7 9 4 0 4 6 7 8 10 18 14 10 17 15 
        19 0 4 2 12 6 10 5 12 2 1 7 12 12 10 8 9 2 20 19 20 17 5 19 0 11 5 20";
    echo trainingForSummitingEverest($challenge_input4) . "\n";
}

main();
