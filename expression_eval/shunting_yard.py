
import math
import re



LEFT = False 
RIGHT = True

NUMBER = 0
OPERATOR = 1
BRACKET = 3
FUNCTION = 4
COMMA = 5
LEFT_BRACKET = 6
RIGHT_BRACKET = 7


class Token:

    def __init__(self, type: int, precedence: int, associativity: bool, value, apply=None, arg_amt=0) -> None:
        self.type  = type
        self.precedence = precedence
        self.associativity = associativity
        self.value = value
        self.apply = apply
        self.arg_amt = arg_amt
        
    def __repr__(self) -> str:
        return f"<Token type: {self.type} prec: {self.precedence} asso: {self.associativity} valu: {self.value} >"



TOKENS = (
    Token(FUNCTION, 100, LEFT, value="sin", apply=lambda x : math.sin(math.radians(x)), arg_amt=1),
    Token(FUNCTION, 100, LEFT, value="cos", apply=lambda x : math.cos(math.radians(x)), arg_amt=1),
    Token(FUNCTION, 100, LEFT, value="tan", apply=lambda x : math.tan(math.radians(x)), arg_amt=1),
    Token(FUNCTION, 100, LEFT, value="max", apply=max , arg_amt=2),
    Token(FUNCTION, 100, LEFT, value="fac", apply=math.factorial, arg_amt=1),
    Token(OPERATOR, 20, RIGHT, value="^", apply=lambda a,b: a ** b ),
    Token(OPERATOR, 10, LEFT , value="*", apply=lambda a,b: a * b ),
    Token(OPERATOR, 10, LEFT , value="x", apply=lambda a,b: a * b ),
    Token(OPERATOR, 10, LEFT , value="/", apply=lambda a,b: a / b ),
    Token(OPERATOR, 5 , LEFT , value="+", apply=lambda a,b: a + b ),
    Token(OPERATOR, 5 , LEFT , value="-", apply=lambda a,b: a - b ),

    Token(COMMA   , 0, LEFT, value="," ),
    Token(LEFT_BRACKET , 0, RIGHT, value="(" ),
    Token(RIGHT_BRACKET, 0, LEFT , value=")" ),
)

def get_number_token(number: str):

    return Token(NUMBER, 0, LEFT, float(number))

def get_token(value: str):

    for i in TOKENS:

        if i.value == value:

            return i 
        
    return get_number_token(value)

def next_token(input: str):
    
    input = input.strip()
    
    for token in TOKENS:
            
        if input.startswith(token.value):
        
            return token.type, get_token(input[0:len(token.value)]), input[len(token.value):]
         
    m = re.match(r"^(\d+(?:\.\d+)?)", input)

    if m:
        return NUMBER, get_number_token(m.group()), input[len(m.group()):]
         

         
def shunting_yard(src: str) -> list[Token]:
    """
    the shunting yard algorithm:
    https://en.wikipedia.org/wiki/Shunting_yard_algorithm

    """

    op_stack: list[Token] = []
    out_stack = []

    while True:

        if not src:
            break

        token_type, token, src = next_token(src)
         
        if token_type == NUMBER:

            out_stack.append(token)

        elif token_type == FUNCTION:

            op_stack.append(token)

        elif token_type == OPERATOR:

            while (op_stack and (
                   op_stack[-1].type != LEFT_BRACKET and 
                   (op_stack[-1].precedence > token.precedence or 
                    (op_stack[-1].precedence == token.precedence 
                     and token.associativity == LEFT)))):

                out_stack.append(op_stack.pop())
                   
            op_stack.append(token)

        elif token_type == COMMA:

            while (op_stack and (op_stack[-1].type != LEFT_BRACKET)):

                out_stack.append(op_stack.pop())

        elif token_type == LEFT_BRACKET:

            op_stack.append(token)
                            
        elif token_type == RIGHT_BRACKET:

            while (op_stack and (op_stack[-1].type != LEFT_BRACKET)):
                   
                out_stack.append(op_stack.pop())
                   
            if op_stack:

                op_stack.pop()
            
            else:

                raise Exception("Bracket missmatch")
         

    while op_stack:

        if op_stack[-1].type in (LEFT_BRACKET, RIGHT_BRACKET):

            raise Exception("Bracket missmatch")
        
        out_stack.append(op_stack.pop())

    print(f"Postfix Notation is : "," ".join(str(v.value) for v in out_stack))
    
    return out_stack


def evaluate(equation: str):

    out_stack: list[Token] = shunting_yard(equation)
    
    hist = []

    while out_stack:

        i = out_stack.pop(0)

        if i.type == NUMBER:
            hist.append(i.value)
        
        elif i.type == OPERATOR:
            
            if len(hist) == 1:

                raise Exception(f"Operator '{i.value}' only has 1 operand: {hist[0]}")

            else:
                
                b = hist.pop()
                a = hist.pop()

                ans = i.apply(a, b)
                
                hist.append(ans)
                
        elif i.type == FUNCTION:
            
            if len(hist) < i.arg_amt:

                raise Exception(f"Function '{i.value}' has missing arguments. Expected {i.arg_amt}, got {len(hist)}")

            args = hist[-i.arg_amt :]

            ans = i.apply(*args)
            
            for i in range(len(args)):
                hist.pop()
            
            hist.append(ans)

                
    return hist[0]



if __name__ == "__main__":
    
    tests = [
        (("sin ( 5 / 3 * 3.14159 )"), math.sin(math.radians( 5 / 3 * 3.14159 ))),
        (("3 + 4 * 2 / ( 1 - 5 )")  , (3 + 4 * 2 / ( 1 - 5))),
        (("5.0 + 3 * sin(5 + 3)")   , (5 + 3 * math.sin(math.radians(5 + 3)))),
        (("(12 - 1) + 255 * 255 - 999/58") , ((12 - 1) + 255 * 255 - 999 / 58)),
        (("2 + (10 * 10)") , (2 + (10 * 10))),
        (("5 * 7 + (2 + 4 *(1 + 3))") , (5 * 7 + (2 + 4 * (1 + 3)))),
        (("5 * (2 + 4 / (1 + 1)) + 2.5")  , (5 * (2 + 4 / ( 1 + 1)) + 2.5)),
        (("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3") , (3 + 4 * 2 / ( 1 - 5 ) ** 2 ** 3)),
        (("3 + 4 x 2 / ( 1 - 5 ) ^ 2 ^ 3") , (3 + 4 * 2 / ( 1 - 5 ) ** 2 ** 3)),
    ]
    
    for (equ, out) in tests:

        assert evaluate(equ) == out



    """

    it cannot handle negatives very well, the negative operator works fine,

    but stuff like -1  or -(5 + 1), it treats the negative sign as the negative operator,

    basically need to figure out how to apply the negatives to numbers

    """

    # assert evaluate("2 / (2 + 3) * 4.33 - -6") == (2 / (2 + 3) * 4.33 - - 6)
    # assert evaluate("(1 - 2) + -(-(-(-4)))") == ((1 - 2) + -(-(-(-4))))
    # assert evaluate("-(-1+2)")

    # assert evaluate("12 * 123 / -(-5 + 2.5)")
    # assert evaluate("2 / (2 + 3.33) * 4 --6")
    # assert evaluate("10 - 2 - -5")