class Trie(object):
    
    children = [None] * 26

    def __init__(self):
        pass

    def insert(self, word):
        if not word:
            return
        word = word.lower()
        char = word[0]
        index = ord(char) - 97
        if self.children[index] is None:
            new_trie = Trie()
            new_trie.insert(word[1:])
            self.children[index] = new_trie 

    def contains(self, word):
        if not word:
            return True
        word = word.lower()
        char = word[0]
        index = ord(char) - 97
        if self.children[index] is None:
            return False
        else:
            return self.children[index].contains(word[1:])
    
    def __repr__(self):
        string = "["
        for i in range(26):
            child = self.children[i]
            string += chr(i + 97) + " : "
            print child is None
            if child is None:
                string += "None"
            else:
                string += str(child)
            string += ", "

        return string

trie = Trie()
trie.insert("abc")
print trie
print trie.contains("abc")
print trie.contains("ab")
print trie.contains("a")
print trie.contains("b")
print trie.contains("b")
