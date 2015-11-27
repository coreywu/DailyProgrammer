from sets import Set

class Trie(object):
    """The trie class represents a substring of a valid word by storing an 
    array of children Tries. Each Trie itself is a single letter except the 
    root Trie which points to all words.
    """
    def __init__(self):
        self.children = [None] * 26

    def insert(self, word):
        """Insert a word into the Trie recursively, creating new Tries 
        whenever necessary.
        """
        word = word.strip().lower()
        self.__insert(word)

    def __insert(self, word):
        if not word:
            return
        char = word[0]
        index = ord(char) - 97
        if self.children[index] is None:
            new_trie = Trie()
            new_trie.__insert(word[1:])
            self.children[index] = new_trie 
        else:
            self.children[index].__insert(word[1:])

    def contains(self, word):
        """Returns whether a word exists within a Trie.
        """
        word = word.strip().lower()
        return self.__contains(word)

    def __contains(self, word):
        if not word:
            return True
        char = word[0]
        index = ord(char) - 97
        if self.children[index] is None:
            return False
        else:
            return self.children[index].__contains(word[1:])

    def build_from_file(self, filename):
        """Build the Trie from words in a file.
        """
        with open(filename) as f:
            line = " "
            while len(line) > 0:
                line = f.readline()
                self.insert(line)
    
    def __repr__(self):
        string = "["
        for i in range(26):
            child = self.children[i]
            string += chr(i + 97) + " : "
            if child is None:
                string += "None"
            else:
                string += str(child)
            string += ", "

        string += "]"

        return string

def ternary_to_decimal(ternary):
    result = 0
    power = 0
    for digit in str(ternary)[::-1]:
        result += int(digit) * 3 ** power
        power += 1
    return result

def decimal_to_ternary(decimal):
    result = ""
    while decimal > 0:
        result += str(decimal % 3)
        decimal = decimal / 3
    return result[::-1]

def encode(ternary):
    return encode_recursive(list(reversed(list(str(ternary)))), 1)

def encode_recursive(ternary_list, current_value):
    if not ternary_list:
        return [current_value]
    if ternary_list[0] == '0':
        return encode_recursive(ternary_list[1:], current_value * 3)
    else:
        return encode_recursive(ternary_list[1:], current_value * 3 + int(ternary_list[0])) \
            + encode_recursive(ternary_list[1:], current_value * 3 - int(ternary_list[0]))

def decode(encoded):
    return decode_recursive(encoded, [])

def decode_recursive(encoded, current_list):
    if encoded <= 0:
        return
    elif encoded == 1:
        current_list = "".join([str(x) for x in current_list])
        return [current_list]
    elif encoded % 3 == 0:
        current_list.append(0)
        return decode_recursive(encoded / 3, current_list)
    elif encoded % 3 == 1:
        new_list1 = list(current_list)
        new_list1.append(1)
        new_list2 = list(current_list)
        new_list2.append(2)
        result1 = decode_recursive((encoded - 1) / 3, new_list1)
        result2 = decode_recursive((encoded + 2) / 3, new_list2)
        if result1 is None:
            return result2 if result2 is not None else None
        else:
            return result1 + result2 if result2 is not None else result1
    elif encoded % 3 == 2:
        new_list1 = list(current_list)
        new_list1.append(2)
        new_list2 = list(current_list)
        new_list2.append(1)
        result1 = decode_recursive((encoded - 2) / 3, new_list1)
        result2 = decode_recursive((encoded + 1) / 3, new_list2)
        if result1 is None:
            return result2 if result2 is not None else None
        else:
            return result1 + result2 if result2 is not None else result1

def decode_word(encoded_word):
    decoded_words = decode(encoded_word)
    valid_decodings = Set()
    for decoded_word in decoded_words:
        split_letters = split_decoded_word_recursive(str(decoded_word), "")
        if split_letters:
            valid_decodings.update(split_letters)
            #print split_letters
    return valid_decodings

def split_decoded_word_recursive(decoded, letters):
    global trie
    if len(decoded) == 0:
        return [letters]

    valid_splits = []

    if decoded[:4] in ternary_letters:
        char = chr(ternary_to_decimal(decoded[:4])).lower()
        letters += char
        if trie.contains(letters):
            split_letters = split_decoded_word_recursive(decoded[5:], letters)
            if split_letters:
                valid_splits.extend(split_letters)

    if decoded[:5] in ternary_letters:
        char = chr(ternary_to_decimal(decoded[:5])).lower()
        letters += char
        if trie.contains(letters):
            split_letters = split_decoded_word_recursive(decoded[5:], letters)
            if split_letters:
                valid_splits.extend(split_letters)

    return valid_splits if len(valid_splits) > 0 else None

# Create dict of ternary values to letters
ternary_letters = {}
for i in range(65, 91):
    ternary_letters[decimal_to_ternary(i)] = chr(i)

for i in range(97, 123):
    ternary_letters[decimal_to_ternary(i)] = chr(i)

trie = Trie()
trie.build_from_file("../util/enable1.txt")

print decode_word(1343814725227)
print decode_word(66364005622431677379166556)
print decode_word(1023141284209081472421723187973153755941662449)
