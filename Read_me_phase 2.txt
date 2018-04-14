Only Logistic Regression and Decision Trees are implemented MLib . Regarding Naive Bayes and SVM in class we are instructed to work on that later as there were some issues with it.




The Logistic Regression and Decision tree code are impleneted in spark and then copied to a .py file. Coping line by line into spark can execute the code. We implemented that in a small sample dataset this time to make sure things are working. 


In BFS and FFS the mapper maps the features into the reducer function.  This is also implemented on small data set just to see the working condition of the algorithm. 
The reducer function drawing features from the data sample using 10 fold cross validation. Here in reducer function we used the machine learning algorithm to find out the accuracy, later we will omit that part out as we are designing that separtely in MLib. Hence the mapper and the Reducer functions are subject to change in the final implementation. In forward and backward algorithm the features are added and removed from the feature subset according to their importance in accuracy. The code can be execute in any machine writing filename.py. The output contains the accuracy of the final feature subset and the feature subset. 




Similarly in Brute Force approach we have taken small subset of columns and implemented the approach in a permutation, combination manner to get the output.This is implemented in java. Curently we are using Apache POI for excel sheets. We have plans of moving to csv to avoid extra class files. 


We have implemented algorithms in small dataset for Phase-2 . 

In phase - 3 we will use it for the given data.
