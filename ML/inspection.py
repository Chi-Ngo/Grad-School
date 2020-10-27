#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Sep 12 14:09:26 2020

@author: chi
"""
import sys
import numpy as np
import csv

def inspection(inputfile, outputfile):
    labels = np.zeros((0,1))
    with open(inputfile, newline='\n') as f:
        reader = csv.reader(f, delimiter='\t')
        next(reader)
        for row in reader:
            labels = np.append(labels,row[-1])
            
    values,count = np.unique(labels,return_counts=True)
    results = values[count==count.max()]
    
    if len(results) > 1:
        results = np.sort(results)
        vote = results[-1]
    else:
        vote = results[0]

    pred = np.full((len(labels),),vote)
    
    
    with open(outputfile, 'w', newline='\n') as f:
        writer = csv.writer(f, delimiter=' ')
        writer.writerow(['entropy:', str (entropyCalc(count))])
        writer.writerow(['error:', str (errorCalc(labels, pred))])
    
    
def errorCalc(y, yHat):
    error = 0
    compare = (y==yHat)
    incorrectPred = np.count_nonzero(compare == False)
    error = incorrectPred/y.size
    return error  
    
def entropyCalc(count):
    total = count.sum()
    entropy = 0
    for i in range (len(count)):
        entropy -= count[i]/total * np.log2(count[i]/total) 
    return entropy

if __name__ == '__main__':
    inputfile = sys.argv[1]
    outputfile = sys.argv[2]
    inspection(inputfile,outputfile)


