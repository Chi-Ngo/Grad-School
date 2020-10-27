#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Sep 12 14:52:55 2020

@author: chi
"""

import sys
import numpy as np
import csv

class Node:
    def __init__(self,data):
        self.left = None
        self.right = None
        self.data = data
        self.att = -1
        self.val = None
        self.depth = 0
        

def majorVote(node):
    labels = node.data[:,-1]
    y,count = np.unique(labels,return_counts=True)
    results = y[count==count.max()]
    if len(results) > 1:
        results = np.sort(results)
        vote = results[-1]
    else:
        vote = results[0]
    node.val = vote
    
def decisionStump(node,maxdepth,label,header): 
    if node.depth == int(maxdepth):
        majorVote(node)
        return 
    else:
        
        maxInfo = 0
        splitAtt = -1
        for i in range(len(node.data[0])-1):
            mI = mutualInfo(node.data,i)
            if mI > maxInfo:
                maxInfo = mI
                splitAtt = i
        if maxInfo <= 0:
            majorVote(node)
            return 
        else:
            node.att = splitAtt
            i = 0
            features = node.data[:,node.att]
            values = np.unique(features)
            left = np.zeros((0,len(node.data[0])))
            right = np.zeros((0,len(node.data[0])))
            for f in features:
                if f == values[0]:
                    left = np.vstack((left,node.data[i]))
                else:
                    right = np.vstack((right,node.data[i]))
                i += 1
                
            line = '| ' * (node.depth+1) + ' ' + header[splitAtt] + ' = ' + values[0] + ': ['
            for i in range(len(label)):
                line += (str(np.count_nonzero(left[:,-1]==label[i])) + ' ' + label[i] + '/')
            line = line[:-1]
            line += ']'
            print(line)
            node.left = Node(left)
            node.left.depth = node.depth+1
            decisionStump(node.left,maxdepth,label,header)
            
            line = '| ' * (node.depth+1) + ' ' + header[splitAtt] + ' = ' + values[1] + ': ['
            for i in range(len(label)):
                line += (str(np.count_nonzero(right[:,-1]==label[i])) + ' ' + label[i] + '/')
            line = line[:-1]
            line += ']'
            print(line)
            node.right = Node(right)
            node.right.depth = node.depth+1
            decisionStump(node.right,maxdepth,label,header)           
            
def decisionTree(traininput,maxdepth,trainoutput,metricsoutput):
    with open(traininput, newline='\n') as f:
        reader = csv.reader(f, delimiter='\t')
        header = next(reader)
        data = np.zeros((0,len(header)))
        for row in reader:
            data = np.vstack((data,row))
    root = Node(data)
    label,count=np.unique(data[:,-1],return_counts=True)
    line = '['
    for i in range(len(label)):
        line += (str(count[i]) + ' ' + label[i] + '/')
    line = line[:-1]
    line += ']'
    print(line)
    decisionStump(root,maxdepth,label,header)
    pred = predict(root,data,trainoutput)
    print('error(train)',error(data[:,-1],pred))
    with open(metricsoutput, 'w', newline='\n') as f:
        writer = csv.writer(f, delimiter=' ')
        writer.writerow(['error(train):', str (error(data[:,-1],pred))])
    return root
    
def predict(tree,data,outputfile):
    yhat = np.zeros((0,1))
    for row in data:
        node = tree
        while (node.left is not None) | (node.right is not None):
            f = row[node.att]
            features = node.data[:,node.att]
            values = np.unique(features)
            if f == values[0]:
                node = node.left
            else:
                node = node.right
        yhat = np.append(yhat,node.val)
    np.savetxt(outputfile, yhat, delimiter="\n",fmt='%s')
    return yhat


def test(tree,testinput,testoutput,metricsoutput):
    y = np.zeros((0,1))
    with open(testinput, newline='\n') as f:
        reader = csv.reader(f, delimiter='\t')
        header = next(reader)
        data = np.zeros((0,len(header)))
        for row in reader:
            data = np.vstack((data,row))
            y = np.append(y,row[-1])
    yhat = predict(tree,data,testoutput)
    print('error(test)',error(y,yhat))
    with open(metricsoutput, 'a', newline='\n') as f:
        writer = csv.writer(f, delimiter=' ')
        writer.writerow(['error(test):', str (error(y,yhat))])
       

def error(y, yHat):
    error = 0
    compare = (y==yHat)
    incorrectPred = np.count_nonzero(compare == False)
    error = incorrectPred/y.size
    return error  
    
def entropy(data):
    label = data[:,-1]
    y,count = np.unique(label,return_counts=True)
    total = count.sum()
    entropy = 0
    for i in range (len(count)):
        entropy -= count[i]/total * np.log2(count[i]/total) 
    return entropy

def entropyCond(data,X):
    result = 0
    values = data[:,X]
    x,countX = np.unique(values,return_counts=True)
    total = countX.sum()
    label = data[:,-1]
    values = np.vstack((values,label))
    values = values.transpose()
    #I refer to this site for line 165
    #https://stackoverflow.com/questions/58079075/numpy-select-rows-based-on-condition
    for i in range(len(x)):
        temp = values[values[:,0]==x[i],:]
        entrop = entropy(temp)
        result += entrop*(np.sum(values[:,0]==x[i])/total)
    return result
        

def mutualInfo(data,X):
    return entropy(data) - entropyCond(data,X)

if __name__ == '__main__':
    traininput = sys.argv[1]
    testinput = sys.argv[2]
    maxdepth = sys.argv[3]
    trainoutput = sys.argv[4]
    testoutput = sys.argv[5]
    metricsoutput = sys.argv[6]
    tree = decisionTree(traininput,maxdepth,trainoutput,metricsoutput)
    test(tree,testinput,testoutput,metricsoutput)