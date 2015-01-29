#!/bin/bash
#Drop all existing table by running drop.sql
mysql CS144 < drop.sql
#Create tables by running create.sql
mysql CS144 < create.sql
# Complie and run the parser to generate .csv load files
# ant -buildfile build.xml run
ant
ant run-all

#Remove invalid entries due to primary key confliction
sort -u Users.csv > nonDupUsers.csv
rm Users.csv
mv nonDupUsers.csv Users.csv
sort -u Item.csv > nonDupItem.csv
rm Items.csv
mv nonDupItem.csv Items.csv
sort -u ItemCategory.csv > nonDupIC.csv
rm ItemCategory.csv
mv nonDupIC.csv ItemCategory.csv
sort -u Bids.csv > nonDupBids.csv
rm Bids.csv
mv nonDupBids.csv Bids.csv
#load data files into tables
mysql CS144 < load.sql
#Remove all temporary files
rm *.csv
#Query
mysql CS144 < queries.sql