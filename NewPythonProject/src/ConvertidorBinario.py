# -*- coding: utf-8 -*-
""" Nombre del Programa     : Convertidor Decimal - Binario
    Fecha de Creación       : 28/08/2016
    Autor(s)                : Garcia Xoconostle Ivan Rafael
                            : Zuñiga Reyes Miguel Angel
    Descripción general     : Programa con funciones para realizar las conversiones siguientes:
                            Decimal - Base N        (dcmlToBaseN)
                            Decimal - Complemento 1 (decToC1)
                            Decimal - Complemento 2 (decToC2)
                            Binario - Decimal       (baseNToDec)
                            Complemento 1 - Decimal (c1ToDec)
                            Complemento 2 - Decimal (c2ToDec                          
"""

    
def dcmlToBaseN(number, base):
    """
    Metodo recursivo para convertir un número base 10 N a una base B
    :parameter number:  Número en base 10 a convertir
    :parameter base:    Base a la cual se va a convertir el número decimal.
    :return:            Número N en base B
    Ejemplo: number = 10, base = 2
    -> dcmlToBaseN(10/2,2) + "0" 
    -> dcmlToBaseN(5/2,2) + "10" 
    -> dcmlToBaseN(2/2,2) + "010" 
    -> regresa "1010" 
    """
    if number < base:
        return number
    else:
        return  str(dcmlToBaseN(number/base,base)) + str(number % base)
    
def decToC1(numberToCnvrt, deep):
    """
    
    """
    if deep == 0:
        numberToCnvrt = dcmlToBaseN(numberToCnvrt,2)
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
    return baseNToDec(decToC1(textC1,2, 1),0)

def c2ToDec(textC2):
    """
    
    """
    if textC2[:1] == '1':
        return pow(-2,len(textC2)-1) + baseNToDec(textC2[1:],2, 0)
    else:
        return baseNToDec(textC2,2, 0)

    
print(dcmlToBaseN(10, 2))