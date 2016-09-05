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
    Método recursivo para convertir un número base 10 N a una base B
    parámetro number:  Número en base 10 a convertir
    parámetro base:    Base a la cual se va a convertir el número decimal.
    return:            Número N en base B
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
    Método recursivo para convertir un número decimal a su representación Complemento a 1 en binario
    parámetro numberToCnvrt:    Número en base 10 a convertir, tiene que ser mayor a 0
    parámetro deep:             Variable numerica que nos ayuda a indicar un proceso de conversión previo a  
                                la recursión que solo se hace una vez en. Se requiere llamar con 0.
                                deep = 0 ; Si la función requiere convertir el número a binario 
                                deep != 0 ; si el número ya esta convertido a binario.
    return:                     Cadena que representa al número en binario complemento a 1
    Ejemplo: numberToCnvrt = 10, deep = 0
    -> "0101"
    """
    if deep == 0:
        numberToCnvrt = dcmlToBaseN(numberToCnvrt,2)
        deep = 1
        
    if numberToCnvrt == "":
        return ""
    else:
        if numberToCnvrt[-1:] == '0':
            return str(decToC1(numberToCnvrt[:-1],deep)) + str(1) 
        else:
            return str(decToC1(numberToCnvrt[:-1],deep)) + str(0)  
 
def decToC2(numberToCnvrt):
    """
    Método para convertir un número decimal a su representación Complemento a 2 en binario, hace uso de un método recursivo
    parámetro numberToCnvrt:    Número en base 10 a convertir, puede ser negativo.
    return:                     Cadena que representa al número en binario complemento a 2
    Si el número de entrada es positivo se realiza una conversión a binario con dcmlToBaseN y se agrega el bit 0 al inicio para representar que es positivo.
    Si el número es negativo se obtiene el valor correspondiente a través de decToC2Recursive y se agrega el bit 1 al inicio para indicar que es negativo.
    Ejemplo: numberToCnvrt = 20
    -> "010100"
    Ejemplo: numberToCnvrt = -20
    -> "101100"
    """
    if numberToCnvrt > 0:
        return str(0) + dcmlToBaseN(numberToCnvrt,2)
    else:
        numberToCnvrt = decToC1(numberToCnvrt*-1, 0)
        return  str(1) + decToC2Recursive(numberToCnvrt, 1)
    
def decToC2Recursive(numberToCnvrt, sum):
    """
    Método recursivo para obtener el valor correspondiente de un número decimal negativo a binario complemento a 2.
    parámetro numberToCnvrt:    Cadena en binario a complemento a 1 del número a convertir.
    parámetro sum:              Indicador para el algoritmo, decide si el digito actual se suma y cambia o se mantiene, representa el acarreo
                                Siempre se coloca el valor de 1.
                                
    Tomando el complemento a 1 del número se le suma 1 recorriendo los bits del último al primero, si hay un acarreo 
    la siguiente llamada mantiene el valor de sum a 1, de lo contrario, es decir si se sumo "0" con "1" ya no hay acarreo 
    y se termina la recursión.
    
    """
    if numberToCnvrt == "":
        return ""
    else:
        if sum == 1:
            if numberToCnvrt[-1:] == '0':
                #No hay acarreo y en la siguiente llamada termina la recursión
                return decToC2Recursive(numberToCnvrt[:-1], 0) + str(1)
            else:
                return decToC2Recursive(numberToCnvrt[:-1], 1) + str(0)
        else:
            return numberToCnvrt
        

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

    
print(decToC2(-20))