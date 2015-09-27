import string
import Queue

MALE = False
FEMALE = True

def gale_shapely(filename):
    male_preferences, female_preferences = get_preferences_from_file(filename)

    available_males_set = set(male_preferences.keys())
    available_females_set = set(female_preferences.keys())

    marriages = {}

    while available_males_set:
        suitors = {}
        for female in available_females_set:
            suitors[female] = set()

        males_to_remove = set()
        for male in available_males_set:
            proposed = False
            while not proposed:
                potential_female = male_preferences[male][-1]
                if potential_female in available_females_set:
                    proposed = True
                else:
                    male_preferences[male].pop()

            if female_preferences[potential_female][-1] == male:
                marriages[male] = potential_female
                males_to_remove.add(male)
                available_females_set.remove(potential_female)

                for female_preference in female_preferences:
                    female_preferences[female_preference].remove(male)

                for male_preference in male_preferences:
                    male_preferences[male_preference].remove(potential_female)

            else:
                suitors[potential_female].add(male)

        for female in suitors:
            if female not in available_females_set:
                break
            for male in female_preferences[female]:
                if male in suitors[female] and male not in males_to_remove:
                    marriages[male] = female
                    males_to_remove.add(male)
                    available_females_set.remove(female)

                    for female_preference in female_preferences:
                        female_preferences[female_preference].remove(male)

                    for male_preference in male_preferences:
                        male_preferences[male_preference].remove(female)

                    break

        available_males_set = available_males_set.difference(males_to_remove)

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

gale_shapely("input1.txt")
gale_shapely("input2.txt")

