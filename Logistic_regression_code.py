
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

//select cols from data frame
from pyspark.ml.feature import VectorAssembler, VectorIndexer
featuresCols = df.columns


from pyspark.ml import Pipeline
from pyspark.ml.feature import OneHotEncoder, StringIndexer, VectorAssembler

//specify the cols
categoricalColumns = ["name","age","group"]
stages = [] //stages in the pipeline
for categoricalCol in categoricalColumns:
//for indexing
	stringIndexer = StringIndexer(inputCol=categoricalCol, outputCol=categoricalCol+"Index")
	//for converting convert categorical variables into binary SparseVectors
	encoder = OneHotEncoder(inputCol=categoricalCol+"Index", outputCol=categoricalCol+"classVec")
	//add stages
	stages += [stringIndexer, encoder]

//convert to label indices
label_stringIdx = StringIndexer(inputCol = "name", outputCol = "label")
stages += [label_stringIdx]

//transformation of features into vectors
numericCols = ["age", "name", "group"]
assemblerInputs = map(lambda c: c + "classVec", categoricalColumns) + numericCols
assembler = VectorAssembler(inputCols=assemblerInputs, outputCol="features")
stages += [assembler]

//create pipeline
pipeline = Pipeline(stages=stages)

//pipeline used for running feature processing, model tuning, training and transforming
pipelineModel = pipeline.fit(df)
dataset = pipelineModel.transform(df)

//only keep required cols
selectedcols = ["label", "features"] + cols
dataset = dataset.select(selectedcols)

//random split of data set into train and test data
(trainingData, testData) = dataset.randomSplit([0.7, 0.3], seed = 100)


from pyspark.ml.classification
//create LogisticRegression model 
lr = LogisticRegression(labelCol="label", featuresCol="features", maxIter=10)

//train the model
lrModel = lr.fit(trainingData)

//use transform() and make predictions
predictions = lrModel.transform(testData)

predictions.printSchema()


selected = predictions.select("label","age",,"group"]
from pyspark.ml.evaluation import BinaryClassificationEvaluator

//  model Evaluation
evaluator = BinaryClassificationEvaluator(rawPredictionCol="rawPrediction")
evaluator.evaluate(predictions)


from pyspark.ml.tuning import ParamGridBuilder, CrossValidator

// define parameters such as lr.maxIter to test
paramGrid = (ParamGridBuilder()
             .addGrid(lr.regParam, [0.01, 0.5, 2.0])
             .addGrid(lr.elasticNetParam, [0.0, 0.5, 1.0])
             .addGrid(lr.maxIter, [1, 5, 10])
             .build())

cv = CrossValidator(estimator=gbt, estimatorParamMaps=paramGrid, evaluator=evaluator, numFolds=5)

// Run cross validations
cvModel = cv.fit(trainingData)

predictions = cvModel.transform(testData)

evaluator.evaluate(predictions)

accuracy = evaluator.evaluate(predictions)
print  accuracy
















