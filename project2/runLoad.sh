#!/bin/bash
#Drop all existing table by running drop.sql
mysql CS144 < drop.sql
#Create tables by running create.sql
mysql CS144 < create.sql
# Complie and run the parser to generate .del load files
# ant -buildfile build.xml run
ant
ant run-all

#Remove invalid entries due to primary key confliction
sort -u Users.del > nonDupUsers.del
rm Users.del
mv nonDupUsers.del Users.del
sort -u Item.del > nonDupItem.del
rm Item.del
mv nonDupItem.del Item.del
sort -u ItemCategory.del > nonDupIC.del
rm ItemCategory.del
mv nonDupIC.del ItemCategory.del
sort -u Bids.del > nonDupBids.del
rm Bids.del
mv nonDupBids.del Bids.del
#load data files into tables
mysql CS144 < load.sql
#Remove all temporary files
rm *.del
#Query
mysql CS144 < queries.sql