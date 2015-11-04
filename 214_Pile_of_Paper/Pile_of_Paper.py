class Paper(object):
    def __init__(self, paper_line):
        self.colour = paper_line[0]
        self.x = paper_line[1]
        self.y = paper_line[2]
        self.width = paper_line[3]
        self.height = paper_line[4]

    def compare_coverage(self, old_paper):
        if self.x > old_paper.x and self.y > old_paper.y 
                and self.x + self.width < old_paper.x + old_paper.width
                and self.y + self.height < old_paper.y + old_paper.height:
            return True
        else:
            return 
            

    def __repr__(self):
        return "Paper: [colour: {0}, x: {1}, y: {2}, width: {3}, height: {4}]".format(
            self.colour, self.x, self.y, self.width, self.height)


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
    return grid_width * grid_height, stack

unfilled_area, paper_stack = read_from_file("input1.txt")

print unfilled_area
#while unfilled_area:
old_paper = paper_stack.pop()
new_paper = paper_stack.pop()
fully_covered, filled_area = new_paper.compare_coverage(old_paper)
