#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Oct 21 20:26:47 2020

@author: chi
"""

import sys
import numpy as np
import csv
            
def readfile(inputfile):
    x = list()
    y = list()
    with open(inputfile, newline='\n') as f:
        reader = csv.reader(f,delimiter=',')
        for row in reader:
            y.append(row[0:1])
            x.append(row[1:])
    bias = np.ones((len(y),1))
    x_data = np.array(x)
    x_data = np.hstack((bias,x_data))
    y_data = np.array(y)
    x_data = x_data.astype(float)
    y_data = y_data.astype(int)
    return x_data,y_data

def initialize(init_flag,hidden_units,x,y):
    if init_flag == 2:
        alpha = np.zeros((hidden_units,len(x[0])))
        beta = np.zeros((10,hidden_units+1))
    elif init_flag == 1:
        alpha = np.random.uniform(low=-0.1, high=0.1, size=(hidden_units,len(x[0])))
        alpha[::,0] = 0
        beta = np.random.uniform(low=-0.1, high=0.1, size=(10,hidden_units+1))
        beta[::,0] = 0
    return alpha,beta
        

def sigmoid(x):
    return 1/(1 + np.exp(-x))

def predict(x,y,alpha,beta):
    pred = np.zeros(len(x))
    i = 0
    for row in x:
        a = (np.dot(row,alpha.T)).reshape(1,len(alpha))
        sigmoid_vec = np.vectorize(sigmoid)
        z = sigmoid_vec(a) 
        z = (np.insert(z,0,1)).reshape(1,z.size+1)
        b = np.dot(beta,z.T).T
        b = b.astype(np.float128)
        yhat = np.exp(b)/np.sum(np.exp(b))
        pred[i] = np.argmax(yhat)
        i+=1
    return pred.astype(int),errorCalc(pred,y)

def meanCrossEntropy(x,y,alpha,beta):
    i = 0
    sumEntropy = 0
    for row in x:
        a = (np.dot(row,alpha.T)).reshape(1,len(alpha))
        sigmoid_vec = np.vectorize(sigmoid)
        z = sigmoid_vec(a) 
        z = (np.insert(z,0,1)).reshape(1,z.size+1)
        b = np.dot(beta,z.T).T
        b = b.astype(np.float128)
        yhat = np.exp(b)/np.sum(np.exp(b))
        sumEntropy += crossEntropy(y[i], yhat[0])
        i+=1
    return sumEntropy/len(x)
    


def crossEntropy(y_i,yhat_i):
    return -np.log(yhat_i[y_i])


def forward(alpha,beta,x_i,y):
    a = (np.dot(x_i,alpha.T)).reshape(1,len(alpha))
    #print('a before sigmoid')
    #print(a)
    sigmoid_vec = np.vectorize(sigmoid)
    z = sigmoid_vec(a)
    z = (np.insert(z,0,1)).reshape(1,z.size+1)
    #print('z after sigmoid')
    #print(z)
    b = np.dot(beta,z.T).T
    b = b.astype(np.float128)
    #print('b before softmax')
    #print(b)
    yhat = np.exp(b)/np.sum(np.exp(b))
    #print('yhat after softmax')
    #print(yhat)
    return yhat,z
    
def backward(yhat,y_i,x_i,z,alpha,beta,learning_rate):
    dloss_dbv = yhat
    dloss_dbv[0][y_i] = dloss_dbv[0][y_i] - 1
    #print('dloss/db')
    #print(dloss_dbv)
    dloss_dbeta = np.dot(dloss_dbv.T,z)
    #print('dloss/dbeta')
    #print(dloss_dbeta)
    beta_star = np.delete(beta,0,1)
    dloss_dz = np.dot(dloss_dbv,beta_star)
    #print('dloss/dz')
    #print(dloss_dz)
    dloss_da = dloss_dz*z[0][1:]*(1-z[0][1:])
    #print('dloss/da')
    #print(dloss_da)
    dloss_dalpha = np.dot(dloss_da.T,x_i.reshape(1,len(x_i)))
    #print('dloss/dalpha')
    #print(dloss_dalpha)
    alpha = alpha - learning_rate*dloss_dalpha
    #print('new alpha')
    #print(alpha)
    beta = beta - learning_rate*dloss_dbeta
    #print('new beta')
    #print(beta)
    return alpha,beta

def errorCalc(y_pred,y):
    error = 0
    compare = (y.T==y_pred)
    incorrectPred = np.count_nonzero(compare == False)
    error = incorrectPred/y_pred.size
    return error  
    
def SGD(x,y,x_valid,y_valid,num_epoch,hidden_units,init_flag,learning_rate,metrics_output,train_output,valid_output):
    alpha,beta = initialize(init_flag, hidden_units, x, y)
    lines = list()
    for epoch in range(0,num_epoch):
        #print('epoch ', epoch)
        for i in range(len(x)):
            #print('sample',i)
            #print('Begin forward')
            yhat, z = forward(alpha,beta,x[i],y)
            #print('Begin backward')
            alpha,beta = backward(yhat,y[i],x[i],z,alpha,beta,learning_rate)
            
        meanEntropyTrain = meanCrossEntropy(x,y,alpha,beta)
        line = 'epoch='+str(epoch+1)+' crossentropy(train): '+str(meanEntropyTrain[0])+'\n'
        lines.append(line)
        
        meanEntropyValid = meanCrossEntropy(x_valid,y_valid,alpha,beta)
        line = 'epoch='+str(epoch+1)+' crossentropy(valid): '+str(meanEntropyValid[0])+'\n'
        lines.append(line)
        
    trainpred, errorTrain = predict(x,y,alpha,beta)
    np.savetxt(train_output, trainpred, delimiter="\n",fmt='%s')
    
    validpred, errorValid = predict(x_valid,y_valid,alpha,beta)
    np.savetxt(valid_output, validpred, delimiter="\n",fmt='%s')
    
    line = 'error(train): ' + str(errorTrain) + '\n'
    lines.append(line)
    line = 'error(validation): ' + str(errorValid) + '\n'
    lines.append(line)
    with open(metrics_output, 'w') as f:
        for line in lines:
            f.write(line)

if __name__ == '__main__':
    train_input = sys.argv[1] 
    valid_input = sys.argv[2]
    train_output = sys.argv[3]
    valid_output = sys.argv[4]
    metrics_output = sys.argv[5]
    num_epoch = int(sys.argv[6])
    hidden_units = int(sys.argv[7])
    init_flag = int(sys.argv[8])
    learning_rate = float(sys.argv[9])
    x,y=readfile(train_input)
    x_valid,y_valid = readfile(valid_input)
    SGD(x,y,x_valid,y_valid,num_epoch,hidden_units,init_flag,learning_rate,metrics_output,train_output,valid_output)