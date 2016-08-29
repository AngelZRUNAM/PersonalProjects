
def invert(text):
    if text == "":
        return ""
    else:
        return text[-1:] + str(invert(text[:-1]))

def dcmlToBaseN(number, base):
    """
    
    """
    return invert(dcmlToBaseNRcrsv(number, base))
    
def dcmlToBaseNRcrsv(number, base):
    """ 
    
    """
    if number < base:
        return number
    else:
        return str(number % base) + str(dcmlToBaseNRcrsv(number/base,base))
    
def decToC1(numberToCnvrt, deep):
    """
    
    """
    if deep == 0:
        numberToCnvrt = dcmlToBaseNRcrsv(numberToCnvrt,2)
        deep = 1
        
    if numberToCnvrt == "":
        return ""
    else:
        if numberToCnvrt[-1:] == '0':
            return str(1) + str(decToC1(numberToCnvrt[:-1],deep))
        else:
            return str(0) + str(decToC1(numberToCnvrt[:-1],deep))
 
def decToC2Recursive(numberToCnvrt, sum):
    """
    
    """
    if numberToCnvrt == "":
        return ""
    else:
        if sum == 0:
            if numberToCnvrt[-1:] == '0':
                return decToC2Recursive(numberToCnvrt[:-1], 1) + str(1)
            else:
                return decToC2Recursive(numberToCnvrt[:-1], sum) + str(0)
        else:
            return numberToCnvrt
        

def decToC2(numberToCnvrt):
    """
    
    """
    if numberToCnvrt > 0:
        return str(0) + dcmlToBaseN(numberToCnvrt,2)
    else:
        numberToCnvrt = decToC1(numberToCnvrt*-1, 0)
        return  str(1) + decToC2Recursive(numberToCnvrt, 0)

def baseNToDec(textBin, base, exp):
    """
    
    """
    if textBin == "":
        return 0
    else:
        return int(textBin[-1:])*pow(base,exp) + int(baseNToDec(textBin[:-1],base, exp+1))  

def c1ToDec(textC1):
    """
    
    """
    return baseNToDec(invert(decToC1(textC1,2, 1)),0)

def c2ToDec(textC2):
    """
    
    """
    if textC2[:1] == '1':
        return pow(-2,len(textC2)-1) + baseNToDec(textC2[1:],2, 0)
    else:
        return baseNToDec(textC2,2, 0)

    
         