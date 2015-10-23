from sets import Set

def set_game_solver(filename):
    """Finds all valid sets according the game "Set". For each card, the
    function iterates through each other card and finds the matching card
    required to form a valid set. If it is found, we add the result to our
    set of result tuples."""
    tuples_set = create_tuples_from_file(filename)
    results_sets = Set()

    for tuple1 in tuples_set:
        for tuple2 in tuples_set:
            if tuple1 is not tuple2:
                matching_tuple = get_matching_tuple(tuple1, tuple2)
                if matching_tuple in tuples_set:
                     results_sets.add(Set([tuple1, tuple2, matching_tuple]))

    print_results(results_sets)

def create_tuples_from_file(filename):
    """Creates a set of tuples of values, representing all our cards."""
    tuples_set = Set()
    with open(filename) as f:
        line = f.readline().strip()
        while len(line) > 0:
            tuples_set.add(tuple(line))
            line = f.readline().strip()
    return tuples_set

def get_matching_value(value1, value2, character):
    """Given two values, we find the required value that would be needed to
    form a valid third value in the set. If the two values are the same, the
    third value must also be identical. If the values differ, we find the third
    value by finding the set difference."""
    if value1 == value2:
        return value1
    elif character == 0:
        return (Set(["D", "O", "S"]) - Set([value1, value2])).pop()
    elif character == 1:
        return (Set(["R", "P", "G"]) - Set([value1, value2])).pop()
    elif character == 2:
        return (Set(["1", "2", "3"]) - Set([value1, value2])).pop()
    else:
        return (Set(["O", "F", "H"]) - Set([value1, value2])).pop()

def get_matching_tuple(tuple1, tuple2):
    """Return the required third card (represented as a tuple) given two cards
    (as tuples)."""
    return (get_matching_value(tuple1[0], tuple2[0], 0), get_matching_value(tuple1[1], tuple2[1], 1), get_matching_value(tuple1[2], tuple2[2], 2), get_matching_value(tuple1[3], tuple2[3], 3))

def print_results(results_sets):
    """ Prints the result sets in the form of "1234 1234 1234"."""
    for result_set in results_sets:
        result_string = ""
        for result in result_set:
            for tuple_value in result:
                result_string += tuple_value
            result_string += " "
        print result_string
    print

set_game_solver("input.txt")
set_game_solver("challenge_input.txt")
