from sets import Set

def get_number_of_common_letters(word1, word2):
    """Return the number of common letters in the same position for two words.
    """
    number_of_common_letters = 0
    for i in range(len(word1)):
        if word1[i] == word2[i]:
            number_of_common_letters += 1
    return number_of_common_letters

def read_words_from_file(filename):
    """Read the answer, followed by the number of words, followed by the 
    candidate words for the game. Return the answer and list of words.
    """
    word_list = []
    with open(filename) as f:
        answer = f.readline().strip().upper()
        count = int(f.readline())
        for i in range(count):
            line = f.readline()
            word_list.append(line.strip().upper())
    return answer, word_list

def play_fallout_hacking_game(filename):
    """Attempt to optimally play the Fallout hacking game on a game given a
    filename. The algorithm creates a dictionary which maps a number of 
    correct matches to another dict which contains the matches for each word
    (with number of common letters being the first key). The number of
    different common letter combinations are also stored. For example, a word
    may be stored in the 0 common letters group, 1 common letters group and 2
    common letters group.

    The algorithm guesses words by guessing the word in our candidate word set
    that is contained within the most common letter groups. That way, when the
    game returns the number of common letters in our guess and the answer, we
    can more greatly reduce our candidate set. The possible words given this
    new information comes from our dictionary which maps common letter 
    occurences and word pairs. We then perform an intersection between our
    previous set of candidate words and the new set, given this new information.

    This process is repeated 4 times, or until we guess the correct word.
    """
    answer, word_list = read_words_from_file(filename)
    
    # Dictionary which maps common letter numbers to dictionaries of word pairs.
    # Example:
    # 0 : { "FLOGGING" : Set("MIGRAINE"), ...
    # 1 : { "FLOGGING" : Set("SCORPION", ...), ...
    # 1 : { "FLOGGING" : Set("VAULTING"), ...
    # 
    # This dictionary is used to retrieve sets of candidate words given a number
    # of correct letters and a guess.
    common_letters_dict = {}

    # Dictionary which stores the number of different common letter count dicts that
    # a word is involved in. For example, if the word "FLOGGING" is in the dict with
    # no matching letters, the dict with one matching letter and the dict with two
    # matching letters, "FLOGGING" would map to 3. 
    # This indicates that guessing this word could result in 3 possibilities: 0/8 
    # correct, 1/8 correct or 2/8 correct.
    different_common_letters_count = {word: 0 for word in word_list}

    # Build the common letter occurrences to word pair dictionaries dictionary
    for i in range(len(word_list)):
        for j in range(i + 1, len(word_list)):
            num_common_letters = get_number_of_common_letters(word_list[i], word_list[j])
            if num_common_letters not in common_letters_dict:
                new_pairs_dict = {word_list[i]: Set([word_list[j]]), word_list[j]: Set([word_list[i]])}
                common_letters_dict[num_common_letters] = new_pairs_dict
                different_common_letters_count[word_list[i]] += 1
                different_common_letters_count[word_list[j]] += 1
            else:
                if word_list[i] not in common_letters_dict[num_common_letters]:
                    common_letters_dict[num_common_letters][word_list[i]] = Set([word_list[j]])
                    different_common_letters_count[word_list[i]] += 1
                else:
                    common_letters_dict[num_common_letters][word_list[i]].add(word_list[j])

                if word_list[j] not in common_letters_dict[num_common_letters]:
                    common_letters_dict[num_common_letters][word_list[j]] = Set([word_list[i]])
                    different_common_letters_count[word_list[j]] += 1
                else:
                    common_letters_dict[num_common_letters][word_list[j]].add(word_list[i])
                
    # Sort our words by the number of distinct occurences in different common
    # letter number sets.
    sorted_different_letters_count = sorted(different_common_letters_count.items(), key=lambda x: x[1], reverse=True)
    possible_answer_set = Set(word_list)
    win = False

    for i in range(num_guesses):
        guess = sorted_different_letters_count[0][0]
        print "Guess ({0} left)? {1}".format(num_guesses - i, guess)
        num_correct_letters = get_number_of_common_letters(guess, answer)

        print "{0}/{1} correct".format(num_correct_letters, len(answer))
        if num_correct_letters == len(answer):
            win = True
            break

        # As we guess words, the set of possible words decreases. The previous 
        # answer set is intersected with the set given our guess and the number
        # of common letters between our guess and the answer.
        possible_answer_set = possible_answer_set & common_letters_dict[num_correct_letters][guess]

        # Continue to sort our possible words by the number of distinct 
        # occurences.
        sorted_different_letters_count = sorted(filter(lambda x: x[0] in possible_answer_set, 
                                                different_common_letters_count.items()),
                                                key=lambda x: x[1], reverse=True)
    if win:
        print "You win!"
    else:
        print "You lose!"
    print

num_guesses = 4

play_fallout_hacking_game("input1.txt")
play_fallout_hacking_game("input2.txt")
play_fallout_hacking_game("input3.txt")
play_fallout_hacking_game("input4.txt")
play_fallout_hacking_game("input5.txt")

play_fallout_hacking_game("input6.txt")
play_fallout_hacking_game("input7.txt")
play_fallout_hacking_game("input8.txt")
play_fallout_hacking_game("input9.txt")
play_fallout_hacking_game("input10.txt")
play_fallout_hacking_game("input11.txt")
play_fallout_hacking_game("input12.txt")
play_fallout_hacking_game("input13.txt")
