


from ast import expr
from cmath import exp
import math 
from re import compile

token_ref = {
    "bracket" : ['(', ')'],
    "dm_o" : ["/", "*"],
    "ad_o" : ["+", "-"]
    }

is_number = compile(r"[0-9]")

get_number = compile(r"(-?[0-9]+(?:\.[0-9]+)?)")

remove_whitespace = compile(r"\s+")



def display(tokenized):
    """Debug used to display the tokenized expression"""
    for i in tokenized:
        print(i["string"], end="")
    print("", flush=True)




def end_bracket(token_obj):
    """Used to parse everything after the initial ( of a bracket expression"""

    depth = 1

    for i in range(len(token_obj)):

        obj = token_obj[i]

        if obj["string"] == "(":
            depth += 1

        elif obj["string"] == ")":
            depth -= 1

        if depth == 0:
            return i 

    raise Exception("Invalid Expression -> 'Missing end bracket'")

def operation(a, b, o):
    """Evaluates float(a) operator float(b)"""

    if o == "*":
        return float(a) * float(b)

    if o == "/":
        if b == 0:
            raise Exception("Invalid Expression -> 'Cannot divide by zero'")

        return float(a) / float(b)

    if o == "+":
        return float(a) + float(b)

    if o == "-":
        return float(a) - float(b)

    raise Exception(f"Stuff is broken cause this operator does not exist -> '{o}")


def get_tokens(expression):
    """Puts the string expression into a tokenized format"""

    # remove whitespace
    expression = remove_whitespace.sub("", expression)

    tokenized = []

    i = -1
    while i + 1 < len(expression):

        i += 1
        
        # get first starting from current index
        m = get_number.match(expression, i)

        # found a match
        if m:
            
            # matched number as string
            string = m.group(0)

            # adjust index
            i += len(string) - 1
            
            # add the number to tokenized
            tokenized.append({
                "type" : -1,
                "string" : string
            })

            continue
        
        # could not match a number
        # checking for brackets and operators 

        char = expression[i]
        
        # bracket found, added it
        if char in token_ref["bracket"]:

            tokenized.append({
                "type" : 0,
                "string" : char
            })
            continue
        
        # addition or subtraction found
        if char in token_ref["ad_o"]:
            
            # -() found, need to multiple bracket by -1
            if char == "-" and expression[i + 1] == "(":

                tokenized.append({
                    "type" : 3,
                    "string" : "-m"
                })
                continue 
            
            # normal addition or subtraction
            tokenized.append({
                    "type" : 2,
                    "string" : char
            })
            continue
        
        # multiplication or division
        if char in token_ref["dm_o"]:
            tokenized.append({
                "type" : 1,
                "string" : char
            }) 
            continue

        raise Exception(f"Unknown character in expression -> {char}")

    return tokenized

     
def evaluate(tokenized):
    """Evaluates a tokenized expression"""

    length = len(tokenized)
    i = -1

    # evaluates * / -m and ()
    while i + 1 < length:

        # display(tokenized)

        i += 1

        obj = tokenized[i]
        typ = obj["type"]

        # type 0 == a bracket 
        if typ == 0:

            # get the end bracket position 
            eb = end_bracket(tokenized[i + 1: len(tokenized)])

            # evaluate everything in the brackets 
            r = evaluate(tokenized[i + 1:eb + i + 1])
            
            # idk how to delete more than 1 item from a list in python, so for loop
            # remove the brackets and everything inside it from the tokenized expression
            for _ in range(eb + 2): 
                del tokenized[i]

            # add the result of the brackets that were just removed
            tokenized.insert(i, {"type" : -1, "string" : r})

            # update length
            length = len(tokenized)

            # restart the loop
            i = -1
            continue
        
        # type -1 is a int / float
        if typ == -1:

            # length check
            if i + 2 >= length:
                continue
            
            # if current index is a number,
            # and the next item is * or /
            # and the next item is a number,
            # evaluate it and remove everything
            if tokenized[i + 1]["type"] == 1 and tokenized[i + 2]["type"] == -1:

                # overwrite current number with the result
                tokenized[i] = {
                    "type" : -1,
                    "string" : str(operation(obj["string"], tokenized[i + 2]["string"], tokenized[i + 1]["string"]))
                }
                del tokenized[i + 1] # remove the opoerator
                del tokenized[i + 1] # remove the number

                # adjust length
                length = len(tokenized)

                # restart loop
                i = -1
                continue
        
        # type 3 == -(int)  -> basically multiplies the number by -1
        if typ == 3:

            # length check
            if i + 1 >= length:
                continue
            
            # if the next item is a number
            if tokenized[i + 1]["type"] == -1:

                # remove the -m 
                del tokenized[i]

                # set the next number to itself * -1 
                tokenized[i] = {
                    "type" : -1,
                    "string" : operation(tokenized[i]["string"], -1, "*")
                }

                # adjust length
                length = len(tokenized)

                # restart loop (probably don't need to restart the loop with this but its easy)
                i = -1


    i = -1

    # evaluates + and -
    while i + 1 < length:

        # display(tokenized)

        i += 1
        obj = tokenized[i]
        typ = obj["type"]

        # don't think its possible to not have a type -1 here on the first iteration
        if typ != -1:
            continue
        
        # length check
        if i + 1 >= length:
            break 
        
        # regex turns - 5 into -5
        # so 5 - 3 will become {type : -1, string : 5}, {type : -1, string : -3}
        # so 2 type -1 in a row is addition of a negative
        if tokenized[i + 1]["type"] == -1:

            # evaluate the addition
            tokenized[i] = {
                    "type" : -1,
                    "string" : str(operation(obj["string"], tokenized[i + 1]["string"], "+"))
                }
            
            # remove next item
            del tokenized[i + 1]

            # adjust length and continue
            length = len(tokenized)
            continue
        
        # int +- int 
        # normal addition or subtraction
        if tokenized[i + 1]["type"] == 2 and tokenized[i + 2]["type"] == -1:
            
            # evaluate 
            tokenized[i] = {
                    "type" : -1,
                    "string" : str(operation(obj["string"], tokenized[i + 2]["string"], tokenized[i + 1]["string"]))
                }

            del tokenized[i + 1] # remove operator
            del tokenized[i + 1] # remove number

            # adjust length and continue 
            length = len(tokenized)

    # return the result 
    return tokenized[0]["string"]



def main():
    print(evaluate(get_tokens("(12 - 1) + 255 * 255 - 999/58")))
    print(evaluate(get_tokens("5 * 7 + (2 + 4 *(1 + 3))")))
    print(evaluate(get_tokens("2 + (10 * 10)")))
    print(evaluate(get_tokens("2 / (2 + 3) * 4.33 - -6")))
    print(evaluate(get_tokens("(1 - 2) + -(-(-(-4)))")))
    print(evaluate(get_tokens("5 * (2 + -4 / (1 + 1)) + 2.5")))
    print(evaluate(get_tokens("-(-1+2)")))
    print(evaluate(get_tokens("12 * 123 / -(-5 + 2.5)")))
    print(evaluate(get_tokens("2 / (2 + 3.33) * 4 --6")))

if __name__ == "__main__":
    main()