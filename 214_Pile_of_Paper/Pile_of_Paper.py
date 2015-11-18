class Paper(object):
    """Class that represents a single piece of paper with attributes for the 
    colour, x and y positions of top left corner, width and height.
    """
    def __init__(self, paper_line):
        self.colour = int(paper_line[0])
        self.x = int(paper_line[1])
        self.y = int(paper_line[2])
        self.width = int(paper_line[3])
        self.height = int(paper_line[4])

    def fully_covers(self, old_paper):
        """Returns whether this piece of paper fully covers the given piece wit
        the provided x, y, width and height attributes.
        """
        return (self.x <= old_paper.x and self.y <= old_paper.y and 
                self.x + self.width >= old_paper.x + old_paper.width and 
                self.y + self.height >= old_paper.y + old_paper.height)
            
    def delta(self, width, height):
        """Returns the "delta" of the piece of paper, which is a 2d array which
        represents the area, with 1s in the position where the piece of paper 
        occupies. This delta is used to compute differences in the overlapping
        areas of two pieces of paper.
        """
        delta = [[0 for i in range(width)] for j in range(height)]
        for y in range(self.y, self.y + self.height):
            for x in range(self.x, self.x + self.width):
                delta[y][x] = 1
        return delta

    def __repr__(self):
        return "Paper: [colour: {0}, x: {1}, y: {2}, width: {3}, height: {4}]".format(
            self.colour, self.x, self.y, self.width, self.height)


def compute_delta(delta1, delta2):
    """Returns the difference in delta of two pieces of paper. The difference
    is the first piece of paper's delta with 0s wherever the two overlap.
    """
    delta = list(delta1)
    for y in range(len(delta1)):
        for x in range(len(delta1[0])):
            if delta1[y][x] == 1:
                if delta2[y][x] == 1:
                    delta[y][x] = 0
    return delta

def is_empty(delta):
    """Returns whether the delta is entirely 0s, indicating no visible paper.
    """
    for y in range(len(delta)):
        for x in range(len(delta[0])):
            if delta[y][x] == 1:
                return False
    return True

def count_spaces(delta):
    """Returns the number of 1s in the delta.
    """
    spaces = 0
    for y in range(len(delta)):
        for x in range(len(delta[0])):
            if delta[y][x] == 1:
                spaces += 1
    return spaces

def read_from_file(filename):
    """Reads the attributes of a pile of paper and the area, generating a stack
    of papers which we will traverse backwards.
    """
    stack = []
    with open(filename) as f:
        width_height_line = f.readline().split(" ")
        grid_width = int(width_height_line[0])
        grid_height = int(width_height_line[1])
        paper_line = f.readline().strip()
        while paper_line != "":
            stack.append(Paper(paper_line.split(" ")))
            paper_line = f.readline().strip()
    return grid_width, grid_height, stack

def pile_of_paper(filename):
    """The algorithm essentially builds a stack of papers in order to traverse
    from the top most piece of paper down. Every piece of paper after the first
    is checked to determine if it is visible by calculating the delta of that
    piece with all other visibile papers. If it is, decrement `unfilled_spaces`
    by the area covered. This is continued until either all papers are
    accounted for, or all of the area is covered.
    """
    width, height, paper_stack = read_from_file(filename)
    unfilled_spaces = width * height

    visible_papers = []
    colour_counts = {}

    top_paper = paper_stack.pop()
    visible_papers.append(top_paper)
    colour_counts[top_paper.colour] = count_spaces(top_paper.delta(width, height))
    unfilled_spaces -= count_spaces(top_paper.delta(width, height))

    while unfilled_spaces and paper_stack:
        new_paper = paper_stack.pop()
        valid_new_paper = True
        delta = new_paper.delta(width, height)
        for old_paper in visible_papers:
            if not old_paper.fully_covers(new_paper):
                delta = compute_delta(delta, old_paper.delta(width, height))
            else:
                valid_new_paper = False
                break

            if is_empty(delta):
                valid_new_paper = False
                break

        if valid_new_paper:
            visible_papers.append(new_paper)
            if new_paper.colour not in colour_counts:
                colour_counts[new_paper.colour] = count_spaces(delta)
            else:
                colour_counts[new_paper.colour] += count_spaces(delta)

            unfilled_spaces -= count_spaces(delta)
            
    if 0 not in colour_counts:
        colour_counts[0] = unfilled_spaces
    else:
        colour_counts[0] += unfilled_spaces

    return colour_counts

def print_pile_of_paper(filename):
    colour_counts = pile_of_paper(filename) 
    for colour in sorted(colour_counts.keys()):
        print colour, colour_counts[colour]

    print

def read_output_from_file(filename):
    expected_colour_counts = {}
    with open(filename) as f:
        colour_count_line = f.readline().strip()
        while colour_count_line != "":
            colour_count = colour_count_line.split(" ")
            expected_colour_counts[int(colour_count[0])] = int(colour_count[1])
            colour_count_line = f.readline().strip()
    return expected_colour_counts

def check_pile_of_paper(input_filename, output_filename):
    expected_colour_counts = read_output_from_file(output_filename)
    colour_counts = pile_of_paper(input_filename) 
    for colour in colour_counts:
        if colour_counts[colour] != expected_colour_counts[colour]:
            return False
    return True

print_pile_of_paper("sample.in")
assert check_pile_of_paper("sample.in", "sample.out")

print_pile_of_paper("100rects100x100.in")
assert check_pile_of_paper("100rects100x100.in", "100rects100x100.out")

print_pile_of_paper("1Krects100x100.in")
print_pile_of_paper("10Krects100x100.in")

#print_pile_of_paper("100rects10Kx10K.in")
#print_pile_of_paper("100rects100Kx100K.in")
#assert check_pile_of_paper("10Krects100Kx100K.in", "10Krects100Kx100K.out")
