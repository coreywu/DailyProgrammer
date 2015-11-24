def ternary_to_decimal(ternary):
    result = 0
    power = 0
    for digit in str(ternary)[::-1]:
        result += int(digit) * 3 ** power
        power += 1
    return result

def decimal_to_ternary(decimal):
    result = ""
    while decimal > 0:
        result += str(decimal % 3)
        decimal = decimal / 3
    return result[::-1]

def encode(ternary):
    return encode_recursive(list(reversed(list(str(ternary)))), 1)

def encode_recursive(ternary_list, current_value):
    if not ternary_list:
        return [current_value]
    if ternary_list[0] == '0':
        return encode_recursive(ternary_list[1:], current_value * 3)
    else:
        return encode_recursive(ternary_list[1:], current_value * 3 + int(ternary_list[0])) \
            + encode_recursive(ternary_list[1:], current_value * 3 - int(ternary_list[0]))

def decode(encoded):
    return decode_recursive(encoded, [])

def decode_recursive(encoded, current_list):
    if encoded <= 0:
        return
    elif encoded == 1:
        current_list = "".join([str(x) for x in current_list])
        return [current_list]
    elif encoded % 3 == 0:
        current_list.append(0)
        return decode_recursive(encoded / 3, current_list)
    elif encoded % 3 == 1:
        new_list1 = list(current_list)
        new_list1.append(1)
        new_list2 = list(current_list)
        new_list2.append(2)
        result1 = decode_recursive((encoded - 1) / 3, new_list1)
        result2 = decode_recursive((encoded + 2) / 3, new_list2)
        if result1 is None:
            return result2 if result2 is not None else None
        else:
            return result1 + result2 if result2 is not None else result1
    elif encoded % 3 == 2:
        new_list1 = list(current_list)
        new_list1.append(2)
        new_list2 = list(current_list)
        new_list2.append(1)
        result1 = decode_recursive((encoded - 2) / 3, new_list1)
        result2 = decode_recursive((encoded + 1) / 3, new_list2)
        if result1 is None:
            return result2 if result2 is not None else None
        else:
            return result1 + result2 if result2 is not None else result1

print encode(10121)
print
print 1343814725227 in encode(1102210212110201020210202)
#print

print "262 -> a", decode(262)

print
print
print "11022 to decimal", ternary_to_decimal(11022)
print "10212 to decimal", ternary_to_decimal(10212)
print "11020 to decimal", ternary_to_decimal(11020)
print "10202 to decimal", ternary_to_decimal(10202)

print
print
print "116 to ternary", decimal_to_ternary(116)
print "104 to ternary", decimal_to_ternary(104)
print "114 to ternary", decimal_to_ternary(114)
print "101 to ternary", decimal_to_ternary(101)
