from DNS_parser import *
import re


def handleQuery(query,db):
	regex = r'^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5]),(Q\+R|Q\+A|Q),0,0,0,0;(.+\.)+,(SOASP|SOAADMIN|SOASERIAL|SOAREFRESH|SOARETRY|SOAEXPIRE|NS|MX|A|CNAME|PTR);$'
	match = re.search(regex,query)

	if match:
		query = query.split(';')
		header = query[0].split(',')
		queryInfo = query[1].split(',')

		id = header[0]
		name = queryInfo[0]
		types = queryInfo[1].rstrip("\n;")
		flags = 'A'
		nValues = 0
		values = []
		nAuth = 0
		auth = []
		nExtras = 0
		extras = []
		entries = ''
		responsecode = -1

		if domainExists(name,db['DOMAIN'][0][2]):

			if types == 'PTR':
				nValues, values = searchLinePTR(name,db)
				if nValues == 0:
					responsecode = '1'
				elif nValues > 0:
					responsecode = '0'

				i = len(values)-1
				for v in values:
					if i == 0:
						entries = entries + concatList(v)
					else:
						entries = entries + concatList(v) + ','
					i = i-1

			else:	
				nValues, values = searchLine(types,name,db) 

				if types != 'NS':
					nAuth, auth = searchLineAuth(name,db)

				if types in ['SOASP','SOAADMIN','SOASERIAL','SOAREFRESH','SOARETRY','SOAEXPIRE']:
					nExtras, extras = searchExtras(db['A'],auth)
				elif types == 'A':
					if nValues == 0:
						nExtras, extras = searchExtras(db['A'],auth)
					else:
						pass
				else:
					nExtras, extras = searchExtras2(db['A'],values,auth)

				i = len(values)-1
				for v in values:
					if i == 0:
						entries = entries + concatList(v)
					else:
						entries = entries + concatList(v) + ','
					i = i-1
				entries = entries + ';'
				i = len(auth)-1
				for a in auth:
					if i == 0:
						entries = entries + concatList(a)
					else:
						entries = entries + concatList(a) + ','
					i = i-1
				entries = entries + ';'
				i = len(extras)-1
				for e in extras:
					if i == 0:
						entries = entries + concatList(e)
					else:
						entries = entries + concatList(e) + ','
					i = i-1
				entries = entries + ';'

				if nValues == 0:
					responsecode = '1'
				elif nValues > 0:
					responsecode = '0'

		else:
			responsecode = '2'
		reply = id + ',' + flags + ',' + responsecode + ',' + str(nValues) + ',' + str(nAuth) + ',' + str(nExtras) + ';' + name + ',' + types + ';' + entries
	else:
		reply = '0,R,3,0,0,0;;'
	return reply


def searchLine(types,name,db):
	nValues = 0
	values = []

	for line in db[types]:
		if line[0] == name or line[2] == name:
			values.append(line)
			nValues = nValues + 1
	return nValues, values


def searchLinePTR(name,db):
	nValues = 0
	values = []

	name = flipIP(name)
	for line in db['PTR']:
		if line[0] == name:
			values.append(line)
			nValues = nValues + 1
	return nValues, values



def searchLineAuth(name,db):
	nAuth = 0
	auth = []
	domain = name.split('.',1)[1]

	domain = name
	for x in range(name.count('.')):
		if len(auth) == 0:
			for line in db['NS']:
				if line[0] == domain:
					auth.append(line)
					nAuth = nAuth+1
		else:
			break
		domain = domain.split('.',1)[1]
	return nAuth, auth


def searchExtras(listA,auth):
	extras = []
	nExtras = 0
	machines = getMachines(auth)
	for a in listA:
		if a[0] in machines:
			extras.append(a)
			nExtras = nExtras + 1
	return nExtras, extras


def searchExtras2(listA,values,auth):
	extras = []
	nExtras = 0
	machines = getMachines2(values,auth)
	for a in listA:
		if a[0] in machines:
			extras.append(a)
			nExtras = nExtras + 1

	return nExtras, extras


def getMachines2(values,auth):
	res = []
	for v in values:
		res.append(v[2])
	for a in auth:
		res.append(a[2])
	return res



def getMachines(auth):
	res = []
	for a in auth:
		res.append(a[2])
	return res


def formatLine(line,ttl,default):
	for i in range(len(line)):
		if line[i] == '@':
			line[i] = default
		if line[i] == 'TTL':
			line[i] = ttl

	return line


def displayReply(reply):
	fields = reply.split(';')
	headerFields = fields[0].split(',')
	queryInfoFields = ['','']
	if headerFields[2] != '3':
		queryInfoFields = fields[1].split(',')
	values = []
	auth = []
	extras = [] 
	if headerFields[3] != '0':
		values = fields[2].split(',')
	if headerFields[4] != '0':
		auth = fields[3].split(',')
	if headerFields[5] != '0':
		extras = fields[4].split(',')
	print('MESSAGE ID :')
	print(headerFields[0])
	print('')
	print('FLAGS :')
	print(headerFields[1])
	print('')
	print('RESPONSE CODE :')
	print(headerFields[2])
	print('')
	print('QUERY NAME :')
	print(queryInfoFields[0])
	print('')
	print('QUERY TYPE :')
	print(queryInfoFields[1])
	print('')
	print('RESPONSE VALUES =',headerFields[3],':')
	for v in values:
		print(v)
	print('')
	print('AUTH VALUES =',headerFields[4],':')
	for a in auth:
		print(a)
	print('')
	print('EXTRA VALUES =',headerFields[5],':')
	for e in extras:
		print(e)	


def domainExists(name,domain):
	res = False
	for x in range(name.count('.')):
		if name == domain:
			res = True
			break
		else:
			name = name.split('.',1)[1]
	return res


def printDB(db):
	for x in db:
		print(x)
		for y in db[x]:
			print(y)

def protocolo_tz(tipo, seq_n, line):
	if tipo in [0,1,2]:
		return f"{tipo};{seq_n};{line}"

def flipIP(ip):
	fields = ip.split('.',4)
	res = fields[3] + '.' + fields[2] + '.' + fields[1] + '.' + fields[0] + '.' + fields[4]
	return res


