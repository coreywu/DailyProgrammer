from sets import Set

def get_number_of_common_letters(word1, word2):
    number_of_common_letters = 0
    for i in range(len(word1)):
        if word1[i] == word2[i]:
            number_of_common_letters += 1
    return number_of_common_letters

def read_words_from_file(filename):
    word_list = []
    with open(filename) as f:
        line = f.readline()
        while len(line) > 0:
            word_list.append(line.strip().upper())
            line = f.readline()
    return word_list


word_list = read_words_from_file("input1.txt")

common_letters_dict = {}

for i in range(len(word_list)):
    for j in range(i + 1, len(word_list)):
        num_common_letters = get_number_of_common_letters(word_list[i], word_list[j])
        if num_common_letters not in common_letters_dict:
            new_pairs_dict = {word_list[i] : Set([word_list[j]]), word_list[j] : Set([word_list[i]])}
            common_letters_dict[num_common_letters] = new_pairs_dict
        else:
            if word_list[i] not in common_letters_dict[num_common_letters]:
                common_letters_dict[num_common_letters][word_list[i]] = Set([word_list[j]])
            else:
                common_letters_dict[num_common_letters][word_list[i]].add(word_list[j])

            if word_list[j] not in common_letters_dict[num_common_letters]:
                common_letters_dict[num_common_letters][word_list[j]] = Set([word_list[i]])
            else:
                common_letters_dict[num_common_letters][word_list[j]].add(word_list[i])
            
print common_letters_dict
    




