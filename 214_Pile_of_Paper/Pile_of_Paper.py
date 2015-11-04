class Paper(object):
    def __init__(self, paper_line):
        self.colour = int(paper_line[0])
        self.x = int(paper_line[1])
        self.y = int(paper_line[2])
        self.width = int(paper_line[3])
        self.height = int(paper_line[4])

    def fully_covers(self, old_paper):
        return (self.x > old_paper.x and self.y > old_paper.y and 
                self.x + self.width < old_paper.x + old_paper.width and 
                self.y + self.height < old_paper.y + old_paper.height)
            
    def delta(self, width, height):
        delta = [[0 for i in range(width)] for j in range(height)]
        for y in range(self.y, self.y + self.height):
            for x in range(self.x, self.x + self.width):
                delta[y][x] = 1
        return delta

    def __repr__(self):
        return "Paper: [colour: {0}, x: {1}, y: {2}, width: {3}, height: {4}]".format(
            self.colour, self.x, self.y, self.width, self.height)


def compute_delta(delta1, delta2):
    delta = list(delta1)
    for y in range(len(delta1)):
        for x in range(len(delta1[0])):
            if delta1[y][x] == 1:
                if delta2[y][x] == 1:
                    delta[y][x] = 0
    return delta

def add_delta_to_result(result, colour, delta):
    for y in range(len(result)):
        for x in range(len(result[0])):
            if delta[y][x] == 1:
                result[y][x] = colour

def is_empty(delta):
    for y in range(len(result)):
        if not all(i == 0 for i in result[y]):
            return False 
    return True

def read_from_file(filename):
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

width, height, paper_stack = read_from_file("100rects100x100.in")
unfilled_spaces = width * height
result = [[0 for i in range(width)] for j in range(height)]

print "empty result", is_empty(result)

visible_papers = []

top_paper = paper_stack.pop()
add_delta_to_result(result, top_paper.colour, top_paper.delta(width, height))

visible_papers.append(top_paper)

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
        add_delta_to_result(result, new_paper.colour, delta)
        visible_papers.append(new_paper)

for line in result:
    print line

