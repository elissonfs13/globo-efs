import requests
import json


def sendData(lineStr):
    url = 'http://localhost:8080/notification'
    headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

    lineStr = json.dumps(lineStr) 
    response = requests.post(url, data=json.loads(lineStr), headers=headers)

    if response.status_code == 200:
        print ('globo-efs-script: SEND JSON successfully: ' + lineStr)
    else:
        print ('globo-efs-script: ERROR to send ' + lineStr)


def readFile(fileName):
    with open(fileName) as f:
        lines = f.readlines()
    
    for line in lines:
        if line.startswith('{'):
            sendData(line.replace("'", '"').replace('\n', '').strip())


readFile('../notificacoes.txt')
