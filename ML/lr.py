#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Oct 10 16:30:21 2020

@author: chi
"""
import sys
import numpy as np
import math
import csv
import matplotlib.pyplot as plt

def opendict(dictfile):
    words = dict()
    with open(dictfile) as f:
        for row in f:
            words[row.split(' ')[0]] = row.split(' ')[1].strip()
    return len(words)

def readfile(inputfile,size):
    data = list()
    with open(inputfile) as f:
        for row in f:
            row = row.strip()
            line = row.split('\t')
            formattedline = list()
            formattedline.append(int(line[0]))
            for item in line[1:]:
                formattedline.append(int(item.split(':')[0]))
            formattedline.append(size)
            data.append(formattedline)
    return data

def lr(traindata,testdata,validdata,num_epoch,size,train_output,test_output,metrics_output):
    validJ = list()
    trainJ = list()
    w = np.zeros(size+1)
    alpha = 0.1
    num = 1
    N = len(traindata)
    N_valid = len(validdata)
    for num in range(num_epoch):
        train_obj = 0
        valid_obj = 0
        print('This is epoch',num)
        for i in range(N):
            label = traindata[i][0]
            features = traindata[i][1:]
            w = SGD(w,alpha,features,label,N)
        
        for i in range(N):
            label = traindata[i][0]
            features = traindata[i][1:]
            train_obj += np.log(1 + math.exp(sparse_dot(features,w))) - label*sparse_dot(features,w)
        trainJ.append(train_obj/N)
        
        for j in range(N_valid):
            label = validdata[j][0]
            features = validdata[j][1:]
            valid_obj += np.log(1 + math.exp(sparse_dot(features,w))) - label*sparse_dot(features,w)
        validJ.append(valid_obj/N_valid)
        
    train_error = predict(w,traindata,train_output)
    test_error = predict(w,testdata,test_output)
    
    with open(metrics_output, 'w', newline='\n') as f:
        writer = csv.writer(f, delimiter=' ')
        writer.writerow(['error(train):', str(train_error)])
        writer.writerow(['error(test):', str(test_error)])
    
    return trainJ,validJ

def SGD(w,alpha,features,label,N):
    product = sparse_dot(features,w)
    for j in features:
        w[j] = w[j] + (alpha/N) * (label - sigmoid(product))
    return w

        
def predict(w,data,filename):        
    error = 0
    pred = np.zeros((0,1))
    for row in data:
        label = row[0]
        features = row[1:]
        product = sparse_dot(features,w)
        prob = sigmoid(product)
        if prob >= 0.5:
            yhat = 1
        else:
            yhat = 0
        if yhat != label:
            error += 1
        pred = np.append(pred,yhat)
    np.savetxt(filename, pred.astype(int), delimiter="\n",fmt='%s')
    return (error/len(data))
    
    
def sparse_dot(X, W):
    product = 0.0
    for i in X:
        product += W[i]
    return product

def sigmoid(x):
    return math.exp(x)/(1 + math.exp(x))

if __name__ == '__main__':
    train_input = sys.argv[1] 
    valid_input = sys.argv[2]
    test_input = sys.argv[3]
    dict_input = sys.argv[4]
    size = opendict(dict_input)
    train_output = sys.argv[5]
    test_output = sys.argv[6]
    metrics_output = sys.argv[7]
    num_epoch = int(sys.argv[8])
    train_data = readfile(train_input,size)
    test_data = readfile(test_input,size)
    valid_data = readfile(valid_input,size)
    
    trainJ,validJ = lr(train_data,test_data,valid_data,num_epoch,size,train_output,test_output,metrics_output)
    
    epoch = list(range(1, num_epoch+1))
    #print(trainJ)
    #print(validJ)
    plt.plot(epoch,trainJ,epoch,validJ)
    plt.legend(['training','validation'])
    plt.ylabel('Objective Function')
    plt.xlabel('Number of Epochs')
    plt.show()
    