def problem(word, offensive_word):
    """Determines if the offensive word can be generated from a word using
    Hangman rules. Checks every letter in the offensive word to determine if 
    the order and counts of letters is the same."""
    current_word = word
    for char in offensive_word:
        char_index = current_word.find(char)
        if char_index == -1 or word.count(char) != offensive_word.count(char):
            return False
        current_word = current_word[char_index + 1:]
    return True

print problem("synchronized", "snond")
print problem("misfunctioned", "snond")
print problem("mispronounced", "snond")
print problem("shotgunned", "snond")
print problem("snond", "snond")
