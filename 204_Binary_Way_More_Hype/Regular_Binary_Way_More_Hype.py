def hyperbinary(n):
    """Returns the hyperbinary numbers which represent the value n.
       Uses a recursive backtracking search that tests all branches (adding a 0,
       1, or 2) until it can be determined that n cannot be represented. This 
       occurs when `n < 2 ** power`.
    """
    if n % 2 == 0:
        return hyperbinary_recursive(n - 2, 1, "2") + hyperbinary_recursive(n, 1, "0")
    else:
        return hyperbinary_recursive(n - 1, 1, "1")
    
def hyperbinary_recursive(n, power, current):
    # Return the hyperbinary number if the digits represent our original number
    # (n is the remaining value left to convert into hyperbinary)
    if n == 0:
        return [current]
    # If the current value is greater than our n, we can stop early. No
    # additional digits can possibly represent our original value, since our
    # n will be negative.
    elif n < 2 ** power:
        return
        
    # Recursively find valid hyperbinary representations by adding 0, 1 and 2
    zero = hyperbinary_recursive(n, power + 1, "0" + current)
    one = hyperbinary_recursive(n - 2 ** power, power + 1, "1" + current)
    two = hyperbinary_recursive(n - 2 ** power * 2, power + 1, "2" + current)

    # Dirty set combination of 3 lists which could all potentially be None
    if zero is not None:
        if one is not None:
            return zero + one + two if two is not None else zero + one
        else:
            return zero + two if two is not None else zero
    elif one is not None:
        return one + two if two is not None else one
    else:
        return two

print hyperbinary(18)
print hyperbinary(73)
print hyperbinary(128)
print hyperbinary(239)
print len(hyperbinary(12345678910))
