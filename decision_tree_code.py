
//import spark context and create row then do transformation
from pyspark import SparkContext
 sc = SparkContext()
from pyspark.sql import Row
l = [(1,25,4),(1,22,3),(1,20,4),(0,26,3),(1,23,4),(0,56,3),(0,77,8),(0,25,6),(5,20,8),(5,23,3)]
rdd=sc.parallelize(l)
people = rdd.map(lambda x: Row(name=int(x[0]), age=int(x[1]), group=int(x[2])))
df = sqlContext.createDataFrame(people)
df.collect()
from pyspark.sql.functions import col 
df = df.select([col(c).cast("double").alias(c) for c in df.columns])
df.printSchema()
train, test = df.randomSplit([0.7, 0.3])

//import required package

from pyspark.ml.feature import VectorAssembler, VectorIndexer
featuresCols = df.columns
featuresCols.remove('name')

// Many feature cols are merged to one single col
vectorAssembler = VectorAssembler(inputCols=featuresCols, outputCol="rawFeatures")

//used for identifying features and indexing them
vectorIndexer = VectorIndexer(inputCol="rawFeatures", outputCol="features", maxCategories=4)

from pyspark.ml.regression import GBTRegressor

// Takes the "features" column and learns to predict "cnt"
gbt = GBTRegressor(labelCol="name")


from pyspark.ml.tuning import CrossValidator, ParamGridBuilder
from pyspark.ml.evaluation import RegressionEvaluator

//define parameters such as maxDepth maxIter to test
paramGrid = ParamGridBuilder()\
  .addGrid(gbt.maxDepth, [2, 5])\
  .addGrid(gbt.maxIter, [10, 100])\
  .build()


//this tells how well it is doing with the help of labels
evaluator = RegressionEvaluator(metricName="rmse", labelCol=gbt.getLabelCol(), predictionCol=gbt.getPredictionCol())

//used for tuning the model
cv = CrossValidator(estimator=gbt, evaluator=evaluator, estimatorParamMaps=paramGrid)
from pyspark.ml import Pipeline
pipeline = Pipeline(stages=[vectorAssembler, vectorIndexer, cv])
//pipeline used for running feature processing, model tuning, and training 
pipelineModel = pipeline.fit(train)
//used for accuracy calculation
predictions = pipelineModel.transform(test)
//now evaluate the accuracy
accuracy = evaluator.evaluate(predictions)
print  accuracy










