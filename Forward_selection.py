# Necessary libary functions works on Python 2.7.12. Here skelearn is used on iris dataset to show the function of the forward feature selection algorithm .
# We used sklearn pacaakge for machine learning algorithm here because in spark also we will use MLib anyway.
#Any other pacakges used here are found to be working with pyspark or that part of the algorithm is already implemented using the given dataset (dataset given in class).


import scipy.io
from sklearn.cross_validation import KFold
from sklearn import svm
from sklearn.metrics import accuracy_score
import pandas as pd 
from sklearn.svm import SVC
import numpy as np


#Wrapper method
# Mapper function is used in mapping the features into the reducer code.
# We used only one mapper and reducer here. iris dataset is used here.

def mapper_svm_forward():
     
     for n_features in X.shape:
      print '%s\t%s' % (n_features,1) 

# Reducer function
# drawing features from the data sample
# using 10 fold cross validation, here we used 10 just as standard which
# is subject to change according to our own dataset in pysark and svm
def reducer_svm_forward(X, y, n_selected_features):
    
    # Initially the feature set is subjected to be empty as
    # in FFS we keep adding the features as we progress
    sub_set = []
    count = 0

    n_samples, n_features = X.shape
     # SVM used as the classifier, here we only used SVM just to showing the working condition of the code
    # similarly other algorithms like Decision Tree, Naive Bayes and Logistic regression an also be used to do the same,
    # using same procedure
    classify = KFold(n_samples, n_folds=10, shuffle=True)
   
    function = SVC()

    
    while count < n_selected_features:
        max_acc = 0 # the maximum accuracy is expected to be zero and is updated with the feature importance
        for i in range(n_features):
            if i not in sub_set:
                sub_set.append(i)
                X_tmp = X[:, sub_set]
                acc = 0 
                # features in train set and test set to be reduced to find the smallest subset
                for train, test in classify:
                    function.fit(X_tmp[train], y[train])
                    y_predict = function.predict(X_tmp[test])
                    acc_tmp = accuracy_score(y[test], y_predict)
                    acc += acc_tmp - 0.03
                # here we used a threshold of 0.03 to maintain the accuracy within a limit, 
                    # without that value the obtained subset of features will give the maximum accuracy which is equivaent to the 
                    # original accuracy of the machine learning algorithm 
                acc = float(acc)/10
                sub_set.pop()
                 # BFS is designed in terms of feature importance so we
                # record those feature which contributed to the largest accuracy 
                if acc > max_acc:
                    max_acc = acc
                    idx = i
        # add the feature which results in the largest accuracy
        sub_set.append(idx)
        count += 1
    return np.array(sub_set)

#Main function
# iris dataset file
data = pd.read_csv("https://raw.githubusercontent.com/uiuc-cse/data-fa14/gh-pages/data/iris.csv")

# truncate the dataset in to features and labels

X = data.iloc[:, :-1].values
y = data.iloc[:, -1].values

n_samples, n_features = X.shape    # number of samples and number of features

# Using KFold cross validation
validate = KFold(n_samples, n_folds=10, shuffle=True)

    
function = svm.LinearSVC()

correct = 0



for train, test in validate:
        # selected features and the data set is obtained from the training set having largest accuracy
        idx = reducer_svm_forward(X[train], y[train], n_features)

       
        X_data = X[:, idx]

       # classification model with the selected features on the training dataset
        function.fit(X_data[train], y[train])

        # class labels prediction of test data
        y_predict = function.predict(X_data[test])

        # obtain the classification accuracy on the test data
        acc = accuracy_score(y[test], y_predict)
	correct = correct + acc
# print the accuracy within threshold
print 'Accuracy:', float(correct)*10

#print the subset
print X_data

