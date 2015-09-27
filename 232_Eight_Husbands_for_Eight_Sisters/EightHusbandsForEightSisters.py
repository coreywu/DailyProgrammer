MALE = False
FEMALE = True

def gale_shapley(filename):
    """Apply the Gale-Shapley algorithm to find a stable marriage for a list
    of male and female match preferences. The algorithm involves "rounds" of
    each unmarried male proposing to his most prefered female and each female 
    selecting her preference from her suitors."""

    male_preferences, female_preferences = get_preferences_from_file(filename)

    available_males_set = set(male_preferences.keys())
    available_females_set = set(female_preferences.keys())

    # marriages stores the current matches we have performed
    marriages = {}

    while available_males_set:
        # Keep a dict of sets of suitors which propose to each female
        suitors = {}
        for female in available_females_set:
            suitors[female] = set()

        for male in available_males_set:
            # For each male, propose to his most preferred female
            proposed = False
            while not proposed:
                potential_female = male_preferences[male][-1]
                if potential_female in available_females_set:
                    proposed = True
                else:
                    male_preferences[male].pop()

            suitors[potential_female].add(male)

        # Each female now selects her most preferred male from her suitors
        for female in suitors:
            for male in female_preferences[female]:
                if male in suitors[female]:
                    marriages[male] = female

                    # Once a match is made, remove each person from their 
                    # availability sets and preference lists
                    available_males_set.remove(male)
                    available_females_set.remove(female)

                    for female_preference in female_preferences:
                        female_preferences[female_preference].remove(male)

                    for male_preference in male_preferences:
                        male_preferences[male_preference].remove(female)

                    break

    print marriages


def get_preferences_from_file(filename):
    with open(filename) as f:
        male_preferences = {}
        female_preferences = {}

        line = " "
        while len(line) > 0:
            line = f.readline()

            if len(line) == 0:
                break

            splitLine = line.split(',')

            person = splitLine[0]

            sex = MALE if person.isupper() else FEMALE
            
            preferences = []
            for string in splitLine[1:]:
                preferences.append(string.strip())

            preferences.reverse()
            if sex is MALE:
                male_preferences[person] = preferences
            else:
                female_preferences[person] = preferences

    return male_preferences, female_preferences

gale_shapley("input1.txt")
gale_shapley("input2.txt")

