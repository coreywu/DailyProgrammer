import math

class KDTree(object):
    """A class which holds information for a single node in a k-d tree. The 
       node contains the coordinates of the current node (x and y position) and
       references to the left and right subtrees."""
    
    def __init__(self, node, left=None, right=None, cut_direction=0):
        self.node = node
        self.left = left
        self.right = right
        self.cut_direction = cut_direction

    def insert(self, node):
        next_cut_direction = (self.cut_direction + 1) % 2

        if (self.cut_direction == 0 and node.x < self.node.x) or (self.cut_direction == 1 and node.y < self.node.y):
            if (self.left is not None):
                self.left.insert(node)
            else:
                self.left = KDTree(node, cut_direction=next_cut_direction)
        else:
            if (self.right is not None):
                self.right.insert(node)
            else:
                self.right = KDTree(node, cut_direction=next_cut_direction)

    def find_min(self, min_direction):
        if min_direction == self.cut_direction:
            if self.left is None:
                return self
            return self.left.find_min(min_direction)
        else:
            if self.left is None and self.right is None:
                return self
            if self.left is None:
                right_min = self.right.find_min(min_direction)
                if (min_direction == 0 and self.node.x < right_min.node.x) or (min_direction == 1 and self.node.y < right_min.node.y):
                    return self
                else:
                    return right_min

            if self.right is None:
                left_min = self.left.find_min(min_direction)
                if (min_direction == 0 and self.node.x < left_min.node.x) or (min_direction == 1 and self.node.y < left_min.node.y):
                    return self
                else:
                    return left_min

            left_min = self.left.find_min(min_direction)
            right_min = self.right.find_min(min_direction)

            if min_direction == 0:
                if left_min.node.x < right_min.node.x:
                    if self.node.x < left_min.node.x:
                        return self
                    else:
                        return left_min
                else:
                    if self.node.x < right_min.node.x:
                        return self
                    else:
                        return right_min
            else:
                if left_min.node.y < right_min.node.y:
                    if self.node.y < left_min.node.y:
                        return self
                    else:
                        return left_min 
                else:
                    if self.node.y < right_min.node.y:
                        return self
                    else:
                        return right_min

    def __str__(self):
        return "KDTree:[node=" + str(self.node) + ", left=" + str(self.left) + ", right=" + str(self.right) + \
               ", cut_direction=" + str(self.cut_direction) + "]"

class KDNode(object):
    def __init__(self, coordinates):
        self.x = coordinates[0]
        self.y = coordinates[1]

    def __str__(self):
        return "KDNode:[x=" + str(self.x) + ", y=" + str(self.y) + "]"

def delete(node, kdTree, cut_direction=0):
    next_cut_direction = (cut_direction + 1) % 2

    if kdTree.node.x == node.x and kdTree.node.y == node.y:
        if kdTree.right is not None:
            kdTree.node = kdTree.right.find_min(cut_direction).node
            kdTree.right = delete(kdTree.node, kdTree.right, next_cut_direction)
        elif kdTree.left is not None:
            kdTree.node = kdTree.left.find_min(cut_direction).node
            kdTree.right = delete(kdTree.node, kdTree.left, next_cut_direction)
            kdTree.left = None
        else:
            kdTree = None
    elif kdTree.cut_direction == 0:
        if node.x < kdTree.node.x:
            kdTree.left = delete(node, kdTree.left, next_cut_direction)
        else:
            kdTree.right = delete(node, kdTree.right, next_cut_direction)
    else:
        if node.y < kdTree.node.y:
            kdTree.left = delete(node, kdTree.left, next_cut_direction)
        else:
            kdTree.right = delete(node, kdTree.right, next_cut_direction)

    return kdTree

LEFT = False 
RIGHT = True 

current_nearest_node = None
current_nearest_distance = float("inf")

def find_nearest_neighbour(node, kdTree):
    """Find the nearest neighbour node to a coordinate in a k-d tree. We keep
    track of the current nearest node while we recursively check sub-trees for
    better nodes. We always check the sub-tree in the direction of the desired
    coordinate (if we are looking for (0.5, 0.5), we would always check the 
    sub-tree left of (0.6, 0.4) if the cut direction is x). 
    
    The other sub-tree is checked if the node is closer than the current best 
    distance on that cutting axis. For example, if the current best distance is
    0.2 and we are looking for (0.5, 0.5), we will check the right sub-tree of 
    (0.6, 0.4) because the difference of 0.6 and 0.5 is less than the best
    known distance. This is done because there could be a point at (0.6, 0.5)
    in that sub-tree, which is better than the current nearest distance and is
    a potential candidate for nearest neighbour.
    """

    global current_nearest_node
    global current_nearest_distance

    current_nearest_node = None
    current_nearest_distance = float("inf")

    find_nearest_neighbour_recursive(node, kdTree)
    return current_nearest_node, current_nearest_distance

def find_nearest_neighbour_recursive(node, kdTree):
    global current_nearest_node
    global current_nearest_distance

    if current_nearest_node is None:
        current_nearest_node = kdTree.node
        current_nearest_distance = math.sqrt(abs(kdTree.node.x - node.x) ** 2 + abs(kdTree.node.y - node.y) ** 2)

    distance = math.sqrt(abs(kdTree.node.x - node.x) ** 2 + abs(kdTree.node.y - node.y) ** 2)
    if distance < current_nearest_distance:
        current_nearest_node = kdTree.node
        current_nearest_distance = distance
            #return

    search_first = None

    if ((kdTree.cut_direction == 0 and kdTree.node.x < current_nearest_node.x ) or (kdTree.cut_direction == 1 and kdTree.node.y < current_nearest_node.y)) and kdTree.left is not None:
        find_nearest_neighbour_recursive(node, kdTree.left)
        search_first = LEFT
    elif kdTree.right is not None: 
        find_nearest_neighbour_recursive(node, kdTree.right)
        search_first = RIGHT 

    if kdTree.cut_direction == 0:
        if abs(current_nearest_node.x - node.x) < current_nearest_distance:
            if search_first == LEFT and kdTree.right is not None:
                find_nearest_neighbour_recursive(node, kdTree.right)
            elif kdTree.left is not None:
                find_nearest_neighbour_recursive(node, kdTree.left)
    else:
        if abs(current_nearest_node.y - node.y) < current_nearest_distance:
            if search_first == LEFT and kdTree.right is not None:
                find_nearest_neighbour_recursive(node, kdTree.right)
            elif kdTree.left is not None:
                find_nearest_neighbour_recursive(node, kdTree.left)

def print_tree(kdTree):
    all_None = False 
    frontier = []
    frontier.append(kdTree)

    while frontier:
        level_str = ""
        cut_direction = ""
        new_frontier = []

        if frontier[0] is not None:
            level_str += str(frontier[0].node.x) + ", " + str(frontier[0].node.y)
            new_frontier.append(frontier[0].left)
            new_frontier.append(frontier[0].right)
            cut_direction = frontier[0].cut_direction
        else:
            level_str += "empty"

        for tree_node in frontier[1:]:
            if tree_node is not None:
                level_str +=  " | " + str(tree_node.node.x) + ", " + str(tree_node.node.y)
                new_frontier.append(tree_node.left)
                new_frontier.append(tree_node.right)
                cut_direction = tree_node.cut_direction
            else:
                level_str += "| empty"
        level_str += " -- cut: " + str(cut_direction)
        frontier = new_frontier
        print level_str
        print

def find_nearest(node, node_list):
    current_node = None
    current_distance = float("inf")
    for candidate_node in node_list:
        distance = math.sqrt(abs(node.x - candidate_node.x) ** 2 + abs(node.y - candidate_node.y) ** 2)
        if distance < current_distance:
            current_node = candidate_node
            current_distance = distance

    return current_node

with open("input2.txt") as f:
    num_treats = int(f.readline())
    treats = []

    for i in range(num_treats):
        x, y = tuple(map(float, f.readline().split()))
        treats.append((x,y))

# Build 2 dimensional k-d tree
iterTreats = iter(treats)
kdTree = KDTree(KDNode(next(iterTreats)))
node_list = [kdTree.node]

for treat in iterTreats:
    kdTree.insert(KDNode(treat))
    node_list.append(KDNode(treat))

last_node = KDNode((0.5, 0.5))
total_distance = 0

while kdTree is not None:
    new_node, new_distance = find_nearest_neighbour(last_node, kdTree)
    kdTree = delete(new_node, kdTree)
    last_node = new_node
    total_distance += new_distance

print total_distance

