import math
import Queue

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
        if (self.cut_direction == 0):
            if (node.x < self.node.x):
                if (self.left is not None):
                    self.left.insert(node)
                else:
                    self.left = KDTree(node, cut_direction=next_cut_direction)
            else:
                if (self.right is not None):
                    self.right.insert(node)
                else:
                    self.right = KDTree(node, cut_direction=next_cut_direction)
        else:
            if (node.y < self.node.y):
                if (self.left is not None):
                    self.left.insert(node)
                else:
                    self.left = KDTree(node, cut_direction=next_cut_direction)
            else:
                if (self.right is not None):
                    self.right.insert(node)
                else:
                    self.right = KDTree(node)

    def find_and_delete_min(self, min_direction, previous=None, left=True):
        next_cut_direction = (self.cut_direction + 1) % 2
        if min_direction == self.cut_direction:
            if self.left is None:
                if previous is None:
                    return self, True
                else:
                    if left:
                        print "previous left: ", previous, left
                        print
                        previous.left = None
                        return self, False
                    else:
                        print "previous right: ", previous, left
                        print
                        previous.right = None
                        return self, False

            return self.left.find_and_delete_min(min_direction, self, left=True)
        else:
            if self.left is None and self.right is None:
                if left:
                    previous.left = None
                    return self, False
                else:
                    previous.right = None
                    return self, False

            if self.left is None:
                return self.right.find_and_delete_min(min_direction, self, left=False)
            if self.right is None:
                print "HERE"
                return self.left.find_and_delete_min(min_direction, self, left=True)

            left_min = self.left.find_min(min_direction)
            right_min = self.right.find_min(min_direction)
            if min_direction == 0:
                if left_min.node.x < right_min.node.x:
                    if left:
                        previous.left = None
                        return left_min, False
                    else:
                        previous.right = None
                        return left_min, False
                else:
                    if left:
                        previous.left = None
                        return right_min, False
                    else:
                        previous.right = None
                        return right_min, False
            else:
                if left_min.node.y < right_min.node.y:
                    if left:
                        previous.left = None
                        return left_min, false
                    else:
                        previous.right = None
                        return left_min, false
                else:
                    if left:
                        previous.left = None
                        return right_min, false
                    else:
                        previous.right = None
                        return right_min, false

    def find_min(self, min_direction):
        #next_cut_direction = (self.cut_direction + 1) % 2
        if min_direction == self.cut_direction:
            if self.left is None:
                return self
            return self.left.find_min(min_direction)
        else:
            if self.left is None and self.right is None:
                return self
            if self.left is None:
                right_min = self.right.find_min(min_direction)
                if min_direction == 0:
                    if self.node.x < right_min.node.x:
                        return self
                    else:
                        return right_min
                else:
                    if self.node.y < right_min.node.y:
                        return self
                    else:
                        return right_min
            if self.right is None:
                left_min = self.left.find_min(min_direction)
                if min_direction == 0:
                    if self.node.x < left_min.node.x:
                        return self
                    else:
                        return left_min
                else:
                    if self.node.y < left_min.node.y:
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


    def find_nearest_neighbour(self, node, current_nearest=None):
        if current_nearest is None:
            current_nearest = self.node
        next_cut_direction = (self.cut_direction + 1) % 2
        current_nearest_distance = abs(current_nearest.x - node.x) ** 2 + abs(current_nearest.y - node.y) ** 2
        if (self.cut_direction == 0):
            if (node.x < self.node.x):
                if (self.left is not None):
                    left_nearest_distance = abs(self.left.node.x - node.x) ** 2 + abs(self.left.node.y - node.y) ** 2
                    if (left_nearest_distance < current_nearest_distance):
                        return self.left.find_nearest_neighbour(node, self.left.node)
                    else:
                        return self.left.find_nearest_neighbour(node, current_nearest)
                else:
                    return current_nearest, math.sqrt(current_nearest_distance)
            else:
                if (self.right is not None):
                    left_nearest_distance = abs(self.right.node.x - node.x) ** 2 + abs(self.right.node.y - node.y) ** 2
                    if (left_nearest_distance < current_nearest_distance):
                        return self.right.find_nearest_neighbour(node, self.right.node)
                    else:
                        return self.right.find_nearest_neighbour(node, current_nearest)
                else:
                    return current_nearest, math.sqrt(current_nearest_distance)
        else:
            if (node.y < self.node.y):
                if (self.left is not None):
                    left_nearest_distance = abs(self.left.node.x - node.x) ** 2 + abs(self.left.node.y - node.y) ** 2
                    if (left_nearest_distance < current_nearest_distance):
                        return self.left.find_nearest_neighbour(node, self.left.node)
                    else:
                        return self.left.find_nearest_neighbour(node, current_nearest)
                else:
                    return current_nearest, math.sqrt(current_nearest_distance)
            else:
                if (self.right is not None):
                    left_nearest_distance = abs(self.right.node.x - node.x) ** 2 + abs(self.right.node.y - node.y) ** 2
                    if (left_nearest_distance < current_nearest_distance):
                        return self.right.find_nearest_neighbour(node, self.right.node)
                    else:
                        return self.right.find_nearest_neighbour(node, current_nearest)
                else:
                    return current_nearest, math.sqrt(current_nearest_distance)


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
    #next_cut_direction = (kdTree.cut_direction + 1) % 2
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

def print_tree(kdTree):
    all_None = False 
    frontier = []
    frontier.append(kdTree)

    while frontier:
        level_str = ""
        new_frontier = []
        for tree_node in frontier:
            if tree_node is not None:
                level_str += str(tree_node.node.x) + ", " + str(tree_node.node.y) +  " | "
                new_frontier.append(tree_node.left)
                new_frontier.append(tree_node.right)
                cut_direction = tree_node.cut_direction
            else:
                level_str += "empty |"
        level_str += "-- cut: " + str(cut_direction)
        frontier = new_frontier
        print level_str
        print

with open("input2.txt") as f:
    num_treats = int(f.readline())
    treats = []

    for i in range(num_treats):
        x, y = tuple(map(float, f.readline().split()))
        print x, y
        treats.append((x,y))

print treats

# Build 2 dimensional k-d tree
iterTreats = iter(treats)
kdTree = KDTree(KDNode(next(iterTreats)))

for treat in iterTreats:
    kdTree.insert(KDNode(treat))

print "START:"
print
print kdTree
print

print_tree(kdTree)
print

#print "nearest neighbour: ", kdTree.find_nearest_neighbour(KDNode((0.5, 0.5)))
#print
#print "min x: ", kdTree.find_min(0)
#print
#print "min y: ", kdTree.find_min(1)
#print

#print "find min1:", kdTree.find_min(0)
#print
#print "delete1:", delete(KDNode((0.1, 0.1)), kdTree)
#print
#print

#print "find min2:", kdTree.find_min(0)
#print
#print "delete2:", delete(KDNode((0.4, 0.1)), kdTree)
#print

#print kdTree
#print

#print "find and delete min1: ", kdTree.find_and_delete_min(0)[0]
#print
#print "kd Tree: ", kdTree
#print
#print "find and delete min2: ", kdTree.find_and_delete_min(0)[0]
#print

last_node = KDNode((0.5, 0.5))
total_distance = 0

while kdTree is not None:
#for i in range(5):
    new_node, new_distance = kdTree.find_nearest_neighbour(last_node)
    print "find:", new_node
    print_tree(kdTree)
    kdTree = delete(new_node, kdTree)
    last_node = new_node
    total_distance += new_distance
    print "so far:", new_node, total_distance, kdTree

print total_distance

