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

    def get_first_error(self, word):
        """Returns the position of the first letter which is not contained
        within the Trie.
        """
        return self.__get_first_error_recursive(word, 0)

    def __get_first_error_recursive(self, word, n):
        if not word:
            return -1
        word = word.lower()
        char = word[0]
        index = ord(char) - 97
        if self.children[index] is None:
            return n
        else:
            return self.children[index].__get_first_error_recursive(word[1:], n + 1)

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


def format_first_error(word, error_index):
    return word[0:error_index + 1] + "<" + word[error_index + 1:]

input1 = "foobar"
input2 = "garbgae"

challenge_input1 = "accomodate"
challenge_input2 = "acknowlegement"
challenge_input3 = "arguemint"
challenge_input4 = "comitmment"
challenge_input5 = "deductabel"
challenge_input6 = "depindant"
challenge_input7 = "existanse"
challenge_input8 = "forworde"
challenge_input9 = "herrass"
challenge_input10 = "inadvartent"
challenge_input11 = "judgemant"
challenge_input12 = "ocurrance"
challenge_input13 = "parogative"
challenge_input14 = "suparseed"

trie = Trie()
trie.build_from_file("enable1.txt")

print format_first_error(input1, trie.get_first_error(input1))
print format_first_error(input2, trie.get_first_error(input2))

print format_first_error(challenge_input1, trie.get_first_error(challenge_input1))
print format_first_error(challenge_input2, trie.get_first_error(challenge_input2))
print format_first_error(challenge_input3, trie.get_first_error(challenge_input3))
print format_first_error(challenge_input4, trie.get_first_error(challenge_input4))
print format_first_error(challenge_input5, trie.get_first_error(challenge_input5))
print format_first_error(challenge_input6, trie.get_first_error(challenge_input6))
print format_first_error(challenge_input7, trie.get_first_error(challenge_input7))
print format_first_error(challenge_input8, trie.get_first_error(challenge_input8))
print format_first_error(challenge_input9, trie.get_first_error(challenge_input9))
print format_first_error(challenge_input10, trie.get_first_error(challenge_input10))
print format_first_error(challenge_input11, trie.get_first_error(challenge_input11))
print format_first_error(challenge_input12, trie.get_first_error(challenge_input12))
print format_first_error(challenge_input13, trie.get_first_error(challenge_input13))
print format_first_error(challenge_input14, trie.get_first_error(challenge_input14))
