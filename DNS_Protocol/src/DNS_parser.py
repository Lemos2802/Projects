import sys
import re

def parseDB(file):

    f = open(file,'r')
    texto = f.readlines()

    lines = []

    for line in texto:
        parts = line.split(' ')
        if(parts[0] != '#' and parts[0] != '\n'):
            parts[-1] = parts[-1].rstrip()
            lines.append(parts)

    db = {'DOMAIN':[],'TTL':[],'SOASP':[],'SOAADMIN':[],'SOASERIAL':[],'SOAREFRESH':[],'SOARETRY':[],'SOAEXPIRE':[],'NS':[],'MX':[],'A':[],'CNAME':[],'PTR':[]}

    for line in lines:
        if line[1] == 'DEFAULT':
            if line[0] == '@': 
                db['DOMAIN'].append(line)
            elif line[0] == 'TTL':
                db['TTL'].append(line)
        elif line[1] == 'SOASP':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOASP'].append(newLine)
        elif line[1] == 'SOAADMIN':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOAADMIN'].append(newLine)
        elif line[1] == 'SOASERIAL':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOASERIAL'].append(newLine)
        elif line[1] == 'SOAREFRESH':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOAREFRESH'].append(newLine) 
        elif line[1] == 'SOARETRY':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOARETRY'].append(newLine)
        elif line[1] == 'SOAEXPIRE':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOAEXPIRE'].append(newLine) 
        elif line[1] == 'NS':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['NS'].append(newLine)
        elif line[1] == 'MX':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['MX'].append(newLine)
        elif line[1] == 'A':
            newLine = formatLineA(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['A'].append(newLine)
        elif line[1] == 'CNAME':
            newLine = formatLineCNAME(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['CNAME'].append(newLine)
        elif line[1] == 'PTR':
            newLine = formatLinePTR(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['PTR'].append(newLine)

        
    f.close()

    return db


def parseDB2(texto):

    texto2 = texto.split('\n')

    lines = list()  

    for line in texto2:
        parts = line.split(' ')
        if(parts[0] != '#' and parts[0] != '\n'):
            parts[-1] = parts[-1].rstrip()
            lines.append(parts)

    db = {'DOMAIN':[],'TTL':[],'SOASP':[],'SOAADMIN':[],'SOASERIAL':[],'SOAREFRESH':[],'SOARETRY':[],'SOAEXPIRE':[],'NS':[],'MX':[],'A':[],'CNAME':[],'PTR':[]}
    if len(lines[-1]) < 2:
        lines.pop()
    for line in lines:
        if line[1] == 'DEFAULT':
            if line[0] == '@': 
                db['DOMAIN'].append(line)
            elif line[0] == 'TTL':
                db['TTL'].append(line)
        elif line[1] == 'SOASP':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOASP'].append(newLine)
        elif line[1] == 'SOAADMIN':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOAADMIN'].append(newLine)
        elif line[1] == 'SOASERIAL':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOASERIAL'].append(newLine)
        elif line[1] == 'SOAREFRESH':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOAREFRESH'].append(newLine) 
        elif line[1] == 'SOARETRY':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOARETRY'].append(newLine)
        elif line[1] == 'SOAEXPIRE':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['SOAEXPIRE'].append(newLine) 
        elif line[1] == 'NS':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['NS'].append(newLine)
        elif line[1] == 'MX':
            newLine = formatLine(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['MX'].append(newLine)
        elif line[1] == 'A':
            newLine = formatLineA(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['A'].append(newLine)
        elif line[1] == 'CNAME':
            newLine = formatLineCNAME(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['CNAME'].append(newLine)
        elif line[1] == 'PTR':
            newLine = formatLinePTR(line,db['TTL'][0][2],db['DOMAIN'][0][2])
            db['PTR'].append(newLine)

    return db



def concatList(list):
    res = list[0]
    for item in list[1:]:
        res = res + ' ' + item
    return res



def parseConfig(file):

    f = open(file,'r')
    texto = f.readlines()

    lines = []

    for line in texto:
        parts = line.split(' ')
        if(parts[0] != '#' and parts[0] != '\n'):
            parts[-1] = parts[-1].rstrip()
            lines.append(parts)

    dictionary = {}
    dictionary['SS'] = []
    dictionary['DOMAIN'] = lines[0][0]

    for line in lines:
        if line[1] == 'SS':
            dictionary['SS'].append(line[2])
        else:
            dictionary.update({line[1]:line[2]})

    f.close()

    return dictionary



def parseSDT(file):
    
    sdts = []

    f = open(file,'r')
    texto = f.readlines()

    for line in texto:
        if line[0] != '#' and line[0] != '\n': 
            sdts.append(line.rstrip())

    f.close()
    
    return sdts


def formatLine(line,ttl,domain):
    for i in range(len(line)):
        if '@' in line[i]:
            line[i] = re.sub(r'@',domain,line[i])   
        elif line[i] == 'TTL':
            line[i] = ttl

    return line



def formatLineA(line,ttl,domain):
    line[0] = line[0] + '.' + domain
    if line[3] == 'TTL':
        line[3] = ttl

    return line


def formatLineCNAME(line,ttl,domain):
    line[0] = line[0] + '.' + domain
    line[2] = line[2] + '.' + domain
    if line[3] == 'TTL':
        line[3] = ttl

    return line

def formatLinePTR(line,ttl,domain):
    line[0] = line[0] + '.' + domain
    line[2] = line[2] + '.' + domain
    if line[3] == 'TTL':
        line[3] = ttl

    return line
