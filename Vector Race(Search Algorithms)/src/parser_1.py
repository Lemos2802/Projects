def parser():
    
    file = open("../senra.txt","r")
    lines = file.readlines()
    file.close()

    # traços = 1
    # metas = 2
    # pontoInicial = 3
    # paredes = 4

    percurso = list()
    final = list()

    x = 0
    y = 0

    for line in lines:
        line = line.strip()
        percurso.append(0)
        percurso[y] = list()
        for char in line:
            
            if char == 'P':
                percurso[y].append(1)
                inicio = (y,x)
                
            elif char == 'F':
                percurso[y].append(2)
                final.append((y,x))
                
            elif char == '-':
                percurso[y].append(1)
                
            elif char == 'X':
                percurso[y].append(4)
            
            elif char == 'L':
                percurso[y].append(1)
                inicio2 = (y,x)

            else: 
                x -= 1
                
            x += 1

        y += 1
        x = 0
    
    return (inicio, final, percurso,inicio2)