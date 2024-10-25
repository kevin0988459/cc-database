# Note: Solve this question AFTER you finished reading the
# MySQL section of the writeup

# Read a SQL table to a DataFrame and calculate descriptive

# statistics
#
# Question:
# Print column-wise descriptive statistics of review count and stars
# columns in businesses table
#
# 1. the metrics required are as follows (in a tabular view):
#       review_count     stars
# count     ...           ...
# mean      ...           ...
# std       ...           ...
# min       ...           ...
# 25%       ...           ...
# 50%       ...           ...
# 75%       ...           ...
# max       ...           ...
# 2. the statistics result should be in CSV format
# 3. format each value to 2 decimal places
#
# A DataFrame represents a tabular, spreadsheet-like data structure containing
# an ordered collection of columns.
# As you notice, there is an intuitive relation between a DataFrame and a SQL
# table.
#
# Use the pandas lib to read a SQL table and generate descriptive statistics
# without having to reinvent the wheel. You will realize how easy it is to
# generate statistics using pandas on SQL tables.
#
# Hints:
# 1. pandas.read_sql can read the result of a query on a sql database
# into a DataFrame
# 2. figure out the sql query which can read the required columns from the
#  reviews table into the DataFrame
# 3. figure out how to establish a sql connection using MySQLdb
# 3. figure out the method of pandas.DataFrame which can calculate
# descriptive statistics
# 4. figure out the parameter of pandas.DataFrame.to_csv to format the
# floating point numbers.
#
# You may want to create a separate python script and execute
# the script in this method.
#
# Standard output format:
# count,174567.00,174567.00
# mean,30.14,3.63
# std,98.21,1.00
# min,3.00,1.00
# 25%,4.00,3.00
# 50%,8.00,3.50
# 75%,23.00,4.50
# max,7361.00,5.00

# You can solve the problem by filling in the correct method or parameters

import MySQLdb
import pandas as pd
import sys

conn = MySQLdb.connect(host='localhost', user='clouduser', passwd='dbroot', db='yelp_db')
query = "SELECT review_count, stars FROM businesses"
df = pd.read_sql(query, con=conn)
df.describe().to_csv(sys.stdout, encoding='utf-8', float_format='%.2f')