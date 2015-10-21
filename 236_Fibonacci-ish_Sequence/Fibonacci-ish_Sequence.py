def fibonacci_ish_sequence(n):
    """ Calculate the lowest value of f(1) which generates a Fibonacci-ish
    sequence that contains the integer n. All values in the sequence of 
    Fibonacci-ish are multiples of the original Fibonacci. So, we can determine
    whether a Fibonacci-ish value can be equal to n by checking if n is a 
    multiple of it. To find the lowest value, we start at the back (the value
    in the original Fibonacci that is equal to or less than n)."""
    if n == 0:
        return 1

    # Find all Fibonacci values up to the value that is equal to or greater
    # than the integer n
    current_fibonacci_value = 1
    fibonacci_count = 1
    while n > current_fibonacci_value:
        generate_fibonacci(fibonacci_count + 1)
        fibonacci_count += 1
        current_fibonacci_value = fibonacci[fibonacci_count]

    # All values of the generated Fibonacci sequence can be possible factors
    # of the integer n. We will look for the largest factor.
    f1_found = False

    # If our original Fibonacci sequence contains n, then we just return 1,
    # signifying the original.
    if current_fibonacci_value == n:
        return 1
    else:
        fibonacci_count -= 1

    # Otherwise, we start at the back and look for the largest factor of n from
    # the original Fibonacci sequence. Once we have the largest factor, we can
    # divide n by that factor to find the smallest factor we need f(1) to be to
    # generate a sequence with n.
    while not f1_found:
        if n % fibonacci[fibonacci_count] == 0:
            return n / fibonacci[fibonacci_count]
        else:
            fibonacci_count -= 1

fibonacci = {}
fibonacci[0] = 0
fibonacci[1] = 1

def generate_fibonacci(n):
    """ Generate Fibonacci sequence up to index n dynamically."""
    if n - 1 in fibonacci:
        result = fibonacci[n - 1] + fibonacci[n - 2]
    else:
        result = generate_fibonacci(n - 1) + generate_fibonacci(n - 2)
    fibonacci[n] = result
    return result

input1 = 21
input2 = 84
challenge_input1 = 0
challenge_input2 = 578
challenge_input3 = 123456789 
challenge_input4 = 38695577906193299

print fibonacci_ish_sequence(input1)
print fibonacci_ish_sequence(input2)
print fibonacci_ish_sequence(challenge_input1)
print fibonacci_ish_sequence(challenge_input2)
print fibonacci_ish_sequence(challenge_input3)
print fibonacci_ish_sequence(challenge_input4)
