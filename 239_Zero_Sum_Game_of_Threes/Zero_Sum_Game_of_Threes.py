def zero_sum_game(n):
    """Returns the first solution found for the Zero-Sum Game of Threes. In the
    game, the player starts with an integer and at each step either divides by
    three if the integer is divisible by 3 or adds either +2, +1, -1, or -2 to
    the number to get a multiple of 3. This process continues until the result
    is equal to one. The sum of all additions and subtractions must equal 0.
    """
    global solution
    solution = None
    recursive_zero_sum_game(n, 0, [])
    return solution

def recursive_zero_sum_game(n, current_sum, sequence):
    """The solution recursively follows the rules of the game, branching when
    the number is not a multiple of three. The recursive calls maintain the
    current sum and sequence used to achieve the current sum and value.
    """
    global solution
    if solution is not None or n <= 0:
        return
    if n == 1:
        if current_sum == 0:
            solution = sequence
        return
    if n % 3 == 0:
        recursive_zero_sum_game(n / 3, current_sum, sequence + [(n, 0)])
    if n % 3 == 1:
        if n + 2 > 0:
            recursive_zero_sum_game(n + 2, current_sum + 2, sequence + [(n, +2)])
        if n - 1 > 0:
            recursive_zero_sum_game(n - 1, current_sum - 1, sequence + [(n, -1)])
    if n % 3 == 2:
        if n + 1 > 0:
            recursive_zero_sum_game(n + 1, current_sum + 1, sequence + [(n, +1)])
        if n - 2 > 0:
            recursive_zero_sum_game(n - 2, current_sum - 2, sequence + [(n, -2)])
            
        
print zero_sum_game(27)
print zero_sum_game(4)
print zero_sum_game(929)
print zero_sum_game(18446744073709551615)
