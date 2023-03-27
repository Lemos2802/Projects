def readBytes(amount,curr,data):
	return (curr+amount), data[curr : (curr+amount)]


def dnsEncode(query):
	data = b''

	query = query.split(';',1)
	header = query[0].split(',')

	queryID = header[0]
	data += int(queryID).to_bytes(2,byteorder='big', signed=False)
	
	flags = header[1]
	flagCode = -1
	if flags == 'Q':
		flagCode = 0		
	elif flags == 'Q+A':
		flagCode = 1		
	elif flags == 'Q+R':
		flagCode = 2		
	elif flags == 'A':
		flagCode = 3		
	elif flags == 'R':
		flagCode = 4		
	data += flagCode.to_bytes(1,byteorder='big', signed=False)		
	
	responseCode = header[2]
	data += int(responseCode).to_bytes(1,byteorder='big', signed=False)
	
	nvalues = header[3]
	data += int(nvalues).to_bytes(1,byteorder='big', signed=False)
	
	nauth = header[4]
	data += int(nauth).to_bytes(1,byteorder='big', signed=False)
	
	nextras = header[5]
	data += int(nextras).to_bytes(1,byteorder='big', signed=False)
	
	if flags in ['Q','Q+A','Q+R']:
		queryInfo = query[1].split(',')
		domain = queryInfo[0]
		data += (len(domain)).to_bytes(1,byteorder='big', signed=False)
		data += domain.encode('ascii')
		queryType = queryInfo[1].rstrip(';')
		typeCode = -1
		if queryType == 'SOASP':
			typeCode = 0
		elif queryType == 'SOAADMIN':
			typeCode = 1
		elif queryType == 'SOASERIAL':
			typeCode = 2
		elif queryType == 'SOAREFRESH':
			typeCode = 3
		elif queryType == 'SOARETRY':
			typeCode = 4
		elif queryType == 'SOAEXPIRE':
			typeCode = 5
		elif queryType == 'NS':
			typeCode = 6
		elif queryType == 'MX':
			typeCode = 7
		elif queryType == 'A':
			typeCode = 8
		elif queryType == 'CNAME':
			typeCode = 9
		elif queryType == 'PTR':
			typeCode = 10
		data += typeCode.to_bytes(1,byteorder='big', signed=False)
	else:
		queryInfo = query[1].split(';',1)
		responses = queryInfo[1]
		queryInfo = queryInfo[0].split(',')
		domain = queryInfo[0]
		data += (len(domain)).to_bytes(1,byteorder='big', signed=False)
		data += domain.encode('ascii')
		queryType = queryInfo[1]
		typeCode = -1
		if queryType == 'SOASP':
			typeCode = 0
		elif queryType == 'SOAADMIN':
			typeCode = 1
		elif queryType == 'SOASERIAL':
			typeCode = 2
		elif queryType == 'SOAREFRESH':
			typeCode = 3
		elif queryType == 'SOARETRY':
			typeCode = 4
		elif queryType == 'SOAEXPIRE':
			typeCode = 5
		elif queryType == 'NS':
			typeCode = 6
		elif queryType == 'MX':
			typeCode = 7
		elif queryType == 'A':
			typeCode = 8
		elif queryType == 'CNAME':
			typeCode = 9
		elif queryType == 'PTR':
			typeCode = 10
		data += typeCode.to_bytes(1,byteorder='big', signed=False)
		
		data += (len(responses)).to_bytes(2,byteorder='big', signed=False)
		data += responses.encode('ascii')

	return data


def dnsDecode(data):
	string = ''
	curr = 0
	curr, res = readBytes(2,curr,data)
	string += str(int.from_bytes(res,'big'))
	string += ','
	curr, res = readBytes(1,curr,data)
	qtype = int.from_bytes(res,'big')
	if qtype == 0:
		string += 'Q,'
	elif qtype == 1:
		string += 'Q+A,'
	elif qtype == 2:
		string += 'Q+R,'
	elif qtype == 3:
		string += 'A,'
	elif qtype == 4:
		string += 'R,'

	curr, res = readBytes(1,curr,data)
	string += str(int.from_bytes(res,'big'))
	string += ','

	curr, res = readBytes(1,curr,data)
	string += str(int.from_bytes(res,'big'))
	string += ','

	curr, res = readBytes(1,curr,data)
	string += str(int.from_bytes(res,'big'))
	string += ','
	
	curr, res = readBytes(1,curr,data)
	string += str(int.from_bytes(res,'big'))
	string += ';'

	curr, res = readBytes(1,curr,data)
	aux = int.from_bytes(res,'big')

	curr, res = readBytes(aux,curr,data)
	string += res.decode('ascii')
	string += ','

	curr, res = readBytes(1,curr,data)
	aux = int.from_bytes(res,'big')
	if aux == 0:
		string += 'SOASP;'
	elif aux == 1:
		string += 'SOAADMIN;'
	elif aux == 2:
		string += 'SOASERIAL;'
	elif aux == 3:
		string += 'SOAREFRESH;'
	elif aux == 4:
		string += 'SOARETRY;'
	elif aux == 5:
		string += 'SOAEXPIRE;'
	elif aux == 6:
		string += 'NS;'
	elif aux == 7:
		string += 'MX;'
	elif aux == 8:
		string += 'A;'
	elif aux == 9:
		string += 'CNAME;'
	elif aux == 10:
		string += 'PTR;'

	if qtype in [3,4]:

		curr, res = readBytes(2,curr,data)
		aux = int.from_bytes(res,'big')
		curr, res = readBytes(aux,curr,data)
		string += res.decode('ascii')

	return string

